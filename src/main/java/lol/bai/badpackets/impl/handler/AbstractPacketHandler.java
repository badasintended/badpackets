package lol.bai.badpackets.impl.handler;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.netty.buffer.Unpooled;
import lol.bai.badpackets.api.PacketSender;
import lol.bai.badpackets.impl.Constants;
import lol.bai.badpackets.impl.payload.UntypedPayload;
import lol.bai.badpackets.impl.registry.ChannelRegistry;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractPacketHandler<T> implements PacketSender {

    protected final ChannelRegistry<T> registry;
    protected final Logger logger;

    private final Function<CustomPacketPayload, Packet<?>> packetFactory;
    private final Set<ResourceLocation> sendableChannels = new HashSet<>();

    private final Connection connection;

    private boolean initialized = false;

    protected AbstractPacketHandler(String desc, ChannelRegistry<T> registry, Function<CustomPacketPayload, Packet<?>> packetFactory, Connection connection) {
        this.logger = LogManager.getLogger(desc);
        this.registry = registry;
        this.packetFactory = packetFactory;
        this.connection = connection;

        registry.addHandler(this);
    }

    private void receiveChannelSyncPacket(FriendlyByteBuf buf) {
        switch (buf.readByte()) {
            case Constants.CHANNEL_SYNC_SINGLE -> sendableChannels.add(buf.readResourceLocation());
            case Constants.CHANNEL_SYNC_INITIAL -> {
                int groupSize = buf.readVarInt();
                for (int i = 0; i < groupSize; i++) {
                    String namespace = buf.readUtf();

                    int pathSize = buf.readVarInt();
                    for (int j = 0; j < pathSize; j++) {
                        String path = buf.readUtf();
                        sendableChannels.add(new ResourceLocation(namespace, path));
                    }
                }
                onInitialChannelSyncPacketReceived();
            }
        }
    }

    public static void addChannelSyncReader(Map<ResourceLocation, FriendlyByteBuf.Reader<? extends CustomPacketPayload>> map) {
        map.put(Constants.CHANNEL_SYNC, UntypedPayload.reader(Constants.CHANNEL_SYNC));
    }

    public boolean receive(CustomPacketPayload payload) {
        ResourceLocation id = payload.id();

        if (id.equals(Constants.CHANNEL_SYNC)) {
            UntypedPayload untyped = (UntypedPayload) payload;
            receiveChannelSyncPacket(untyped.buffer());
            return true;
        }

        if (registry.channels.containsKey(id)) {
            try {
                receive(registry.channels.get(id), payload);
            } catch (Throwable t) {
                logger.error("Error when receiving packet {}", id, t);
                throw t;
            }
            return true;
        }

        return false;
    }

    protected abstract void onInitialChannelSyncPacketReceived();

    protected abstract void receive(T receiver, CustomPacketPayload payload);

    public void sendInitialChannelSyncPacket() {
        if (!initialized) {
            initialized = true;
            sendVanillaChannelRegisterPacket(Set.of(Constants.CHANNEL_SYNC));

            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeByte(Constants.CHANNEL_SYNC_INITIAL);

            Map<String, List<ResourceLocation>> group = registry.channels.keySet().stream().collect(Collectors.groupingBy(ResourceLocation::getNamespace));
            buf.writeVarInt(group.size());

            for (Map.Entry<String, List<ResourceLocation>> entry : group.entrySet()) {
                buf.writeUtf(entry.getKey());
                buf.writeVarInt(entry.getValue().size());

                for (ResourceLocation value : entry.getValue()) {
                    buf.writeUtf(value.getPath());
                }
            }

            send(Constants.CHANNEL_SYNC, buf);
            sendVanillaChannelRegisterPacket(registry.channels.keySet());
        }
    }

    public void onRegister(ResourceLocation id) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeByte(Constants.CHANNEL_SYNC_SINGLE);
        buf.writeResourceLocation(id);
        send(Constants.CHANNEL_SYNC, buf);
        sendVanillaChannelRegisterPacket(Set.of(id));
    }

    public void remove() {
        registry.removeHandler(this);
    }

    private void sendVanillaChannelRegisterPacket(Set<ResourceLocation> channels) {
        if (!channels.isEmpty()) {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            boolean first = true;
            for (ResourceLocation channel : channels) {
                if (first) {
                    first = false;
                } else {
                    buf.writeByte(0);
                }
                buf.writeBytes(channel.toString().getBytes(StandardCharsets.US_ASCII));
            }
            send(Constants.MC_REGISTER_CHANNEL, buf);
        }
    }

    @Override
    public void send(CustomPacketPayload payload, @Nullable PacketSendListener callback) {
        connection.send(packetFactory.apply(payload), callback);
    }

    @Override
    public boolean canSend(ResourceLocation id) {
        return sendableChannels.contains(id);
    }

}
