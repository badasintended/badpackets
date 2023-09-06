package lol.bai.badpackets.impl.mixin;

import lol.bai.badpackets.impl.handler.ServerPlayPacketHandler;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.common.ServerCommonPacketListener;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ServerboundConfigurationAcknowledgedPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class MixinServerGamePacketListenerImpl extends MixinServerCommonPacketListenerImpl implements ServerPlayPacketHandler.Holder {

    @Unique
    private ServerPlayPacketHandler badpacket_packetHandler;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void badpackets_createServerPacketHandler(MinecraftServer minecraftServer, Connection connection, ServerPlayer serverPlayer, CommonListenerCookie cookie, CallbackInfo ci) {
        badpacket_packetHandler = new ServerPlayPacketHandler(minecraftServer, (ServerGamePacketListenerImpl) (Object) this, connection);
    }

    @Inject(method = "onDisconnect", at = @At("HEAD"))
    private void badpackets_removeServerPacketHandler(Component reason, CallbackInfo ci) {
        badpacket_packetHandler.remove();
    }

    @Inject(method = "handleConfigurationAcknowledged", at = @At("RETURN"))
    private void badpacekts_removePacketHandler(ServerboundConfigurationAcknowledgedPacket packet, CallbackInfo ci) {
        badpacket_packetHandler.remove();
    }

    @Override
    protected boolean badpackets_handleCustomPayload(ServerboundCustomPayloadPacket packet) {
        PacketUtils.ensureRunningOnSameThread(packet, (ServerCommonPacketListener) this, server);
        return badpacket_packetHandler.receive(packet.payload());
    }

    @Override
    public ServerPlayPacketHandler badpackets_getHandler() {
        return badpacket_packetHandler;
    }

}
