package lol.bai.badpackets.api.play;

import lol.bai.badpackets.impl.marker.ApiSide;

@ApiSide.ClientOnly
@FunctionalInterface
public interface ClientPlayPacketReceiver<P> {

    void receive(ClientPlayConnectionContext context, P payload);

}
