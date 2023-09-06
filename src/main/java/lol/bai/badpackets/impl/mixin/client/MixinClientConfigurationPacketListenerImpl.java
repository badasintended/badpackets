package lol.bai.badpackets.impl.mixin.client;

import lol.bai.badpackets.impl.Constants;
import lol.bai.badpackets.impl.handler.ClientConfigPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientConfigurationPacketListenerImpl;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.configuration.ClientboundFinishConfigurationPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConfigurationPacketListenerImpl.class)
public class MixinClientConfigurationPacketListenerImpl extends MixinClientCommonPacketListenerImpl {

    @Unique
    private ClientConfigPacketHandler badpackets_packetHandler;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void badpackets_createPacketHandler(Minecraft minecraft, Connection connection, CommonListenerCookie cookie, CallbackInfo ci) {
        badpackets_packetHandler = new ClientConfigPacketHandler(minecraft, (ClientConfigurationPacketListenerImpl) (Object) this, connection);
    }

    @Inject(method = "handleCustomPayload", at = @At("HEAD"), cancellable = true)
    private void badpackets_receiveS2CPacket(CustomPacketPayload payload, CallbackInfo ci) {
        if (badpackets_packetHandler.receive(payload)) ci.cancel();
    }

    @Inject(method = "handleConfigurationFinished", at = @At("RETURN"))
    private void badpacekts_removePacketHandler(ClientboundFinishConfigurationPacket $$0, CallbackInfo ci) {
        badpackets_packetHandler.remove();
    }

    @Override
    protected void badpackets_removeClientPacketHandler(Component reason) {
        badpackets_packetHandler.remove();
    }

    @Override
    protected boolean badpackets_handlePing(int id) {
        return id == Constants.PING_PONG;
    }

}
