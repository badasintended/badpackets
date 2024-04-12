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
import org.jetbrains.annotations.Nullable;

public class ChannelRegistry<B extends FriendlyByteBuf, R> implements ChannelCodecFinder {

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
    private final Map<ResourceLocation, R> receivers = new HashMap<>();
    private final Set<AbstractPacketHandler<R, B>> handlers = new HashSet<>();

    private final ReentrantReadWriteLock locks = new ReentrantReadWriteLock();

    private ChannelRegistry(Set<ResourceLocation> reservedChannels) {
        this.reservedChannels = reservedChannels;

        codecs.put(Constants.CHANNEL_SYNC, UntypedPayload.codec(Constants.CHANNEL_SYNC));
    }

    public <P extends CustomPacketPayload> void registerCodec(ResourceLocation id, StreamCodec<? super B, P> codec) {
        Lock lock = locks.writeLock();
        lock.lock();

        try {
            if (reservedChannels.contains(id)) {
                throw new IllegalArgumentException("Reserved channel id " + id);
            }

            codecs.put(id, codec);
        } finally {
            lock.unlock();
        }
    }

    public void registerReceiver(ResourceLocation id, R receiver) {
        Lock lock = locks.writeLock();
        lock.lock();

        try {
            if (!codecs.containsKey(id)) {
                throw new IllegalArgumentException("Unknown channel id " + id);
            }

            receivers.put(id, receiver);
            for (AbstractPacketHandler<R, B> handler : handlers) {
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
            return receivers.containsKey(id);
        } finally {
            lock.unlock();
        }
    }

    public R get(ResourceLocation id) {
        Lock lock = locks.readLock();
        lock.lock();

        try {
            return receivers.get(id);
        } finally {
            lock.unlock();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public @Nullable StreamCodec<FriendlyByteBuf, CustomPacketPayload> getCodec(ResourceLocation id, FriendlyByteBuf buf) {
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
            return new HashSet<>(receivers.keySet());
        } finally {
            lock.unlock();
        }
    }

    public void addHandler(AbstractPacketHandler<R, B> handler) {
        Lock lock = locks.writeLock();
        lock.lock();

        try {
            handlers.add(handler);
        } finally {
            lock.unlock();
        }
    }

    public void removeHandler(AbstractPacketHandler<R, B> handler) {
        Lock lock = locks.writeLock();
        lock.lock();

        try {
            handlers.remove(handler);
        } finally {
            lock.unlock();
        }
    }

}
