package lol.bai.badpackets.impl.mixin;

import lol.bai.badpackets.impl.handler.ServerPacketHandler;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class MixinServerGamePacketListenerImpl implements ServerPacketHandler.Holder {

    @Shadow
    @Final
    private MinecraftServer server;

    @Unique
    private ServerPacketHandler badpacket_packetHandler;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void badpackets_createServerPacketHandler(MinecraftServer minecraftServer, Connection connection, ServerPlayer serverPlayer, CallbackInfo ci) {
        badpacket_packetHandler = new ServerPacketHandler(minecraftServer, (ServerGamePacketListenerImpl) (Object) this);
    }

    @Inject(method = "onDisconnect", at = @At("HEAD"))
    private void badpackets_removeServerPacketHandler(Component reason, CallbackInfo ci) {
        badpacket_packetHandler.onDisconnect();
    }

    @Inject(method = "handleCustomPayload", at = @At("HEAD"), cancellable = true)
    private void badpackets_handleCustomPayload(ServerboundCustomPayloadPacket packet, CallbackInfo ci) {
        if (!server.isSameThread() && badpacket_packetHandler.receive(packet.getIdentifier(), packet.getData())) {
            ci.cancel();
        }
    }

    @Override
    public ServerPacketHandler badpackets_getHandler() {
        return badpacket_packetHandler;
    }


}

