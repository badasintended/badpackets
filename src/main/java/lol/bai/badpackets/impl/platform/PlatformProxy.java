package lol.bai.badpackets.impl.platform;

import java.util.ServiceLoader;

import lol.bai.badpackets.impl.Constants;
import lol.bai.badpackets.impl.payload.UntypedPayload;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;

public class PlatformProxy {

    public static final PlatformProxy INSTANCE = ServiceLoader.load(PlatformProxy.class).findFirst().orElseGet(PlatformProxy::new);

    public boolean canSendVanillaRegisterPackets() {
        return true;
    }

    public Packet<?> createVanillaRegisterConfigC2SPacket(FriendlyByteBuf buf) {
        return new ServerboundCustomPayloadPacket(new UntypedPayload(Constants.MC_REGISTER_CHANNEL, buf));
    }

    public Packet<?> createVanillaRegisterConfigS2CPacket(FriendlyByteBuf buf) {
        return new ClientboundCustomPayloadPacket(new UntypedPayload(Constants.MC_REGISTER_CHANNEL, buf));
    }

    public Packet<?> createVanillaRegisterPlayC2SPacket(FriendlyByteBuf buf) {
        return new ServerboundCustomPayloadPacket(new UntypedPayload(Constants.MC_REGISTER_CHANNEL, buf));
    }

    public Packet<?> createVanillaRegisterPlayS2CPacket(FriendlyByteBuf buf) {
        return new ClientboundCustomPayloadPacket(new UntypedPayload(Constants.MC_REGISTER_CHANNEL, buf));
    }

}
