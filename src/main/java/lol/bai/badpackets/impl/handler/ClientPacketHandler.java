package lol.bai.badpackets.impl.handler;

import lol.bai.badpackets.api.S2CPacketReceiver;
import lol.bai.badpackets.api.event.PacketSenderReadyCallback;
import lol.bai.badpackets.impl.registry.CallbackRegistry;
import lol.bai.badpackets.impl.registry.ChannelRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;

public class ClientPacketHandler extends AbstractPacketHandler<S2CPacketReceiver> {

    private final Minecraft client;
    private final ClientPacketListener listener;

    public ClientPacketHandler(Minecraft client, ClientPacketListener listener) {
        super("ClientPlayPacketHandler", ChannelRegistry.S2C, ServerboundCustomPayloadPacket::new, listener.getConnection());

        this.client = client;
        this.listener = listener;
    }

    public static ClientPacketHandler get() {
        ClientPacketListener listener = Minecraft.getInstance().getConnection();
        if (listener == null) {
            throw new IllegalStateException("Cannot get c2s sender when not in game!");
        }

        return ((ClientPacketHandler.Holder) listener).badpackets_getHandler();
    }

    @Override
    protected void onInitialChannelSyncPacketReceived() {
        sendInitialChannelSyncPacket();
        for (PacketSenderReadyCallback.Client callback : CallbackRegistry.CLIENT_PLAYER_JOIN) {
            callback.onJoin(listener, this, Minecraft.getInstance());
        }
    }

    @Override
    protected void receive(S2CPacketReceiver receiver, FriendlyByteBuf buf) {
        receiver.receive(client, listener, buf, this);
    }

    public interface Holder {

        ClientPacketHandler badpackets_getHandler();

    }

}
