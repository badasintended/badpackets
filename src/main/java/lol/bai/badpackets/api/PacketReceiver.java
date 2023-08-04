package lol.bai.badpackets.api;

import lol.bai.badpackets.impl.marker.ApiSide;
import lol.bai.badpackets.impl.mixin.AccessServerboundCustomPayloadPacket;
import lol.bai.badpackets.impl.mixin.client.AccessClientboundCustomPayloadPacket;
import lol.bai.badpackets.impl.payload.UntypedPayload;
import lol.bai.badpackets.impl.registry.ChannelRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public final class PacketReceiver {

    public static void registerC2S(ResourceLocation id, C2S<FriendlyByteBuf> receiver) {
        registerC2S(id, buf -> new UntypedPayload(id, buf), (server, player, handler, payload, responseSender) ->
            receiver.receive(server, player, handler, payload.buffer(), responseSender));
    }

    @SuppressWarnings("unchecked")
    public static <P extends CustomPacketPayload> void registerC2S(ResourceLocation id, FriendlyByteBuf.Reader<P> reader, C2S<P> receiver) {
        AccessServerboundCustomPayloadPacket.badpackets_getPacketReaders().put(id, reader);
        ChannelRegistry.C2S.register(id, (C2S<CustomPacketPayload>) receiver);
    }

    @ApiSide.ClientOnly
    public static void registerS2C(ResourceLocation id, S2C<FriendlyByteBuf> receiver) {
        registerS2C(id, buf -> new UntypedPayload(id, buf), (client, handler, payload, responseSender) ->
            receiver.receive(client, handler, payload.buffer(), responseSender));
    }

    @ApiSide.ClientOnly
    @SuppressWarnings("unchecked")
    public static <P extends CustomPacketPayload> void registerS2C(ResourceLocation id, FriendlyByteBuf.Reader<P> reader, S2C<P> receiver) {
        AccessClientboundCustomPayloadPacket.badpackets_getPacketReaders().put(id, reader);
        ChannelRegistry.S2C.register(id, (S2C<CustomPacketPayload>) receiver);
    }

    public interface C2S<P> {

        void receive(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, P payload, PacketSender responseSender);

    }

    @ApiSide.ClientOnly
    public interface S2C<P> {

        void receive(Minecraft client, ClientPacketListener handler, P payload, PacketSender responseSender);

    }

}
