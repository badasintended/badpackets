package lol.bai.badpackets.impl.mixin.client;

import lol.bai.badpackets.impl.handler.ClientPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class MixinClientPacketListener extends MixinClientCommonPacketListenerImpl implements ClientPacketHandler.Holder {

    @Unique
    private ClientPacketHandler badpacket_packetHandler;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void badpackets_createClientPacketHandler(Minecraft $$0, Connection $$1, CommonListenerCookie $$2, CallbackInfo ci) {
        badpacket_packetHandler = new ClientPacketHandler(minecraft, (ClientPacketListener) (Object) this);
    }

    @Override
    protected void badpackets_removeClientPacketHandler(Component reason) {
        badpacket_packetHandler.onDisconnect();
    }

    @Inject(method = "handleCustomPayload", at = @At("HEAD"), cancellable = true)
    private void badpackets_receiveS2CPacket(CustomPacketPayload payload, CallbackInfo ci) {
        badpacket_packetHandler.sendInitialChannelSyncPacket();
        if (badpacket_packetHandler.receive(payload)) ci.cancel();
    }

    @Override
    public ClientPacketHandler badpackets_getHandler() {
        return badpacket_packetHandler;
    }

}
