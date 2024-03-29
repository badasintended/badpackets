package lol.bai.badpackets.impl.forge;

import lol.bai.badpackets.impl.Constants;
import lol.bai.badpackets.impl.platform.PlatformProxy;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.DiscardedPayload;

public class ForgeProxy extends PlatformProxy {

    @Override
    public Packet<?> createVanillaRegisterConfigS2CPacket(FriendlyByteBuf buf) {
        return new ClientboundCustomPayloadPacket(new DiscardedPayload(Constants.MC_REGISTER_CHANNEL, buf));
    }

    @Override
    public Packet<?> createVanillaRegisterConfigC2SPacket(FriendlyByteBuf buf) {
        return new ServerboundCustomPayloadPacket(new DiscardedPayload(Constants.MC_REGISTER_CHANNEL, buf));
    }

    @Override
    public Packet<?> createVanillaRegisterPlayS2CPacket(FriendlyByteBuf buf) {
        return new ClientboundCustomPayloadPacket(new DiscardedPayload(Constants.MC_REGISTER_CHANNEL, buf));
    }

    @Override
    public Packet<?> createVanillaRegisterPlayC2SPacket(FriendlyByteBuf buf) {
        return new ServerboundCustomPayloadPacket(new DiscardedPayload(Constants.MC_REGISTER_CHANNEL, buf));
    }

}
