package lol.bai.badpackets.impl.handler;

import java.util.Set;
import java.util.function.Supplier;

import lol.bai.badpackets.api.play.ClientPlayPacketReadyCallback;
import lol.bai.badpackets.api.play.ClientPlayPacketReceiver;
import lol.bai.badpackets.impl.platform.PlatformProxy;
import lol.bai.badpackets.impl.registry.CallbackRegistry;
import lol.bai.badpackets.impl.registry.ChannelRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public class ClientPlayPacketHandler extends AbstractPacketHandler<ClientPlayPacketReceiver<CustomPacketPayload>> {

    private final Minecraft client;
    private final ClientPacketListener listener;

    public ClientPlayPacketHandler(Minecraft client, ClientPacketListener listener) {
        super("ClientPlayPacketHandler", ChannelRegistry.PLAY_S2C, ServerboundCustomPayloadPacket::new, client, listener.getConnection());

        this.client = client;
        this.listener = listener;
    }

    public static ClientPlayPacketHandler get() {
        ClientPacketListener listener = Minecraft.getInstance().getConnection();
        if (listener == null) {
            throw new IllegalStateException("Cannot get c2s sender when not in play stage!");
        }

        return ((ClientPlayPacketHandler.Holder) listener).badpackets_getHandler();
    }

    @Override
    protected Packet<?> createVanillaRegisterPacket(Set<ResourceLocation> channels, Supplier<FriendlyByteBuf> buf) {
        return PlatformProxy.INSTANCE.createVanillaRegisterPlayC2SPacket(channels, buf);
    }

    @Override
    protected void onInitialChannelSyncPacketReceived() {
        sendInitialChannelSyncPacket();
        for (ClientPlayPacketReadyCallback callback : CallbackRegistry.CLIENT_PLAY) {
            callback.onReady(listener, this, client);
        }
    }

    @Override
    protected void receiveUnsafe(ClientPlayPacketReceiver<CustomPacketPayload> receiver, CustomPacketPayload payload) {
        receiver.receive(client, listener, payload, this);
    }

    public interface Holder extends AbstractPacketHandler.Holder {

        ClientPlayPacketHandler badpackets_getHandler();

    }

}
