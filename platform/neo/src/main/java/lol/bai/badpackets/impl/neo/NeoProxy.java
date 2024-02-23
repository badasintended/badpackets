package lol.bai.badpackets.impl.neo;

import java.util.Set;
import java.util.function.Supplier;

import lol.bai.badpackets.impl.platform.PlatformProxy;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.payload.MinecraftRegisterPayload;

public class NeoProxy extends PlatformProxy {

    @Override
    public Packet<?> createVanillaRegisterConfigS2CPacket(Set<ResourceLocation> channels, Supplier<FriendlyByteBuf> buf) {
        return new ClientboundCustomPayloadPacket(new MinecraftRegisterPayload(channels));
    }

    @Override
    public Packet<?> createVanillaRegisterConfigC2SPacket(Set<ResourceLocation> channels, Supplier<FriendlyByteBuf> buf) {
        return new ServerboundCustomPayloadPacket(new MinecraftRegisterPayload(channels));
    }

    @Override
    public Packet<?> createVanillaRegisterPlayS2CPacket(Set<ResourceLocation> channels, Supplier<FriendlyByteBuf> buf) {
        return new ClientboundCustomPayloadPacket(new MinecraftRegisterPayload(channels));
    }

    @Override
    public Packet<?> createVanillaRegisterPlayC2SPacket(Set<ResourceLocation> channels, Supplier<FriendlyByteBuf> buf) {
        return new ServerboundCustomPayloadPacket(new MinecraftRegisterPayload(channels));
    }

}
