package lol.bai.badpackets.impl.mixin.client;

import com.mojang.authlib.GameProfile;
import lol.bai.badpackets.impl.handler.ClientPacketHandler;
import net.minecraft.client.ClientTelemetryManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class MixinClientPacketListener implements ClientPacketHandler.Holder {

    @Unique
    private ClientPacketHandler badpacket_packetHandler;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void badpackets_createClientPacketHandler(Minecraft minecraft, Screen screen, Connection connection, GameProfile gameProfile, ClientTelemetryManager clientTelemetryManager, CallbackInfo ci) {
        badpacket_packetHandler = new ClientPacketHandler((ClientPacketListener) (Object) this);
    }

    @Inject(method = "onDisconnect", at = @At("HEAD"))
    private void badpackets_removeClientPacketHandler(Component reason, CallbackInfo ci) {
        badpacket_packetHandler.onDisconnect();
    }

    @Inject(method = "handleLogin", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/Connection;send(Lnet/minecraft/network/protocol/Packet;)V"))
    private void badpackets_initClientPacketHandler(ClientboundLoginPacket packet, CallbackInfo ci) {
        badpacket_packetHandler.sendInitialChannelSyncPacket();
    }

    @Inject(method = "handleCustomPayload", at = @At("HEAD"), cancellable = true)
    private void badpackets_receiveS2CPacket(ClientboundCustomPayloadPacket packet, CallbackInfo ci) {
        if (badpacket_packetHandler.receive(packet.getIdentifier(), packet.getData())) {
            ci.cancel();
        }
    }

    @Override
    public ClientPacketHandler badpackets_getHandler() {
        return badpacket_packetHandler;
    }

}
