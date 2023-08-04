package lol.bai.badpackets.impl.mixin;

import lol.bai.badpackets.impl.handler.ServerPacketHandler;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public class MixinPlayerList {

    @Inject(method = "placeNewPlayer", at = @At("TAIL"))
    private void badpackets_initServerPlayPacketHandler(Connection netManager, ServerPlayer player, int latency, CallbackInfo ci) {
        ServerPacketHandler.get(player).sendInitialChannelSyncPacket();
    }

}
