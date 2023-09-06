package lol.bai.badpackets.impl.handler;

import lol.bai.badpackets.api.play.PlayPackets;
import lol.bai.badpackets.impl.registry.CallbackRegistry;
import lol.bai.badpackets.impl.registry.ChannelRegistry;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class ServerPlayPacketHandler extends AbstractPacketHandler<PlayPackets.ServerReceiver<CustomPacketPayload>> {

    private final MinecraftServer server;
    private final ServerGamePacketListenerImpl handler;

    public ServerPlayPacketHandler(MinecraftServer server, ServerGamePacketListenerImpl handler, Connection connection) {
        super("ServerPlayPacketHandler for " + handler.getPlayer().getScoreboardName(), ChannelRegistry.PLAY_C2S, ClientboundCustomPayloadPacket::new, connection);
        this.server = server;
        this.handler = handler;
    }

    public static ServerPlayPacketHandler get(ServerPlayer player) {
        return ((ServerPlayPacketHandler.Holder) player.connection).badpackets_getHandler();
    }

    @Override
    protected void onInitialChannelSyncPacketReceived() {
        for (PlayPackets.ServerReadyCallback callback : CallbackRegistry.SERVER_PLAY) {
            callback.onReady(handler, this, server);
        }
    }

    @Override
    protected void receive(PlayPackets.ServerReceiver<CustomPacketPayload> receiver, CustomPacketPayload payload) {
        receiver.receive(server, handler.getPlayer(), handler, payload, this);
    }

    public interface Holder {

        ServerPlayPacketHandler badpackets_getHandler();

    }

}
