package lol.bai.badpackets.api;

import lol.bai.badpackets.api.play.ClientPlayPacketReadyCallback;
import lol.bai.badpackets.api.play.ClientPlayPacketReceiver;
import lol.bai.badpackets.api.play.PlayPackets;
import lol.bai.badpackets.api.play.ServerPlayPacketReadyCallback;
import lol.bai.badpackets.api.play.ServerPlayPacketReceiver;
import lol.bai.badpackets.impl.handler.ClientPlayPacketHandler;
import lol.bai.badpackets.impl.handler.ServerPlayPacketHandler;
import lol.bai.badpackets.impl.marker.ApiSide;
import lol.bai.badpackets.impl.payload.UntypedPayload;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.NonExtendable
public interface PacketSender {

    /**
     * Returns client-to-server <b>play</b> packet sender.
     * <p>
     * <b>Only available when on game.</b>
     *
     * @see PlayPackets#registerServerReceiver(ResourceLocation, ServerPlayPacketReceiver)
     * @see PlayPackets#registerServerReceiver(CustomPacketPayload.Type, StreamCodec, ServerPlayPacketReceiver)
     */
    @ApiSide.ClientOnly
    static PacketSender c2s() {
        return ClientPlayPacketHandler.get();
    }

    /**
     * Returns a server-to-client <b>play</b> packet sender.
     *
     * @param player the player that we want to send packets to.
     *
     * @see PlayPackets#registerClientReceiver(ResourceLocation, ClientPlayPacketReceiver)
     * @see PlayPackets#registerClientReceiver(CustomPacketPayload.Type, StreamCodec, ClientPlayPacketReceiver)
     */
    @ApiSide.ServerOnly
    static PacketSender s2c(ServerPlayer player) {
        return ServerPlayPacketHandler.get(player);
    }

    /**
     * Returns whether the target can receive a packet with the specified id.
     * <p>
     * <b>Note:</b> Only works for Bad Packets channels.
     *
     * @see PlayPackets#registerClientReadyCallback(ClientPlayPacketReadyCallback)
     * @see PlayPackets#registerServerReadyCallback(ServerPlayPacketReadyCallback)
     */
    boolean canSend(ResourceLocation id);

    /**
     * Returns whether the target can receive a packet with the specified id.
     * <p>
     * <b>Note:</b> Only works for Bad Packets channels.
     *
     * @see PlayPackets#registerClientReadyCallback(ClientPlayPacketReadyCallback)
     * @see PlayPackets#registerServerReadyCallback(ServerPlayPacketReadyCallback)
     */
    default boolean canSend(CustomPacketPayload.Type<?> type) {
        return canSend(type.id());
    }

    /**
     * Send a packet to the target.
     */
    default void send(CustomPacketPayload payload) {
        send(payload, null);
    }

    /**
     * Send a packet to the target.
     *
     * @param callback a callback in which will be called after the packet sent to the target.
     */
    void send(CustomPacketPayload payload, @Nullable PacketSendListener callback);

    /**
     * Send a packet to the target.
     */
    default void send(ResourceLocation id, FriendlyByteBuf buf) {
        send(id, buf, null);
    }

    /**
     * Send a packet to the target.
     *
     * @param callback a callback in which will be called after the packet sent to the target.
     */
    default void send(ResourceLocation id, FriendlyByteBuf buf, @Nullable PacketSendListener callback) {
        send(new UntypedPayload(UntypedPayload.type(id), buf), callback);
    }

}
