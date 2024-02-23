package lol.bai.badpackets.impl.platform;

import java.util.ServiceLoader;
import java.util.Set;
import java.util.function.Supplier;

import lol.bai.badpackets.impl.Constants;
import lol.bai.badpackets.impl.payload.UntypedPayload;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;

public class PlatformProxy {

    public static final PlatformProxy INSTANCE = ServiceLoader.load(PlatformProxy.class).findFirst().orElseGet(PlatformProxy::new);

    public boolean canSendVanillaRegisterPackets() {
        return true;
    }

    public Packet<?> createVanillaRegisterConfigC2SPacket(Set<ResourceLocation> channels, Supplier<FriendlyByteBuf> buf) {
        return new ServerboundCustomPayloadPacket(new UntypedPayload(Constants.MC_REGISTER_CHANNEL, buf.get()));
    }

    public Packet<?> createVanillaRegisterConfigS2CPacket(Set<ResourceLocation> channels, Supplier<FriendlyByteBuf> buf) {
        return new ClientboundCustomPayloadPacket(new UntypedPayload(Constants.MC_REGISTER_CHANNEL, buf.get()));
    }

    public Packet<?> createVanillaRegisterPlayC2SPacket(Set<ResourceLocation> channels, Supplier<FriendlyByteBuf> buf) {
        return new ServerboundCustomPayloadPacket(new UntypedPayload(Constants.MC_REGISTER_CHANNEL, buf.get()));
    }

    public Packet<?> createVanillaRegisterPlayS2CPacket(Set<ResourceLocation> channels, Supplier<FriendlyByteBuf> buf) {
        return new ClientboundCustomPayloadPacket(new UntypedPayload(Constants.MC_REGISTER_CHANNEL, buf.get()));
    }

}
