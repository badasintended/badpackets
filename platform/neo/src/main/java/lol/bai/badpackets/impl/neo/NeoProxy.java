package lol.bai.badpackets.impl.neo;

import lol.bai.badpackets.impl.Constants;
import lol.bai.badpackets.impl.platform.PlatformProxy;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.neoforged.neoforge.network.custom.payload.SimplePayload;

public class NeoProxy extends PlatformProxy {

    @Override
    public Packet<?> createVanillaRegisterConfigS2CPacket(FriendlyByteBuf buf) {
        return new ClientboundCustomPayloadPacket(SimplePayload.outbound(buf, 0, Constants.MC_REGISTER_CHANNEL));
    }

    @Override
    public Packet<?> createVanillaRegisterConfigC2SPacket(FriendlyByteBuf buf) {
        return new ServerboundCustomPayloadPacket(SimplePayload.outbound(buf, 0, Constants.MC_REGISTER_CHANNEL));
    }

    @Override
    public Packet<?> createVanillaRegisterPlayS2CPacket(FriendlyByteBuf buf) {
        return new ClientboundCustomPayloadPacket(SimplePayload.outbound(buf, 0, Constants.MC_REGISTER_CHANNEL));
    }

    @Override
    public Packet<?> createVanillaRegisterPlayC2SPacket(FriendlyByteBuf buf) {
        return new ServerboundCustomPayloadPacket(SimplePayload.outbound(buf, 0, Constants.MC_REGISTER_CHANNEL));
    }

}
