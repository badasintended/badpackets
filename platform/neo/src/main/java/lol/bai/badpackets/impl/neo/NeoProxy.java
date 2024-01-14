package lol.bai.badpackets.impl.neo;

import lol.bai.badpackets.impl.platform.PlatformProxy;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

// TODO: NeoForge has different format for minecraft:register. Support it again when it reuse the old format.
public class NeoProxy extends PlatformProxy {

    @Override
    public boolean canSendVanillaRegisterPackets() {
        return false;
    }

    @Override
    public Packet<?> createVanillaRegisterConfigS2CPacket(FriendlyByteBuf buf) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Packet<?> createVanillaRegisterConfigC2SPacket(FriendlyByteBuf buf) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Packet<?> createVanillaRegisterPlayS2CPacket(FriendlyByteBuf buf) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Packet<?> createVanillaRegisterPlayC2SPacket(FriendlyByteBuf buf) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
