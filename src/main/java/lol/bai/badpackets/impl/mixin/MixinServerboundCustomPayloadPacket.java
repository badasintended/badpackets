package lol.bai.badpackets.impl.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import lol.bai.badpackets.impl.handler.PacketHandlerHolder;
import lol.bai.badpackets.impl.registry.ChannelCodecFinder;
import lol.bai.badpackets.impl.registry.ChannelRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.ServerCommonPacketListener;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@Mixin(ServerboundCustomPayloadPacket.class)
public abstract class MixinServerboundCustomPayloadPacket {

    @Shadow
    public abstract CustomPacketPayload payload();

    @Inject(method = "handle(Lnet/minecraft/network/protocol/common/ServerCommonPacketListener;)V", at = @At("HEAD"), cancellable = true)
    private void badpackets_handle(ServerCommonPacketListener listener, CallbackInfo ci) {
        if (listener instanceof PacketHandlerHolder<?> holder && holder.badpackets_handler().receive(payload())) {
            ci.cancel();
        }
    }

    @WrapOperation(method = "<clinit>", at = @At(value = "INVOKE", target = "net/minecraft/network/codec/StreamCodec.map(Ljava/util/function/Function;Ljava/util/function/Function;)Lnet/minecraft/network/codec/StreamCodec;"))
    private static StreamCodec<?, ?> badpackets_attachChannelCodecs(StreamCodec<?, ?> codec, Function<?, ?> to, Function<?, ?> from, Operation<StreamCodec<?, ?>> map) {
        ChannelCodecFinder.attach(codec, (id, buf) -> {
            var registry = buf instanceof RegistryFriendlyByteBuf ? ChannelRegistry.PLAY_C2S : ChannelRegistry.CONFIG_C2S;
            return registry.getCodec(id, buf);
        });
        return map.call(codec, to, from);
    }

}
