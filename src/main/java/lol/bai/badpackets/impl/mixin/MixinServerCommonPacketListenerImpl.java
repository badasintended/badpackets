package lol.bai.badpackets.impl.mixin;

import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundPongPacket;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerCommonPacketListenerImpl.class)
public class MixinServerCommonPacketListenerImpl {

    @Inject(method = "handleCustomPayload", at = @At("HEAD"), cancellable = true)
    private void badpackets_handleCustomPayload(ServerboundCustomPayloadPacket packet, CallbackInfo ci) {
        if (badpackets_handleCustomPayload(packet)) ci.cancel();
    }

    @Inject(method = "handlePong", at = @At("HEAD"))
    private void badpackets_onPong(ServerboundPongPacket packet, CallbackInfo ci) {
        badpackets_onPong(packet.getId());
    }

    @Unique
    protected boolean badpackets_handleCustomPayload(ServerboundCustomPayloadPacket packet) {
        return false;
    }

    @Unique
    protected void badpackets_onPong(int id) {
    }

}
