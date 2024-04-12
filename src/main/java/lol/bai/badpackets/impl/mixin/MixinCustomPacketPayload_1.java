package lol.bai.badpackets.impl.mixin;

import lol.bai.badpackets.impl.registry.ChannelCodecFinder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(targets = "net.minecraft.network.protocol.common.custom.CustomPacketPayload$1")
public class MixinCustomPacketPayload_1 implements ChannelCodecFinder.Holder {

    @Unique
    @Nullable
    private ChannelCodecFinder badpackets_channelCodecFinder = null;

    @Override
    public void badpackets_setChannelCodecFinder(ChannelCodecFinder finder) {
        this.badpackets_channelCodecFinder = finder;
    }

    @Inject(method = "writeCap", cancellable = true, at = @At(value = "INVOKE", target = "net/minecraft/network/protocol/common/custom/CustomPacketPayload$1.findCodec(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/network/codec/StreamCodec;"))
    private void badpackets_encode(FriendlyByteBuf buf, CustomPacketPayload.Type<?> type, CustomPacketPayload payload, CallbackInfo ci) {
        if (badpackets_channelCodecFinder == null) return;

        var codec = badpackets_channelCodecFinder.getCodec(type.id(), buf);
        if (codec != null) {
            codec.encode(buf, payload);
            ci.cancel();
        }
    }

    @Inject(method = "decode", cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", target = "net/minecraft/network/protocol/common/custom/CustomPacketPayload$1.findCodec(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/network/codec/StreamCodec;"))
    private void badpackets_decode(FriendlyByteBuf buf, CallbackInfoReturnable<CustomPacketPayload> cir, ResourceLocation id) {
        if (badpackets_channelCodecFinder == null) return;

        var codec = badpackets_channelCodecFinder.getCodec(id, buf);
        if (codec != null) cir.setReturnValue(codec.decode(buf));
    }

}
