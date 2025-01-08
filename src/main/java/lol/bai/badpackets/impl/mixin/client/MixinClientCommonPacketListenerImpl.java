package lol.bai.badpackets.impl.mixin.client;

import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ClientboundPingPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientCommonPacketListenerImpl.class)
public abstract class MixinClientCommonPacketListenerImpl {

    @Inject(method = "handlePing", at = @At("HEAD"), cancellable = true)
    private void badpackets_handlePing(ClientboundPingPacket packet, CallbackInfo ci) {
        if (badpackets_handlePing(packet.getId())) ci.cancel();
    }

    @Inject(method = "handleCustomPayload(Lnet/minecraft/network/protocol/common/ClientboundCustomPayloadPacket;)V", at = @At("HEAD"), cancellable = true)
    private void badpackets_handleCustomPayload(ClientboundCustomPayloadPacket packet, CallbackInfo ci) {
        if (badpackets_handleCustomPayload(packet)) ci.cancel();
    }

    @Unique
    protected boolean badpackets_handlePing(int id) {
        return false;
    }

    @Unique
    protected boolean badpackets_handleCustomPayload(ClientboundCustomPayloadPacket packet) {
        return false;
    }

}
