package lol.bai.badpackets.api.play;

import lol.bai.badpackets.api.PacketSender;
import lol.bai.badpackets.impl.marker.ApiSide;
import lol.bai.badpackets.impl.mixin.AccessServerboundCustomPayloadPacket;
import lol.bai.badpackets.impl.mixin.client.AccessClientboundCustomPayloadPacket;
import lol.bai.badpackets.impl.payload.UntypedPayload;
import lol.bai.badpackets.impl.registry.CallbackRegistry;
import lol.bai.badpackets.impl.registry.ChannelRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

/**
 * Utility for working with play packets.
 */
public final class PlayPackets {

    /**
     * Register a client-to-server packet receiver.
     *
     * @param id       the packet id
     * @param receiver the receiver
     */
    public static void registerServerReceiver(ResourceLocation id, ServerReceiver<FriendlyByteBuf> receiver) {
        registerServerReceiver(id, UntypedPayload.reader(id), (server, player, handler, payload, responseSender) ->
            receiver.receive(server, player, handler, payload.buffer(), responseSender));
    }

    /**
     * Register a client-to-server packet receiver.
     *
     * @param id       the {@linkplain CustomPacketPayload#id() packet id}
     * @param reader   the payload reader
     * @param receiver the receiver
     */
    @SuppressWarnings("unchecked")
    public static <P extends CustomPacketPayload> void registerServerReceiver(ResourceLocation id, FriendlyByteBuf.Reader<P> reader, ServerReceiver<P> receiver) {
        AccessServerboundCustomPayloadPacket.badpackets_getPacketReaders().put(id, reader);
        ChannelRegistry.PLAY_C2S.register(id, (ServerReceiver<CustomPacketPayload>) receiver);
    }

    /**
     * Register a callback that will be called when player that <b>has Bad Packets</b> joined.
     * <p>
     * This is the first point that {@link PacketSender#canSend} will behave properly.
     * <p>
     * Not a general-purpose player join callback, use platform specific API for that.
     */
    public static void registerServerReadyCallback(ServerReadyCallback callback) {
        CallbackRegistry.SERVER_PLAY.add(callback);
    }

    /**
     * Register a server-to-client packet receiver.
     *
     * @param id       the packet id
     * @param receiver the receiver
     */
    @ApiSide.ClientOnly
    public static void registerClientReceiver(ResourceLocation id, ClientReceiver<FriendlyByteBuf> receiver) {
        registerClientReceiver(id, UntypedPayload.reader(id), (client, handler, payload, responseSender) ->
            receiver.receive(client, handler, payload.buffer(), responseSender));
    }

    /**
     * Register a server-to-client packet receiver.
     *
     * @param id       the {@linkplain CustomPacketPayload#id() packet id}
     * @param reader   the payload reader
     * @param receiver the receiver
     */
    @ApiSide.ClientOnly
    @SuppressWarnings("unchecked")
    public static <P extends CustomPacketPayload> void registerClientReceiver(ResourceLocation id, FriendlyByteBuf.Reader<P> reader, ClientReceiver<P> receiver) {
        AccessClientboundCustomPayloadPacket.badpackets_getPacketReaders().put(id, reader);
        ChannelRegistry.PLAY_S2C.register(id, (ClientReceiver<CustomPacketPayload>) receiver);
    }

    /**
     * Register a callback that will be called when the client joined a server that <b>has Bad Packets installed</b>.
     * <p>
     * This is the first point that {@link PacketSender#canSend} will behave properly.
     * <p>
     * Not a general-purpose player join callback, use platform specific API for that.
     */
    @ApiSide.ClientOnly
    public static void registerClientReadyCallback(ClientReadyCallback callback) {
        CallbackRegistry.CLIENT_PLAY.add(callback);
    }

    @FunctionalInterface
    public interface ServerReceiver<P> {

        void receive(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, P payload, PacketSender responseSender);

    }

    @FunctionalInterface
    public interface ServerReadyCallback {

        void onReady(ServerGamePacketListenerImpl handler, PacketSender sender, MinecraftServer server);

    }

    @ApiSide.ClientOnly
    @FunctionalInterface
    public interface ClientReceiver<P> {

        void receive(Minecraft client, ClientPacketListener handler, P payload, PacketSender responseSender);

    }

    @ApiSide.ClientOnly
    @FunctionalInterface
    public interface ClientReadyCallback {

        void onReady(ClientPacketListener handler, PacketSender sender, Minecraft client);

    }

}
