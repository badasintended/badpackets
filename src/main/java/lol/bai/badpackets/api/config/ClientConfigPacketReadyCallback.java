package lol.bai.badpackets.api.config;

import lol.bai.badpackets.impl.marker.ApiSide;

@ApiSide.ClientOnly
@FunctionalInterface
public interface ClientConfigPacketReadyCallback {

    void onConfig(ClientConfigConnectionContext context);

}
