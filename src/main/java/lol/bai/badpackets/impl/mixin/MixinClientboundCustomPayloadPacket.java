package lol.bai.badpackets.impl.mixin;

import java.util.function.Function;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import lol.bai.badpackets.impl.handler.AbstractPacketHandler;
import lol.bai.badpackets.impl.registry.ChannelCodecFinder;
import lol.bai.badpackets.impl.registry.ChannelRegistry;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.ClientCommonPacketListener;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
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

    @WrapOperation(method = "<clinit>", at = @At(value = "INVOKE", ordinal = 0, target = "net/minecraft/network/codec/StreamCodec.map(Ljava/util/function/Function;Ljava/util/function/Function;)Lnet/minecraft/network/codec/StreamCodec;"))
    private static StreamCodec<?, ?> badpackets_attachPlayChannelCodecs(StreamCodec<?, ?> codec, Function<?, ?> to, Function<?, ?> from, Operation<StreamCodec<?, ?>> map) {
        ChannelCodecFinder.attach(codec, ChannelRegistry.PLAY_S2C);
        return map.call(codec, to, from);
    }

    @WrapOperation(method = "<clinit>", at = @At(value = "INVOKE", ordinal = 1, target = "net/minecraft/network/codec/StreamCodec.map(Ljava/util/function/Function;Ljava/util/function/Function;)Lnet/minecraft/network/codec/StreamCodec;"))
    private static StreamCodec<?, ?> badpackets_attachConfigChannelCodecs(StreamCodec<?, ?> codec, Function<?, ?> to, Function<?, ?> from, Operation<StreamCodec<?, ?>> map) {
        ChannelCodecFinder.attach(codec, ChannelRegistry.CONFIG_S2C);
        return map.call(codec, to, from);
    }

}
