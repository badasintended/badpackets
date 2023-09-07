package lol.bai.badpackets.api.play;

import lol.bai.badpackets.api.PacketSender;
import lol.bai.badpackets.impl.marker.ApiSide;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;

@ApiSide.ClientOnly
@FunctionalInterface
public interface ClientPlayPacketReadyCallback {

    void onReady(ClientPacketListener handler, PacketSender sender, Minecraft client);

}
