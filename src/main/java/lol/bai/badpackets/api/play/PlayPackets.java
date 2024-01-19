package lol.bai.badpackets.api.play;

import lol.bai.badpackets.api.PacketSender;
import lol.bai.badpackets.impl.marker.ApiSide;
import lol.bai.badpackets.impl.payload.UntypedPayload;
import lol.bai.badpackets.impl.registry.CallbackRegistry;
import lol.bai.badpackets.impl.registry.ChannelRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;

/**
 * Utility for working with play packets.
 */
public final class PlayPackets {

    /**
     * Register a client-to-server packet channel.
     * <p>
     * Raw packet receiver is run on Netty event-loop. Read the buffer on it and run
     * the operation on {@linkplain MinecraftServer#execute(Runnable) server thread}.
     *
     * @param id       the packet id
     * @param receiver the receiver
     */
    public static void registerServerChannel(ResourceLocation id, ServerPlayPacketReceiver<FriendlyByteBuf> receiver) {
        ChannelRegistry.PLAY_C2S.registerCodec(id, UntypedPayload.codec(id));
        ChannelRegistry.PLAY_C2S.registerReceiver(id, (server, player, handler, payload, responseSender) -> receiver.receive(server, player, handler, ((UntypedPayload) payload).buffer(), responseSender));
    }

    /**
     * Register a client-to-server packet channel.
     * <p>
     * Typed packet receiver is run on the main server thread.
     *
     * @param type     the {@linkplain CustomPacketPayload#type() packet type}
     * @param codec    the payload codec
     * @param receiver the receiver
     */
    @SuppressWarnings("unchecked")
    public static <P extends CustomPacketPayload> void registerServerChannel(CustomPacketPayload.Type<P> type, StreamCodec<? super RegistryFriendlyByteBuf, P> codec, ServerPlayPacketReceiver<P> receiver) {
        ChannelRegistry.PLAY_C2S.registerCodec(type.id(), codec);
        ChannelRegistry.PLAY_C2S.registerReceiver(type.id(), (ServerPlayPacketReceiver<CustomPacketPayload>) receiver);
    }

    /**
     * Register a callback that will be called when player that <b>has Bad Packets</b> joined.
     * <p>
     * This is the first point that {@link PacketSender#canSend} will behave properly.
     * <p>
     * Not a general-purpose player join callback, use platform specific API for that.
     */
    public static void registerServerReadyCallback(ServerPlayPacketReadyCallback callback) {
        CallbackRegistry.SERVER_PLAY.add(callback);
    }

    /**
     * Register a server-to-client packet channel.
     * <p>
     * This method needs to be called on <b>all sides</b>.
     * <p>
     * Register the receiver on <b>client side</b> with {@link #registerClientReceiver(ResourceLocation, ClientPlayPacketReceiver)}
     *
     * @param id the packet id
     */
    public static void registerClientChannel(ResourceLocation id) {
        ChannelRegistry.PLAY_S2C.registerCodec(id, UntypedPayload.codec(id));
    }

    /**
     * Register a server-to-client packet channel.
     * <p>
     * This method needs to be called on <b>all sides</b>.
     * <p>
     * Register the receiver on <b>client side</b> with {@link #registerClientReceiver(CustomPacketPayload.Type, ClientPlayPacketReceiver)}
     *
     * @param type  the {@linkplain CustomPacketPayload#type() packet type}
     * @param codec the payload codec
     */
    public static <P extends CustomPacketPayload> void registerClientChannel(CustomPacketPayload.Type<P> type, StreamCodec<? super RegistryFriendlyByteBuf, P> codec) {
        ChannelRegistry.PLAY_S2C.registerCodec(type.id(), codec);
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
     */
    @ApiSide.ClientOnly
    public static void registerClientReceiver(ResourceLocation id, ClientPlayPacketReceiver<FriendlyByteBuf> receiver) {
        ChannelRegistry.PLAY_S2C.registerReceiver(id, (client, handler, payload, responseSender) -> receiver.receive(client, handler, ((UntypedPayload) payload).buffer(), responseSender));
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
     */
    @ApiSide.ClientOnly
    @SuppressWarnings("unchecked")
    public static <P extends CustomPacketPayload> void registerClientReceiver(CustomPacketPayload.Type<P> type, ClientPlayPacketReceiver<P> receiver) {
        ChannelRegistry.PLAY_S2C.registerReceiver(type.id(), (ClientPlayPacketReceiver<CustomPacketPayload>) receiver);
    }

    /**
     * Register a callback that will be called when the client joined a server that <b>has Bad Packets installed</b>.
     * <p>
     * This is the first point that {@link PacketSender#canSend} will behave properly.
     * <p>
     * Not a general-purpose player join callback, use platform specific API for that.
     */
    @ApiSide.ClientOnly
    public static void registerClientReadyCallback(ClientPlayPacketReadyCallback callback) {
        CallbackRegistry.CLIENT_PLAY.add(callback);
    }

    private PlayPackets() {
    }

}
