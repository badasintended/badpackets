package lol.bai.badpackets.impl.platform;

import java.util.ServiceLoader;

import lol.bai.badpackets.impl.Constants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.DiscardedPayload;

public class PlatformProxy {

    public static final PlatformProxy INSTANCE = ServiceLoader.load(PlatformProxy.class).findFirst().orElseGet(PlatformProxy::new);

    public boolean canSendVanillaRegisterPackets() {
        return true;
    }

    public Packet<?> createVanillaRegisterConfigC2SPacket(FriendlyByteBuf buf) {
        return new ServerboundCustomPayloadPacket(new DiscardedPayload(Constants.MC_REGISTER_CHANNEL));
    }

    public Packet<?> createVanillaRegisterConfigS2CPacket(FriendlyByteBuf buf) {
        return new ClientboundCustomPayloadPacket(new DiscardedPayload(Constants.MC_REGISTER_CHANNEL));
    }

    public Packet<?> createVanillaRegisterPlayC2SPacket(FriendlyByteBuf buf) {
        return new ServerboundCustomPayloadPacket(new DiscardedPayload(Constants.MC_REGISTER_CHANNEL));
    }

    public Packet<?> createVanillaRegisterPlayS2CPacket(FriendlyByteBuf buf) {
        return new ClientboundCustomPayloadPacket(new DiscardedPayload(Constants.MC_REGISTER_CHANNEL));
    }

}
