package lol.bai.badpackets.api;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lol.bai.badpackets.api.event.PacketSenderReadyCallback;
import lol.bai.badpackets.impl.handler.ClientPacketHandler;
import lol.bai.badpackets.impl.handler.ServerPacketHandler;
import lol.bai.badpackets.impl.marker.ApiSide;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketSendListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.NonExtendable
public interface PacketSender {

    /**
     * Returns client-to-server packet sender.
     * <p>
     * <b>Only available when on game.</b>
     *
     * @see PacketSenderReadyCallback#registerClient(PacketSenderReadyCallback.Client)
     */
    @ApiSide.ClientOnly
    static PacketSender c2s() {
        return ClientPacketHandler.get();
    }

    /**
     * Returns a server-to-client packet sender.
     *
     * @param player the player that we want to send packets to.
     *
     * @see PacketSenderReadyCallback#registerServer(PacketSenderReadyCallback.Server)
     */
    @ApiSide.ServerOnly
    static PacketSender s2c(ServerPlayer player) {
        return ServerPacketHandler.get(player);
    }

    /**
     * Returns whether the target can receive a packet with the specified id.
     * <p>
     * <b>Note:</b> Only works for Bad Packets channels.
     */
    boolean canSend(ResourceLocation id);

    /**
     * Send a packet to the target.
     */
    default void send(ResourceLocation id, FriendlyByteBuf buf) {
        send(id, buf, (PacketSendListener) null);
    }

    /**
     * Send a packet to the target.
     *
     * @param callback a callback in which will be called after the packet sent to the target.
     */
    void send(ResourceLocation id, FriendlyByteBuf buf, @Nullable PacketSendListener callback);

    /**
     * Send a packet to the target.
     *
     * @param callback a callback in which will be called after the packet sent to the target.
     */
    @Deprecated
    void send(ResourceLocation id, FriendlyByteBuf buf, @Nullable GenericFutureListener<? extends Future<? super Void>> callback);

}
