package lol.bai.badpackets.impl.mixin;

import io.netty.channel.ChannelHandlerContext;
import lol.bai.badpackets.impl.handler.PacketHandlerHolder;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public class MixinConnection {

    @Shadow
    @Nullable
    private volatile PacketListener packetListener;

    @Inject(method = "setListener", at = @At("HEAD"))
    private void badpackets_cleanOldListener(PacketListener $$0, CallbackInfo ci) {
        badpackets_cleanListener();
    }

    @Inject(method = "channelInactive", at = @At("HEAD"))
    private void badpackets_cleanListener(ChannelHandlerContext $$0, CallbackInfo ci) {
        badpackets_cleanListener();
    }

    @Inject(method = "handleDisconnection", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/PacketListener;onDisconnect(Lnet/minecraft/network/chat/Component;)V"))
    private void badpackets_cleanListener(CallbackInfo ci) {
        badpackets_cleanListener();
    }

    @Unique
    private void badpackets_cleanListener() {
        if (this.packetListener instanceof PacketHandlerHolder<?> holder) {
            holder.badpackets_handler().remove();
        }
    }

}
