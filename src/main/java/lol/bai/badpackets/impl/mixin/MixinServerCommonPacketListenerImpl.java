package lol.bai.badpackets.impl.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.ServerboundPongPacket;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerCommonPacketListenerImpl.class)
public class MixinServerCommonPacketListenerImpl {

    @Inject(method = "handlePong", at = @At("HEAD"))
    private void badpackets_onPong(ServerboundPongPacket packet, CallbackInfo ci) {
        badpackets_onPong(packet.getId());
    }

    @Inject(method = "onDisconnect", at = @At("HEAD"))
    private void badpackets_removePacketHandler(Component reason, CallbackInfo ci) {
        badpackets_removePacketHandler();
    }

    @Unique
    protected void badpackets_removePacketHandler() {
    }

    @Unique
    protected void badpackets_onPong(int id) {
    }

}
