package lol.bai.badpackets.api.config;

import lol.bai.badpackets.api.PacketSender;
import lol.bai.badpackets.impl.handler.ServerConfigPacketHandler;
import lol.bai.badpackets.impl.marker.ApiSide;
import lol.bai.badpackets.impl.mixin.client.AccessClientCommonPacketListenerImpl;
import lol.bai.badpackets.impl.payload.UntypedPayload;
import lol.bai.badpackets.impl.registry.CallbackRegistry;
import lol.bai.badpackets.impl.registry.ChannelRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientConfigurationPacketListenerImpl;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;

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
     * <p>
     * Raw packet receiver is run on Netty event-loop. Read the buffer on it and run
     * the operation on {@linkplain MinecraftServer#execute(Runnable) server thread}.
     *
     * @param id       the packet id
     * @param receiver the receiver
     *
     * @see ServerConfigPacketReceiver#receive
     */
    public static void registerServerChannel(ResourceLocation id, ServerConfigPacketReceiver<FriendlyByteBuf> receiver) {
        ChannelRegistry.CONFIG_C2S.registerCodec(id, UntypedPayload.codec(id));
        ChannelRegistry.CONFIG_C2S.registerReceiver(id, (context, payload) -> receiver.receive(context, ((UntypedPayload) payload).buffer()));
    }

    /**
     * Register a client-to-server packet receiver.
     * <p>
     * Typed packet receiver is run on the main server thread.
     *
     * @param type     the {@linkplain CustomPacketPayload#type() packet type}
     * @param codec    the payload codec
     * @param receiver the receiver
     *
     * @see ServerConfigPacketReceiver#receive
     */
    @SuppressWarnings("unchecked")
    public static <P extends CustomPacketPayload> void registerServerChannel(CustomPacketPayload.Type<P> type, StreamCodec<? super FriendlyByteBuf, P> codec, ServerConfigPacketReceiver<P> receiver) {
        ChannelRegistry.CONFIG_C2S.registerCodec(type.id(), codec);
        ChannelRegistry.CONFIG_C2S.registerReceiver(type.id(), (ServerConfigPacketReceiver<CustomPacketPayload>) receiver);
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
     * <p>
     * This method needs to be called on <b>all sides</b>.
     * <p>
     * Register the receiver on <b>client side</b> with {@link #registerClientReceiver(ResourceLocation, ClientConfigPacketReceiver)}
     *
     * @param id the packet id
     */
    public static void registerClientChannel(ResourceLocation id) {
        ChannelRegistry.CONFIG_S2C.registerCodec(id, UntypedPayload.codec(id));
    }

    /**
     * Register a server-to-client packet receiver.
     * <p>
     * This method needs to be called on <b>all sides</b>.
     * <p>
     * Register the receiver on <b>client side</b> with {@link #registerClientReceiver(CustomPacketPayload.Type, ClientConfigPacketReceiver)}
     *
     * @param type  the {@linkplain CustomPacketPayload#type() packet type}
     * @param codec the payload codec
     */
    public static <P extends CustomPacketPayload> void registerClientChannel(CustomPacketPayload.Type<P> type, StreamCodec<? super FriendlyByteBuf, P> codec) {
        ChannelRegistry.CONFIG_S2C.registerCodec(type.id(), codec);
    }

    /**
     * Register a server-to-client packet receiver.
     * <p>
     * The channel needs to be {@linkplain #registerClientChannel(ResourceLocation) registered} first.
     * <p>
     * Raw packet receiver is run on Netty event-loop. Read the buffer on it and run
     * the operation on {@linkplain Minecraft#execute(Runnable) client thread}.
     *
     * @param id       the packet id
     * @param receiver the receiver
     *
     * @see ClientConfigPacketReceiver#receive
     * @see #disconnect
     */
    @ApiSide.ClientOnly
    public static void registerClientReceiver(ResourceLocation id, ClientConfigPacketReceiver<FriendlyByteBuf> receiver) {
        ChannelRegistry.CONFIG_S2C.registerReceiver(id, (context, payload) -> receiver.receive(context, ((UntypedPayload) payload).buffer()));
    }

    /**
     * Register a server-to-client packet receiver.
     * <p>
     * The channel needs to be {@linkplain #registerClientChannel(CustomPacketPayload.Type, StreamCodec) registered} first.
     * <p>
     * Typed packet receiver is run on the main client thread.
     *
     * @param type     the {@linkplain CustomPacketPayload#type() packet type}
     * @param receiver the receiver
     *
     * @see ClientConfigPacketReceiver#receive
     * @see #disconnect
     */
    @ApiSide.ClientOnly
    @SuppressWarnings("unchecked")
    public static <P extends CustomPacketPayload> void registerClientReceiver(CustomPacketPayload.Type<P> type, ClientConfigPacketReceiver<P> receiver) {
        ChannelRegistry.CONFIG_S2C.registerReceiver(type.id(), (ClientConfigPacketReceiver<CustomPacketPayload>) receiver);
    }

    /**
     * Register a callback that will be called when the client joined a server that <b>has Bad Packets installed</b>.
     * <p>
     * This is the first point that {@link PacketSender#canSend} will behave properly.
     * <p>
     * Not a general-purpose player join callback, use platform specific API for that.
     *
     * @see #disconnect
     */
    @ApiSide.ClientOnly
    public static void registerClientReadyCallback(ClientConfigPacketReadyCallback callback) {
        CallbackRegistry.CLIENT_READY_CONFIG.add(callback);
    }

    /**
     * Helper method to disconnect client-to-server connection.
     *
     * @param handler the handler instance
     * @param reason   the disconnection reason
     */
    @ApiSide.ClientOnly
    public static void disconnect(ClientConfigurationPacketListenerImpl handler, Component reason) {
        ((AccessClientCommonPacketListenerImpl) handler).badpackets_connection().disconnect(reason);
    }

    private ConfigPackets() {
    }

}
