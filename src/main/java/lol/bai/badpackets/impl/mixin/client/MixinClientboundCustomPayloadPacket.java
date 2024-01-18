package lol.bai.badpackets.impl.mixin.client;

import lol.bai.badpackets.impl.handler.AbstractPacketHandler;
import lol.bai.badpackets.impl.registry.ChannelRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.ClientCommonPacketListener;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientboundCustomPayloadPacket.class)
public abstract class MixinClientboundCustomPayloadPacket {

    @Shadow
    public abstract CustomPacketPayload payload();

    @Inject(method = "handle(Lnet/minecraft/network/protocol/common/ClientCommonPacketListener;)V", at = @At("HEAD"), cancellable = true)
    private void badpackets_handle(ClientCommonPacketListener listener, CallbackInfo ci) {
        if (listener instanceof AbstractPacketHandler.Holder holder && holder.badpackets_receive(payload())) {
            ci.cancel();
        }
    }

    @Unique
    private static void badpackets_getPlayCodec(ResourceLocation id, CallbackInfoReturnable<StreamCodec<FriendlyByteBuf, CustomPacketPayload>> cir) {
        var codec = ChannelRegistry.PLAY_S2C.getCodec(id);
        if (codec != null) cir.setReturnValue(codec);
    }

    @Unique
    private static void badpackets_getConfigCodec(ResourceLocation id, CallbackInfoReturnable<StreamCodec<FriendlyByteBuf, CustomPacketPayload>> cir) {
        var codec = ChannelRegistry.CONFIG_S2C.getCodec(id);
        if (codec != null) cir.setReturnValue(codec);
    }

    @Inject(method = "lambda$static$0", at = @At("HEAD"), cancellable = true, require = 0)
    private static void badpackets_getPlayCodec_mojmap(ResourceLocation id, CallbackInfoReturnable<StreamCodec<FriendlyByteBuf, CustomPacketPayload>> cir) {
        badpackets_getPlayCodec(id, cir);
    }

    @Inject(method = "lambda$static$1", at = @At("HEAD"), cancellable = true, require = 0)
    private static void badpackets_getConfigCodec_mojmap(ResourceLocation id, CallbackInfoReturnable<StreamCodec<FriendlyByteBuf, CustomPacketPayload>> cir) {
        badpackets_getConfigCodec(id, cir);
    }

    @SuppressWarnings({"UnresolvedMixinReference", "MixinAnnotationTarget"})
    @Inject(method = "method_56461", at = @At("HEAD"), cancellable = true, require = 0)
    private static void badpackets_getPlayCodec_intermediary(ResourceLocation id, CallbackInfoReturnable<StreamCodec<FriendlyByteBuf, CustomPacketPayload>> cir) {
        badpackets_getPlayCodec(id, cir);
    }

    @SuppressWarnings({"UnresolvedMixinReference", "MixinAnnotationTarget"})
    @Inject(method = "method_56460", at = @At("HEAD"), cancellable = true, require = 0)
    private static void badpackets_getConfigCodec_intermediary(ResourceLocation id, CallbackInfoReturnable<StreamCodec<FriendlyByteBuf, CustomPacketPayload>> cir) {
        badpackets_getConfigCodec(id, cir);
    }

}
