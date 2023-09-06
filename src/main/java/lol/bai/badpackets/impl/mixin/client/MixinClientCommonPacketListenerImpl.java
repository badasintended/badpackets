package lol.bai.badpackets.impl.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.ClientboundPingPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientCommonPacketListenerImpl.class)
public abstract class MixinClientCommonPacketListenerImpl {

    @Shadow
    @Final
    protected Minecraft minecraft;

    @Inject(method = "onDisconnect", at = @At("HEAD"))
    private void badpackets_removeClientPacketHandler(Component reason, CallbackInfo ci) {
        badpackets_removeClientPacketHandler(reason);
    }

    @Inject(method = "handlePing", at = @At("HEAD"), cancellable = true)
    private void badpackets_handlePing(ClientboundPingPacket packet, CallbackInfo ci) {
        if (badpackets_handlePing(packet.getId())) ci.cancel();
    }

    @Unique
    protected void badpackets_removeClientPacketHandler(Component reason) {
    }

    @Unique
    protected boolean badpackets_handlePing(int id) {
        return false;
    }

}
