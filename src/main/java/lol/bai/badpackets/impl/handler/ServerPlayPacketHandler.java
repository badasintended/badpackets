package lol.bai.badpackets.impl.handler;

import lol.bai.badpackets.api.play.ServerPlayPacketReadyCallback;
import lol.bai.badpackets.api.play.ServerPlayPacketReceiver;
import lol.bai.badpackets.impl.platform.PlatformProxy;
import lol.bai.badpackets.impl.registry.CallbackRegistry;
import lol.bai.badpackets.impl.registry.ChannelRegistry;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class ServerPlayPacketHandler extends AbstractPacketHandler<ServerPlayPacketReceiver<CustomPacketPayload>> {

    private final MinecraftServer server;
    private final ServerGamePacketListenerImpl handler;

    public ServerPlayPacketHandler(MinecraftServer server, ServerGamePacketListenerImpl handler, Connection connection) {
        super("ServerPlayPacketHandler for " + handler.getPlayer().getScoreboardName(), ChannelRegistry.PLAY_C2S, ClientboundCustomPayloadPacket::new, server, connection);
        this.server = server;
        this.handler = handler;
    }

    public static ServerPlayPacketHandler get(ServerPlayer player) {
        return ((ServerPlayPacketHandler.Holder) player.connection).badpackets_getHandler();
    }

    @Override
    protected Packet<?> createVanillaRegisterPacket(FriendlyByteBuf buf) {
        return PlatformProxy.INSTANCE.createVanillaRegisterPlayS2CPacket(buf);
    }

    @Override
    protected void onInitialChannelSyncPacketReceived() {
        for (ServerPlayPacketReadyCallback callback : CallbackRegistry.SERVER_PLAY) {
            callback.onReady(handler, this, server);
        }
    }

    @Override
    protected void receiveUnsafe(ServerPlayPacketReceiver<CustomPacketPayload> receiver, CustomPacketPayload payload) {
        receiver.receive(server, handler.getPlayer(), handler, payload, this);
    }

    public interface Holder {

        ServerPlayPacketHandler badpackets_getHandler();

    }

}
