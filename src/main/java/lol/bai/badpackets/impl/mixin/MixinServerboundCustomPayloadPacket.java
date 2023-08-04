package lol.bai.badpackets.impl.mixin;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.network.FriendlyByteBuf;
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

@Mixin(ServerboundCustomPayloadPacket.class)
public class MixinServerboundCustomPayloadPacket {

    @Shadow
    @Final
    @Mutable
    private static Map<ResourceLocation, FriendlyByteBuf.Reader<? extends CustomPacketPayload>> KNOWN_TYPES;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void badpackets_makeReaderMapMutable(CallbackInfo ci) {
        KNOWN_TYPES = new HashMap<>(KNOWN_TYPES);
    }

}
