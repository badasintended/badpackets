package lol.bai.badpackets.impl.mixin;

import java.util.List;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import lol.bai.badpackets.impl.handler.AbstractPacketHandler;
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

    @WrapOperation(method = "<clinit>", at = @At(value = "INVOKE", target = "net/minecraft/network/protocol/common/custom/CustomPacketPayload.codec(Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload$FallbackProvider;Ljava/util/List;)Lnet/minecraft/network/codec/StreamCodec;"))
    private static StreamCodec<?, ?> badpackets_attachChannelCodecs(CustomPacketPayload.FallbackProvider<?> fallbackProvider, List<?> list, Operation<StreamCodec<?, ?>> original) {
        var codec = original.call(fallbackProvider, list);
        ChannelCodecFinder.attach(codec, (id, buf) -> {
            var registry = buf instanceof RegistryFriendlyByteBuf ? ChannelRegistry.PLAY_C2S : ChannelRegistry.CONFIG_C2S;
            return registry.getCodec(id, buf);
        });
        return codec;
    }

//    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "net/minecraft/network/protocol/common/custom/CustomPacketPayload.codec(Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload$FallbackProvider;Ljava/util/List;)Lnet/minecraft/network/codec/StreamCodec;"))
//    private static CustomPacketPayload.FallbackProvider<FriendlyByteBuf> badpackets_getCodec(CustomPacketPayload.FallbackProvider<FriendlyByteBuf> original) {
//        return id -> {
//            var playCodec = (StreamCodec<RegistryFriendlyByteBuf, CustomPacketPayload>) ChannelRegistry.PLAY_C2S.getCodec(id);
//            var configCodec = (StreamCodec<FriendlyByteBuf, CustomPacketPayload>) ChannelRegistry.CONFIG_C2S.getCodec(id);
//            var discardedCodec = (StreamCodec<FriendlyByteBuf, CustomPacketPayload>) original.create(id);
//
//            if (playCodec != null || configCodec != null) {
//                return new StreamCodec<>() {
//                    @Override
//                    public CustomPacketPayload decode(FriendlyByteBuf buf) {
//                        if (buf instanceof RegistryFriendlyByteBuf registryFriendlyByteBuf) {
//                            if (playCodec != null) return playCodec.decode(registryFriendlyByteBuf);
//                        } else {
//                            if (configCodec != null) return configCodec.decode(buf);
//                        }
//
//                        return discardedCodec.decode(buf);
//                    }
//
//                    @Override
//                    public void encode(FriendlyByteBuf buf, CustomPacketPayload payload) {
//                        if (buf instanceof RegistryFriendlyByteBuf registryFriendlyByteBuf) {
//                            if (playCodec != null) {
//                                playCodec.encode(registryFriendlyByteBuf, payload);
//                                return;
//                            }
//                        } else {
//                            if (configCodec != null) {
//                                configCodec.encode(buf, payload);
//                                return;
//                            }
//                        }
//
//                        discardedCodec.encode(buf, payload);
//                    }
//                };
//            }
//
//            return discardedCodec;
//        };
//    }


}
