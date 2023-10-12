package lol.bai.badpackets.impl.fabric;

import lol.bai.badpackets.impl.Constants;
import lol.bai.badpackets.impl.platform.PlatformProxy;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class FabricProxy extends PlatformProxy {

    private static final boolean HAS_FABRIC_API = FabricLoader.getInstance().isModLoaded("fabric-networking-api-v1");

    @Override
    public Packet<?> createVanillaRegisterConfigS2CPacket(FriendlyByteBuf buf) {
        return HAS_FABRIC_API
            ? ServerConfigurationNetworking.createS2CPacket(Constants.MC_REGISTER_CHANNEL, buf)
            : super.createVanillaRegisterConfigS2CPacket(buf);
    }

    @Override
    public Packet<?> createVanillaRegisterConfigC2SPacket(FriendlyByteBuf buf) {
        return HAS_FABRIC_API
            ? ClientConfigurationNetworking.createC2SPacket(Constants.MC_REGISTER_CHANNEL, buf)
            : super.createVanillaRegisterConfigC2SPacket(buf);
    }

    @Override
    public Packet<?> createVanillaRegisterPlayS2CPacket(FriendlyByteBuf buf) {
        return HAS_FABRIC_API
            ? ServerPlayNetworking.createS2CPacket(Constants.MC_REGISTER_CHANNEL, buf)
            : super.createVanillaRegisterPlayS2CPacket(buf);
    }

    @Override
    public Packet<?> createVanillaRegisterPlayC2SPacket(FriendlyByteBuf buf) {
        return HAS_FABRIC_API
            ? ClientPlayNetworking.createC2SPacket(Constants.MC_REGISTER_CHANNEL, buf)
            : super.createVanillaRegisterPlayC2SPacket(buf);
    }

}
