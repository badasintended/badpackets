package lol.bai.badpackets.api.config;

import lol.bai.badpackets.api.PacketSender;
import lol.bai.badpackets.impl.marker.ApiSide;
import lol.bai.badpackets.impl.mixin.AccessServerboundCustomPayloadPacket;
import lol.bai.badpackets.impl.mixin.client.AccessClientboundCustomPayloadPacket;
import lol.bai.badpackets.impl.payload.UntypedPayload;
import lol.bai.badpackets.impl.registry.CallbackRegistry;
import lol.bai.badpackets.impl.registry.ChannelRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientConfigurationPacketListenerImpl;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;

/**
 * Utility for working with configuration packets.
 */
public final class ConfigPackets {

    /**
     * Register a client-to-server packet receiver.
     *
     * @param id       the packet id
     * @param receiver the receiver
     */
    public static void registerServerReceiver(ResourceLocation id, ServerReceiver<FriendlyByteBuf> receiver) {
        registerServerReceiver(id, UntypedPayload.reader(id), (server, handler, payload, responseSender) ->
            receiver.receive(server, handler, payload.buffer(), responseSender));
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
        ChannelRegistry.CONFIG_C2S.register(id, (ServerReceiver<CustomPacketPayload>) receiver);
    }

    /**
     * Register a callback that will be called when player that <b>has Bad Packets</b> joined.
     * <p>
     * This is the first point that {@link PacketSender#canSend} will behave properly.
     * <p>
     * Not a general-purpose player join callback, use platform specific API for that.
     */
    @ApiSide.ServerOnly
    public static void registerServerReadyCallback(ServerReadyCallback callback) {
        CallbackRegistry.SERVER_READY_CONFIG.add(callback);
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
        ChannelRegistry.CONFIG_S2C.register(id, (ClientReceiver<CustomPacketPayload>) receiver);
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
        CallbackRegistry.CLIENT_READY_CONFIG.add(callback);
    }

    public interface ServerReceiver<P> {

        void receive(MinecraftServer server, ServerConfigurationPacketListenerImpl handler, P payload, PacketSender responseSender);

    }

    @ApiSide.ClientOnly
    public interface ClientReceiver<P> {

        void receive(Minecraft client, ClientConfigurationPacketListenerImpl handler, P payload, PacketSender responseSender);

    }

    @ApiSide.ServerOnly
    @FunctionalInterface
    public interface ServerReadyCallback {

        void onConfig(ServerConfigurationPacketListenerImpl handler, PacketSender sender, MinecraftServer server);

    }

    @ApiSide.ClientOnly
    @FunctionalInterface
    public interface ClientReadyCallback {

        void onConfig(ClientConfigurationPacketListenerImpl handler, PacketSender sender, Minecraft client);

    }

}
