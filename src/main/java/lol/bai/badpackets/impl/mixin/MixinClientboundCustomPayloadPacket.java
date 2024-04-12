package lol.bai.badpackets.impl.mixin;

import java.util.List;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import lol.bai.badpackets.impl.handler.AbstractPacketHandler;
import lol.bai.badpackets.impl.registry.ChannelCodecFinder;
import lol.bai.badpackets.impl.registry.ChannelRegistry;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.ClientCommonPacketListener;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.FallbackProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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

    @WrapOperation(method = "<clinit>", at = @At(value = "INVOKE", ordinal = 0, target = "net/minecraft/network/protocol/common/custom/CustomPacketPayload.codec(Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload$FallbackProvider;Ljava/util/List;)Lnet/minecraft/network/codec/StreamCodec;"))
    private static StreamCodec<?, ?> badpackets_attachPlayChannelCodecs(FallbackProvider<?> fallbackProvider, List<?> list, Operation<StreamCodec<?, ?>> original) {
        var codec = original.call(fallbackProvider, list);
        ChannelCodecFinder.attach(codec, ChannelRegistry.PLAY_S2C);
        return codec;
    }

    @WrapOperation(method = "<clinit>", at = @At(value = "INVOKE", ordinal = 1, target = "net/minecraft/network/protocol/common/custom/CustomPacketPayload.codec(Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload$FallbackProvider;Ljava/util/List;)Lnet/minecraft/network/codec/StreamCodec;"))
    private static StreamCodec<?, ?> badpackets_attachConfigChannelCodecs(FallbackProvider<?> fallbackProvider, List<?> list, Operation<StreamCodec<?, ?>> original) {
        var codec = original.call(fallbackProvider, list);
        ChannelCodecFinder.attach(codec, ChannelRegistry.CONFIG_S2C);
        return codec;
    }

//    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", ordinal = 0, target = "net/minecraft/network/protocol/common/custom/CustomPacketPayload.codec(Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload$FallbackProvider;Ljava/util/List;)Lnet/minecraft/network/codec/StreamCodec;"))
//    private static CustomPacketPayload.FallbackProvider<?> badpackets_getPlayCodec(CustomPacketPayload.FallbackProvider<?> original) {
//        return id -> {
//            var codec = ChannelRegistry.PLAY_S2C.getCodec(id, null);
//            if (codec != null) return codec;
//            return original.create(id);
//        };
//    }
//
//    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", ordinal = 1, target = "net/minecraft/network/protocol/common/custom/CustomPacketPayload.codec(Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload$FallbackProvider;Ljava/util/List;)Lnet/minecraft/network/codec/StreamCodec;"))
//    private static CustomPacketPayload.FallbackProvider<FriendlyByteBuf> badpackets_getConfigCodec(CustomPacketPayload.FallbackProvider<FriendlyByteBuf> original) {
//        return id -> {
//            var codec = ChannelRegistry.CONFIG_S2C.getCodec(id);
//            if (codec != null) return codec;
//            return original.create(id);
//        };
//    }

}
