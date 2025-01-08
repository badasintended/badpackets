package lol.bai.badpackets.impl.mixin.client;

import lol.bai.badpackets.impl.Constants;
import lol.bai.badpackets.impl.handler.ClientConfigPacketHandler;
import lol.bai.badpackets.impl.handler.PacketHandlerHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientConfigurationPacketListenerImpl;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.network.Connection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConfigurationPacketListenerImpl.class)
public abstract class MixinClientConfigurationPacketListenerImpl extends MixinClientCommonPacketListenerImpl implements PacketHandlerHolder<ClientConfigPacketHandler> {

    @Unique
    private ClientConfigPacketHandler badpackets_packetHandler;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void badpackets_createPacketHandler(Minecraft minecraft, Connection connection, CommonListenerCookie cookie, CallbackInfo ci) {
        badpackets_packetHandler = new ClientConfigPacketHandler(minecraft, (ClientConfigurationPacketListenerImpl) (Object) this, connection);
    }

    @Override
    protected boolean badpackets_handlePing(int id) {
        return id == Constants.PING_PONG;
    }

    @Override
    public ClientConfigPacketHandler badpackets_handler() {
        return badpackets_packetHandler;
    }

}
