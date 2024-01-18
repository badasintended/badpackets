package lol.bai.badpackets.impl.fabric;

import lol.bai.badpackets.impl.platform.PlatformProxy;
import net.fabricmc.loader.api.FabricLoader;

public class FabricProxy extends PlatformProxy {

    private static final boolean HAS_FABRIC_API = FabricLoader.getInstance().isModLoaded("fabric-networking-api-v1");

    // TODO
    // @Override
    // public Packet<?> createVanillaRegisterConfigS2CPacket(FriendlyByteBuf buf) {
    //     return HAS_FABRIC_API
    //         ? ServerConfigurationNetworking.createS2CPacket(Constants.MC_REGISTER_CHANNEL, buf)
    //         : super.createVanillaRegisterConfigS2CPacket(buf);
    // }

    // @Override
    // public Packet<?> createVanillaRegisterConfigC2SPacket(FriendlyByteBuf buf) {
    //     return HAS_FABRIC_API
    //         ? ClientConfigurationNetworking.createC2SPacket(Constants.MC_REGISTER_CHANNEL, buf)
    //         : super.createVanillaRegisterConfigC2SPacket(buf);
    // }

    // @Override
    // public Packet<?> createVanillaRegisterPlayS2CPacket(FriendlyByteBuf buf) {
    //     return HAS_FABRIC_API
    //         ? ServerPlayNetworking.createS2CPacket(Constants.MC_REGISTER_CHANNEL, buf)
    //         : super.createVanillaRegisterPlayS2CPacket(buf);
    // }

    // @Override
    // public Packet<?> createVanillaRegisterPlayC2SPacket(FriendlyByteBuf buf) {
    //     return HAS_FABRIC_API
    //         ? ClientPlayNetworking.createC2SPacket(Constants.MC_REGISTER_CHANNEL, buf)
    //         : super.createVanillaRegisterPlayC2SPacket(buf);
    // }

}
