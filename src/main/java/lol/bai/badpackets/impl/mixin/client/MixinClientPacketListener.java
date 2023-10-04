package lol.bai.badpackets.impl.mixin.client;

import lol.bai.badpackets.impl.handler.ClientPlayPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundStartConfigurationPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class MixinClientPacketListener extends MixinClientCommonPacketListenerImpl implements ClientPlayPacketHandler.Holder {

    @Unique
    private ClientPlayPacketHandler badpacket_packetHandler;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void badpackets_createClientPacketHandler(Minecraft client, Connection connection, CommonListenerCookie cookie, CallbackInfo ci) {
        badpacket_packetHandler = new ClientPlayPacketHandler(client, (ClientPacketListener) (Object) this);
    }

    @Override
    protected void badpackets_removeClientPacketHandler(Component reason) {
        badpacket_packetHandler.remove();
    }

    @Inject(method = "handleCustomPayload", at = @At("HEAD"), cancellable = true)
    private void badpackets_receiveS2CPacket(CustomPacketPayload payload, CallbackInfo ci) {
        if (badpacket_packetHandler.receive(payload)) ci.cancel();
    }

    @Inject(method = "handleConfigurationStart", at = @At("RETURN"))
    private void badpackets_removeHandler(ClientboundStartConfigurationPacket $$0, CallbackInfo ci) {
        badpacket_packetHandler.remove();
    }

    @Override
    public ClientPlayPacketHandler badpackets_getHandler() {
        return badpacket_packetHandler;
    }

}
