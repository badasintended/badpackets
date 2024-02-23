package lol.bai.badpackets.impl.forge;

import java.util.Set;
import java.util.function.Supplier;

import lol.bai.badpackets.impl.Constants;
import lol.bai.badpackets.impl.platform.PlatformProxy;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.DiscardedPayload;
import net.minecraft.resources.ResourceLocation;

public class ForgeProxy extends PlatformProxy {

    @Override
    public Packet<?> createVanillaRegisterConfigS2CPacket(Set<ResourceLocation> channels, Supplier<FriendlyByteBuf> buf) {
        return new ClientboundCustomPayloadPacket(new DiscardedPayload(Constants.MC_REGISTER_CHANNEL, buf.get()));
    }

    @Override
    public Packet<?> createVanillaRegisterConfigC2SPacket(Set<ResourceLocation> channels, Supplier<FriendlyByteBuf> buf) {
        return new ServerboundCustomPayloadPacket(new DiscardedPayload(Constants.MC_REGISTER_CHANNEL, buf.get()));
    }

    @Override
    public Packet<?> createVanillaRegisterPlayS2CPacket(Set<ResourceLocation> channels, Supplier<FriendlyByteBuf> buf) {
        return new ClientboundCustomPayloadPacket(new DiscardedPayload(Constants.MC_REGISTER_CHANNEL, buf.get()));
    }

    @Override
    public Packet<?> createVanillaRegisterPlayC2SPacket(Set<ResourceLocation> channels, Supplier<FriendlyByteBuf> buf) {
        return new ServerboundCustomPayloadPacket(new DiscardedPayload(Constants.MC_REGISTER_CHANNEL, buf.get()));
    }

}
