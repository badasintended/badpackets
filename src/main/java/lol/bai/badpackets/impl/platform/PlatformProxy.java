package lol.bai.badpackets.impl.platform;

import java.util.ServiceLoader;
import java.util.Set;
import java.util.function.Consumer;

import io.netty.buffer.Unpooled;
import lol.bai.badpackets.impl.Constants;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class PlatformProxy {

    public static final PlatformProxy INSTANCE = ServiceLoader.load(PlatformProxy.class).findFirst().orElseGet(PlatformProxy::new);

    public boolean canSendVanillaRegisterPackets() {
        return true;
    }

    @SuppressWarnings("unchecked")
    private static <T extends FriendlyByteBuf> T createBuf(@Nullable RegistryAccess registry, Consumer<T> writer) {
        var buf = (T) (registry != null ? new RegistryFriendlyByteBuf(Unpooled.buffer(), registry) : new FriendlyByteBuf(Unpooled.buffer()));
        buf.writeResourceLocation(Constants.MC_REGISTER_CHANNEL);
        writer.accept(buf);
        return buf;
    }

    public Packet<?> createVanillaRegisterConfigC2SPacket(Set<ResourceLocation> channels, Consumer<FriendlyByteBuf> writer) {
        return ServerboundCustomPayloadPacket.STREAM_CODEC.decode(createBuf(null, writer));
    }

    public Packet<?> createVanillaRegisterConfigS2CPacket(Set<ResourceLocation> channels, Consumer<FriendlyByteBuf> writer) {
        return ClientboundCustomPayloadPacket.CONFIG_STREAM_CODEC.decode(createBuf(null, writer));
    }

    public Packet<?> createVanillaRegisterPlayC2SPacket(RegistryAccess registry, Set<ResourceLocation> channels, Consumer<? extends FriendlyByteBuf> writer) {
        return ServerboundCustomPayloadPacket.STREAM_CODEC.decode(createBuf(registry, writer));
    }

    public Packet<?> createVanillaRegisterPlayS2CPacket(RegistryAccess registry, Set<ResourceLocation> channels, Consumer<RegistryFriendlyByteBuf> writer) {
        return ClientboundCustomPayloadPacket.GAMEPLAY_STREAM_CODEC.decode(createBuf(registry, writer));
    }

}
