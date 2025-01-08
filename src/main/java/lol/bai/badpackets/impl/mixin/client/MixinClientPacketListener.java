package lol.bai.badpackets.impl.mixin.client;

import lol.bai.badpackets.impl.handler.ClientPlayPacketHandler;
import lol.bai.badpackets.impl.handler.PacketHandlerHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.network.Connection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class MixinClientPacketListener extends MixinClientCommonPacketListenerImpl implements PacketHandlerHolder<ClientPlayPacketHandler> {

    @Unique
    private ClientPlayPacketHandler badpacket_packetHandler;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void badpackets_createClientPacketHandler(Minecraft client, Connection connection, CommonListenerCookie cookie, CallbackInfo ci) {
        badpacket_packetHandler = new ClientPlayPacketHandler(client, (ClientPacketListener) (Object) this);
    }

    @Override
    public ClientPlayPacketHandler badpackets_handler() {
        return badpacket_packetHandler;
    }

}
