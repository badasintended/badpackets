package lol.bai.badpackets.api;

import io.netty.channel.ChannelFutureListener;
import lol.bai.badpackets.api.play.ClientPlayContext;
import lol.bai.badpackets.api.play.PlayPackets;
import lol.bai.badpackets.api.play.ServerPlayContext;
import lol.bai.badpackets.impl.handler.ClientPlayPacketHandler;
import lol.bai.badpackets.impl.handler.ServerPlayPacketHandler;
import lol.bai.badpackets.impl.marker.ApiSide;
import lol.bai.badpackets.impl.payload.UntypedPayload;
import net.minecraft.network.FriendlyByteBuf;
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
     * @see PlayPackets#registerServerChannel(ResourceLocation)
     * @see PlayPackets#registerServerChannel(CustomPacketPayload.Type, StreamCodec)
     */
    @ApiSide.ClientOnly
    static ClientPlayContext c2s() {
        return ClientPlayPacketHandler.get();
    }

    /**
     * Returns a server-to-client <b>play</b> packet sender.
     *
     * @param player the player that we want to send packets to.
     *
     * @see PlayPackets#registerClientChannel(ResourceLocation)
     * @see PlayPackets#registerClientChannel(CustomPacketPayload.Type, StreamCodec)
     */
    static ServerPlayContext s2c(ServerPlayer player) {
        return ServerPlayPacketHandler.get(player);
    }

    /**
     * Returns whether the target can receive a packet with the specified id.
     * <p>
     * <b>Note:</b> Only works for Bad Packets channels.
     *
     * @see PlayPackets#registerClientReadyCallback(PacketReadyCallback)
     * @see PlayPackets#registerServerReadyCallback(PacketReadyCallback)
     */
    boolean canSend(ResourceLocation id);

    /**
     * Returns whether the target can receive a packet with the specified id.
     * <p>
     * <b>Note:</b> Only works for Bad Packets channels.
     *
     * @see PlayPackets#registerClientReadyCallback(PacketReadyCallback)
     * @see PlayPackets#registerServerReadyCallback(PacketReadyCallback)
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
    void send(CustomPacketPayload payload, @Nullable ChannelFutureListener callback);

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
    default void send(ResourceLocation id, FriendlyByteBuf buf, @Nullable ChannelFutureListener callback) {
        send(new UntypedPayload(UntypedPayload.type(id), buf), callback);
    }

}
