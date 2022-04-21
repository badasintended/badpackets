package lol.bai.badpackets.impl.handler;

import lol.bai.badpackets.api.C2SPacketReceiver;
import lol.bai.badpackets.api.event.PacketSenderReadyCallback;
import lol.bai.badpackets.impl.registry.CallbackRegistry;
import lol.bai.badpackets.impl.registry.ChannelRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class ServerPacketHandler extends AbstractPacketHandler<C2SPacketReceiver> {

    private final MinecraftServer server;
    private final ServerGamePacketListenerImpl handler;

    public ServerPacketHandler(MinecraftServer server, ServerGamePacketListenerImpl handler) {
        super("ServerPlayPacketHandler for " + handler.getPlayer().getScoreboardName(), ChannelRegistry.C2S, ClientboundCustomPayloadPacket::new, handler.getConnection());
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
    protected void receive(C2SPacketReceiver receiver, FriendlyByteBuf buf) {
        receiver.receive(server, handler.getPlayer(), handler, buf, this);
    }

    public interface Holder {

        ServerPacketHandler badpackets_getHandler();

    }

}
