package lol.bai.badpackets.impl.mixin;

import io.netty.channel.ChannelHandlerContext;
import lol.bai.badpackets.impl.handler.PacketHandlerHolder;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.ProtocolInfo;
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

    @Inject(method = "setupInboundProtocol", at = @At("HEAD"))
    private <T extends PacketListener> void badpackets_cleanOldListener(ProtocolInfo<T> info, T listener, CallbackInfo ci) {
        badpackets_cleanListener();
    }

    @Inject(method = "channelInactive", at = @At("HEAD"))
    private void badpackets_cleanListener(ChannelHandlerContext ctx, CallbackInfo ci) {
        badpackets_cleanListener();
    }

    @Inject(method = "handleDisconnection", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/PacketListener;onDisconnect(Lnet/minecraft/network/DisconnectionDetails;)V"))
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
