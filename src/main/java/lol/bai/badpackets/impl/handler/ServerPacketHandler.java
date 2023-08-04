package lol.bai.badpackets.impl.handler;

import lol.bai.badpackets.api.PacketReceiver;
import lol.bai.badpackets.api.event.PacketSenderReadyCallback;
import lol.bai.badpackets.impl.registry.CallbackRegistry;
import lol.bai.badpackets.impl.registry.ChannelRegistry;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class ServerPacketHandler extends AbstractPacketHandler<PacketReceiver.C2S<CustomPacketPayload>> {

    private final MinecraftServer server;
    private final ServerGamePacketListenerImpl handler;

    public ServerPacketHandler(MinecraftServer server, ServerGamePacketListenerImpl handler, Connection connection) {
        super("ServerPlayPacketHandler for " + handler.getPlayer().getScoreboardName(), ChannelRegistry.C2S, ClientboundCustomPayloadPacket::new, connection);
        this.server = server;
        this.handler = handler;
    }

    public static ServerPacketHandler get(ServerPlayer player) {
        return ((ServerPacketHandler.Holder) player.connection).badpackets_getHandler();
    }

    @Override
    protected void onInitialChannelSyncPacketReceived() {
        for (PacketSenderReadyCallback.Server callback : CallbackRegistry.SERVER_PLAYER_JOIN) {
            callback.onJoin(handler, this, server);
        }
    }

    @Override
    protected void receive(PacketReceiver.C2S<CustomPacketPayload> receiver, CustomPacketPayload payload) {
        receiver.receive(server, handler.getPlayer(), handler, payload, this);
    }

    public interface Holder {

        ServerPacketHandler badpackets_getHandler();

    }

}
