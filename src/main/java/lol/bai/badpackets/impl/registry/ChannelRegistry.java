package lol.bai.badpackets.impl.registry;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import lol.bai.badpackets.api.config.ConfigPackets;
import lol.bai.badpackets.api.play.PlayPackets;
import lol.bai.badpackets.impl.Constants;
import lol.bai.badpackets.impl.handler.AbstractPacketHandler;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public class ChannelRegistry<T> {

    private static final Set<ResourceLocation> RESERVED_CHANNELS = Set.of(
        Constants.CHANNEL_SYNC,
        Constants.MC_REGISTER_CHANNEL,
        Constants.MC_UNREGISTER_CHANNEL);

    public static final ChannelRegistry<ConfigPackets.ClientReceiver<CustomPacketPayload>> CONFIG_S2C = new ChannelRegistry<>(RESERVED_CHANNELS);
    public static final ChannelRegistry<ConfigPackets.ServerReceiver<CustomPacketPayload>> CONFIG_C2S = new ChannelRegistry<>(RESERVED_CHANNELS);

    public static final ChannelRegistry<PlayPackets.ClientReceiver<CustomPacketPayload>> PLAY_S2C = new ChannelRegistry<>(RESERVED_CHANNELS);
    public static final ChannelRegistry<PlayPackets.ServerReceiver<CustomPacketPayload>> PLAY_C2S = new ChannelRegistry<>(RESERVED_CHANNELS);

    public final Map<ResourceLocation, T> channels = new HashMap<>();

    private final Set<ResourceLocation> reservedChannels;
    private final Set<AbstractPacketHandler<T>> handlers = new HashSet<>();

    public ChannelRegistry(Set<ResourceLocation> reservedChannels) {
        this.reservedChannels = reservedChannels;
    }

    public void register(ResourceLocation id, T receiver) {
        if (reservedChannels.contains(id)) {
            throw new IllegalArgumentException("Reserved channel id " + id);
        }

        channels.put(id, receiver);
        for (AbstractPacketHandler<T> handler : handlers) {
            handler.onRegister(id);
        }
    }

    public void addHandler(AbstractPacketHandler<T> handler) {
        handlers.add(handler);
    }

    public void removeHandler(AbstractPacketHandler<T> handler) {
        handlers.remove(handler);
    }

}
