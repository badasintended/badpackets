package lol.bai.badpackets.impl.registry;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.google.common.base.Suppliers;
import lol.bai.badpackets.api.config.ClientConfigPacketReceiver;
import lol.bai.badpackets.api.config.ServerConfigPacketReceiver;
import lol.bai.badpackets.api.play.ClientPlayPacketReceiver;
import lol.bai.badpackets.api.play.ServerPlayPacketReceiver;
import lol.bai.badpackets.impl.Constants;
import lol.bai.badpackets.impl.handler.AbstractPacketHandler;
import lol.bai.badpackets.impl.mixin.AccessServerboundCustomPayloadPacket;
import lol.bai.badpackets.impl.mixin.client.AccessClientboundCustomPayloadPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public class ChannelRegistry<T> {

    private static final Set<ResourceLocation> RESERVED_CHANNELS = Set.of(
        Constants.CHANNEL_SYNC,
        Constants.MC_REGISTER_CHANNEL,
        Constants.MC_UNREGISTER_CHANNEL);

    private static final Supplier<ReaderMapHolder> C2S_READERS = Suppliers.memoize(() -> new ReaderMapHolder(AccessServerboundCustomPayloadPacket::badpackets_getPacketReaders, AccessServerboundCustomPayloadPacket::badpackets_setPacketReaders));
    private static final Supplier<ReaderMapHolder> S2C_READERS = Suppliers.memoize(() -> new ReaderMapHolder(AccessClientboundCustomPayloadPacket::badpackets_getPacketReaders, AccessClientboundCustomPayloadPacket::badpackets_setPacketReaders));

    public static final ChannelRegistry<ClientConfigPacketReceiver<CustomPacketPayload>> CONFIG_S2C = new ChannelRegistry<>(RESERVED_CHANNELS, S2C_READERS);
    public static final ChannelRegistry<ServerConfigPacketReceiver<CustomPacketPayload>> CONFIG_C2S = new ChannelRegistry<>(RESERVED_CHANNELS, C2S_READERS);

    public static final ChannelRegistry<ClientPlayPacketReceiver<CustomPacketPayload>> PLAY_S2C = new ChannelRegistry<>(RESERVED_CHANNELS, S2C_READERS);
    public static final ChannelRegistry<ServerPlayPacketReceiver<CustomPacketPayload>> PLAY_C2S = new ChannelRegistry<>(RESERVED_CHANNELS, C2S_READERS);

    private final Map<ResourceLocation, T> channels = new HashMap<>();
    private final Set<ResourceLocation> reservedChannels;
    private final Set<AbstractPacketHandler<T>> handlers = new HashSet<>();
    private final Supplier<ReaderMapHolder> readersHolder;

    private final ReentrantReadWriteLock locks = new ReentrantReadWriteLock();

    private ChannelRegistry(Set<ResourceLocation> reservedChannels, Supplier<ReaderMapHolder> readersHolder) {
        this.reservedChannels = reservedChannels;
        this.readersHolder = readersHolder;
    }

    public void register(ResourceLocation id, FriendlyByteBuf.Reader<? extends CustomPacketPayload> reader, T receiver) {
        Lock lock = locks.writeLock();
        lock.lock();

        try {
            if (reservedChannels.contains(id)) {
                throw new IllegalArgumentException("Reserved channel id " + id);
            }

            Map<ResourceLocation, FriendlyByteBuf.Reader<? extends CustomPacketPayload>> readers = readersHolder.get().getter.get();
            if (!(readers instanceof HashMap)) {
                readers = new HashMap<>(readers);
                readersHolder.get().setter.accept(readers);
            }

            readers.put(id, reader);
            channels.put(id, receiver);
            for (AbstractPacketHandler<T> handler : handlers) {
                handler.onRegister(id);
            }
        } finally {
            lock.unlock();
        }
    }

    public boolean has(ResourceLocation id) {
        Lock lock = locks.readLock();
        lock.lock();

        try {
            return channels.containsKey(id);
        } finally {
            lock.unlock();
        }
    }

    public T get(ResourceLocation id) {
        Lock lock = locks.readLock();
        lock.lock();

        try {
            return channels.get(id);
        } finally {
            lock.unlock();
        }
    }

    public Set<ResourceLocation> getChannels() {
        Lock lock = locks.readLock();
        lock.lock();

        try {
            return new HashSet<>(channels.keySet());
        } finally {
            lock.unlock();
        }
    }

    public void addHandler(AbstractPacketHandler<T> handler) {
        Lock lock = locks.writeLock();
        lock.lock();

        try {
            handlers.add(handler);
        } finally {
            lock.unlock();
        }
    }

    public void removeHandler(AbstractPacketHandler<T> handler) {
        Lock lock = locks.writeLock();
        lock.lock();

        try {
            handlers.remove(handler);
        } finally {
            lock.unlock();
        }
    }

    private record ReaderMapHolder(
        Supplier<Map<ResourceLocation, FriendlyByteBuf.Reader<? extends CustomPacketPayload>>> getter,
        Consumer<Map<ResourceLocation, FriendlyByteBuf.Reader<? extends CustomPacketPayload>>> setter
    ) {

    }

}
