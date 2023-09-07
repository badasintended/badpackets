package lol.bai.badpackets.api.config;

import lol.bai.badpackets.api.PacketSender;
import lol.bai.badpackets.impl.handler.ServerConfigPacketHandler;
import lol.bai.badpackets.impl.marker.ApiSide;
import lol.bai.badpackets.impl.mixin.AccessServerboundCustomPayloadPacket;
import lol.bai.badpackets.impl.mixin.client.AccessClientboundCustomPayloadPacket;
import lol.bai.badpackets.impl.payload.UntypedPayload;
import lol.bai.badpackets.impl.registry.CallbackRegistry;
import lol.bai.badpackets.impl.registry.ChannelRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * Utility for working with configuration packets.
 */
public final class ConfigPackets {

    /**
     * Register a configuration task.
     *
     * @param id       the task id
     * @param executor the task executor
     *
     * @see ConfigTaskExecutor#runTask
     */
    public static void registerTask(ResourceLocation id, ConfigTaskExecutor executor) {
        ServerConfigPacketHandler.registerTask(id, executor);
    }

    /**
     * Register a client-to-server packet receiver.
     *
     * @param id       the packet id
     * @param receiver the receiver
     *
     * @see ServerConfigPacketReceiver#receive
     */
    public static void registerServerReceiver(ResourceLocation id, ServerConfigPacketReceiver<FriendlyByteBuf> receiver) {
        registerServerReceiver(id, UntypedPayload.reader(id), (server, handler, payload, responseSender, taskFinisher) ->
            receiver.receive(server, handler, payload.buffer(), responseSender, taskFinisher));
    }

    /**
     * Register a client-to-server packet receiver.
     *
     * @param id       the {@linkplain CustomPacketPayload#id() packet id}
     * @param reader   the payload reader
     * @param receiver the receiver
     *
     * @see ServerConfigPacketReceiver#receive
     */
    @SuppressWarnings("unchecked")
    public static <P extends CustomPacketPayload> void registerServerReceiver(ResourceLocation id, FriendlyByteBuf.Reader<P> reader, ServerConfigPacketReceiver<P> receiver) {
        AccessServerboundCustomPayloadPacket.badpackets_getPacketReaders().put(id, reader);
        ChannelRegistry.CONFIG_C2S.register(id, (ServerConfigPacketReceiver<CustomPacketPayload>) receiver);
    }

    /**
     * Register a callback that will be called when player that <b>has Bad Packets</b> joined.
     * <p>
     * This is the first point that {@link PacketSender#canSend} will behave properly.
     * <p>
     * Not a general-purpose player join callback, use platform specific API for that.
     */
    @ApiSide.ServerOnly
    public static void registerServerReadyCallback(ServerConfigPacketReadyCallback callback) {
        CallbackRegistry.SERVER_READY_CONFIG.add(callback);
    }

    /**
     * Register a server-to-client packet receiver.
     *
     * @param id       the packet id
     * @param receiver the receiver
     *
     * @see ClientConfigPacketReceiver#receive
     */
    @ApiSide.ClientOnly
    public static void registerClientReceiver(ResourceLocation id, ClientConfigPacketReceiver<FriendlyByteBuf> receiver) {
        registerClientReceiver(id, UntypedPayload.reader(id), (client, handler, payload, responseSender) ->
            receiver.receive(client, handler, payload.buffer(), responseSender));
    }

    /**
     * Register a server-to-client packet receiver.
     *
     * @param id       the {@linkplain CustomPacketPayload#id() packet id}
     * @param reader   the payload reader
     * @param receiver the receiver
     *
     * @see ClientConfigPacketReceiver#receive
     */
    @ApiSide.ClientOnly
    @SuppressWarnings("unchecked")
    public static <P extends CustomPacketPayload> void registerClientReceiver(ResourceLocation id, FriendlyByteBuf.Reader<P> reader, ClientConfigPacketReceiver<P> receiver) {
        AccessClientboundCustomPayloadPacket.badpackets_getPacketReaders().put(id, reader);
        ChannelRegistry.CONFIG_S2C.register(id, (ClientConfigPacketReceiver<CustomPacketPayload>) receiver);
    }

    /**
     * Register a callback that will be called when the client joined a server that <b>has Bad Packets installed</b>.
     * <p>
     * This is the first point that {@link PacketSender#canSend} will behave properly.
     * <p>
     * Not a general-purpose player join callback, use platform specific API for that.
     */
    @ApiSide.ClientOnly
    public static void registerClientReadyCallback(ClientConfigPacketReadyCallback callback) {
        CallbackRegistry.CLIENT_READY_CONFIG.add(callback);
    }

    private ConfigPackets() {
    }

}
