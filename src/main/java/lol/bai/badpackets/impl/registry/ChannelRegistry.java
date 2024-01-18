package lol.bai.badpackets.impl.registry;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import lol.bai.badpackets.api.config.ClientConfigPacketReceiver;
import lol.bai.badpackets.api.config.ServerConfigPacketReceiver;
import lol.bai.badpackets.api.play.ClientPlayPacketReceiver;
import lol.bai.badpackets.api.play.ServerPlayPacketReceiver;
import lol.bai.badpackets.impl.Constants;
import lol.bai.badpackets.impl.handler.AbstractPacketHandler;
import lol.bai.badpackets.impl.payload.UntypedPayload;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public class ChannelRegistry<B extends FriendlyByteBuf, R> {

    private static final Set<ResourceLocation> RESERVED_CHANNELS = Set.of(
        Constants.CHANNEL_SYNC,
        Constants.MC_REGISTER_CHANNEL,
        Constants.MC_UNREGISTER_CHANNEL);

    public static final ChannelRegistry<FriendlyByteBuf, ClientConfigPacketReceiver<CustomPacketPayload>> CONFIG_S2C = new ChannelRegistry<>(RESERVED_CHANNELS);
    public static final ChannelRegistry<FriendlyByteBuf, ServerConfigPacketReceiver<CustomPacketPayload>> CONFIG_C2S = new ChannelRegistry<>(RESERVED_CHANNELS);

    public static final ChannelRegistry<RegistryFriendlyByteBuf, ClientPlayPacketReceiver<CustomPacketPayload>> PLAY_S2C = new ChannelRegistry<>(RESERVED_CHANNELS);
    public static final ChannelRegistry<RegistryFriendlyByteBuf, ServerPlayPacketReceiver<CustomPacketPayload>> PLAY_C2S = new ChannelRegistry<>(RESERVED_CHANNELS);

    private final Set<ResourceLocation> reservedChannels;

    private final Map<ResourceLocation, StreamCodec<?, ?>> codecs = new HashMap<>();
    private final Map<ResourceLocation, R> channels = new HashMap<>();
    private final Set<AbstractPacketHandler<R>> handlers = new HashSet<>();

    private final ReentrantReadWriteLock locks = new ReentrantReadWriteLock();

    private ChannelRegistry(Set<ResourceLocation> reservedChannels) {
        this.reservedChannels = reservedChannels;

        codecs.put(Constants.CHANNEL_SYNC, UntypedPayload.codec(Constants.CHANNEL_SYNC));
    }

    public <P extends CustomPacketPayload> void register(CustomPacketPayload.Type<P> type, StreamCodec<? super B, P> codec, R receiver) {
        Lock lock = locks.writeLock();
        lock.lock();

        try {
            if (reservedChannels.contains(type.id())) {
                throw new IllegalArgumentException("Reserved channel id " + type.id());
            }

            codecs.put(type.id(), codec);
            channels.put(type.id(), receiver);
            for (AbstractPacketHandler<R> handler : handlers) {
                handler.onRegister(type.id());
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

    public R get(ResourceLocation id) {
        Lock lock = locks.readLock();
        lock.lock();

        try {
            return channels.get(id);
        } finally {
            lock.unlock();
        }
    }

    @SuppressWarnings("unchecked")
    public StreamCodec<FriendlyByteBuf, CustomPacketPayload> getCodec(ResourceLocation id) {
        Lock lock = locks.readLock();
        lock.lock();

        try {
            return (StreamCodec<FriendlyByteBuf, CustomPacketPayload>) codecs.get(id);
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

    public void addHandler(AbstractPacketHandler<R> handler) {
        Lock lock = locks.writeLock();
        lock.lock();

        try {
            handlers.add(handler);
        } finally {
            lock.unlock();
        }
    }

    public void removeHandler(AbstractPacketHandler<R> handler) {
        Lock lock = locks.writeLock();
        lock.lock();

        try {
            handlers.remove(handler);
        } finally {
            lock.unlock();
        }
    }

}
