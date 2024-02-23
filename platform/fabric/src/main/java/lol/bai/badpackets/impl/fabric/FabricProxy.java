package lol.bai.badpackets.impl.fabric;

import java.util.Set;
import java.util.function.Supplier;

import lol.bai.badpackets.impl.Constants;
import lol.bai.badpackets.impl.platform.PlatformProxy;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;

public class FabricProxy extends PlatformProxy {

    private static final boolean HAS_FABRIC_API = FabricLoader.getInstance().isModLoaded("fabric-networking-api-v1");
    private static final boolean HAS_QUILT_STD = FabricLoader.getInstance().isModLoaded("quilt_networking");

    @Override
    public boolean canSendVanillaRegisterPackets() {
        return !HAS_QUILT_STD;
    }

    @Override
    public Packet<?> createVanillaRegisterConfigS2CPacket(Set<ResourceLocation> channels, Supplier<FriendlyByteBuf> buf) {
        return HAS_FABRIC_API
            ? ServerConfigurationNetworking.createS2CPacket(Constants.MC_REGISTER_CHANNEL, buf.get())
            : super.createVanillaRegisterConfigS2CPacket(channels, buf);
    }

    @Override
    public Packet<?> createVanillaRegisterConfigC2SPacket(Set<ResourceLocation> channels, Supplier<FriendlyByteBuf> buf) {
        return HAS_FABRIC_API
            ? ClientConfigurationNetworking.createC2SPacket(Constants.MC_REGISTER_CHANNEL, buf.get())
            : super.createVanillaRegisterConfigC2SPacket(channels, buf);
    }

    @Override
    public Packet<?> createVanillaRegisterPlayS2CPacket(Set<ResourceLocation> channels, Supplier<FriendlyByteBuf> buf) {
        return HAS_FABRIC_API
            ? ServerPlayNetworking.createS2CPacket(Constants.MC_REGISTER_CHANNEL, buf.get())
            : super.createVanillaRegisterPlayS2CPacket(channels, buf);
    }

    @Override
    public Packet<?> createVanillaRegisterPlayC2SPacket(Set<ResourceLocation> channels, Supplier<FriendlyByteBuf> buf) {
        return HAS_FABRIC_API
            ? ClientPlayNetworking.createC2SPacket(Constants.MC_REGISTER_CHANNEL, buf.get())
            : super.createVanillaRegisterPlayC2SPacket(channels, buf);
    }

}
