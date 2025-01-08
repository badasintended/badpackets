package lol.bai.badpackets.impl.mixin;

import lol.bai.badpackets.impl.handler.AbstractPacketHandler;
import lol.bai.badpackets.impl.handler.PacketHandlerHolder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.ServerCommonPacketListener;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(ServerboundCustomPayloadPacket.class)
public abstract class MixinServerboundCustomPayloadPacket {

    @Shadow
    @Final
    @Mutable
    private static Map<ResourceLocation, FriendlyByteBuf.Reader<? extends CustomPacketPayload>> KNOWN_TYPES;

    @Shadow
    public abstract CustomPacketPayload payload();

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void badpackets_makeReaderMapMutable(CallbackInfo ci) {
        KNOWN_TYPES = new HashMap<>(KNOWN_TYPES);
        AbstractPacketHandler.addChannelSyncReader(KNOWN_TYPES);
    }

    @Inject(method = "handle(Lnet/minecraft/network/protocol/common/ServerCommonPacketListener;)V", at = @At("HEAD"), cancellable = true)
    private void badpackets_handle(ServerCommonPacketListener listener, CallbackInfo ci) {
        if (listener instanceof PacketHandlerHolder<?> holder && holder.badpackets_handler().receive(payload())) {
            ci.cancel();
        }
    }

}
