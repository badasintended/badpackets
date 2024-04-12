package lol.bai.badpackets.api.play;

import lol.bai.badpackets.impl.marker.ApiSide;

@ApiSide.ClientOnly
@FunctionalInterface
public interface ClientPlayPacketReadyCallback {

    void onReady(ClientPlayConnectionContext context);

}
