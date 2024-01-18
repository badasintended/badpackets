package lol.bai.badpackets.impl.mixin;

import lol.bai.badpackets.impl.handler.AbstractPacketHandler;
import lol.bai.badpackets.impl.registry.ChannelRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.ServerCommonPacketListener;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerboundCustomPayloadPacket.class)
public abstract class MixinServerboundCustomPayloadPacket {

    @Shadow
    public abstract CustomPacketPayload payload();

    @Inject(method = "handle(Lnet/minecraft/network/protocol/common/ServerCommonPacketListener;)V", at = @At("HEAD"), cancellable = true)
    private void badpackets_handle(ServerCommonPacketListener listener, CallbackInfo ci) {
        if (listener instanceof AbstractPacketHandler.Holder holder && holder.badpackets_receive(payload())) {
            ci.cancel();
        }
    }

    @Inject(method = "lambda$static$0", at = @At("HEAD"), cancellable = true, require = 0)
    private static void badpackets_getCodec_mojmap(ResourceLocation id, CallbackInfoReturnable<StreamCodec<FriendlyByteBuf, CustomPacketPayload>> cir) {
        badpackets_getCodec(id, cir);
    }

    @SuppressWarnings({"UnresolvedMixinReference", "MixinAnnotationTarget"})
    @Inject(method = "method_56475", at = @At("HEAD"), cancellable = true, require = 0)
    private static void badpackets_getCodec_intermediary(ResourceLocation id, CallbackInfoReturnable<StreamCodec<FriendlyByteBuf, CustomPacketPayload>> cir) {
        badpackets_getCodec(id, cir);
    }

    @Unique
    private static void badpackets_getCodec(ResourceLocation id, CallbackInfoReturnable<StreamCodec<FriendlyByteBuf, CustomPacketPayload>> cir) {
        var codec = ChannelRegistry.PLAY_C2S.getCodec(id);
        if (codec == null) codec = ChannelRegistry.CONFIG_C2S.getCodec(id);
        if (codec != null) cir.setReturnValue(codec);
    }

}
