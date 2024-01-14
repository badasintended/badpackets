package lol.bai.badpackets.impl.mixin.client;

import java.util.HashMap;
import java.util.Map;

import lol.bai.badpackets.impl.handler.AbstractPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.ClientCommonPacketListener;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientboundCustomPayloadPacket.class)
public abstract class MixinClientboundCustomPayloadPacket {

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

    @Inject(method = "handle(Lnet/minecraft/network/protocol/common/ClientCommonPacketListener;)V", at = @At("HEAD"), cancellable = true)
    private void badpackets_handle(ClientCommonPacketListener listener, CallbackInfo ci) {
        if (listener instanceof AbstractPacketHandler.Holder holder && holder.badpackets_receive(payload())) {
            ci.cancel();
        }
    }

}
