package lol.bai.badpackets.impl.mixin;

import lol.bai.badpackets.impl.handler.ServerPacketHandler;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.common.ServerCommonPacketListener;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class MixinServerGamePacketListenerImpl extends MixinServerCommonPacketListenerImpl implements ServerPacketHandler.Holder {

    @Shadow
    public ServerPlayer player;

    @Unique
    private ServerPacketHandler badpacket_packetHandler;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void badpackets_createServerPacketHandler(MinecraftServer minecraftServer, Connection connection, ServerPlayer serverPlayer, int i, CallbackInfo ci) {
        badpacket_packetHandler = new ServerPacketHandler(minecraftServer, (ServerGamePacketListenerImpl) (Object) this, connection);
    }

    @Inject(method = "onDisconnect", at = @At("HEAD"))
    private void badpackets_removeServerPacketHandler(Component reason, CallbackInfo ci) {
        badpacket_packetHandler.onDisconnect();
    }

    @Override
    protected boolean badpackets_handleCustomPayload(ServerboundCustomPayloadPacket packet) {
        PacketUtils.ensureRunningOnSameThread(packet, (ServerCommonPacketListener) this, this.player.serverLevel());
        return badpacket_packetHandler.receive(packet.payload());
    }

    @Override
    public ServerPacketHandler badpackets_getHandler() {
        return badpacket_packetHandler;
    }

}
