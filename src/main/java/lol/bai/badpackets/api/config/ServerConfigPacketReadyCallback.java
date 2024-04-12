package lol.bai.badpackets.api.config;

import lol.bai.badpackets.impl.marker.ApiSide;

@ApiSide.ServerOnly
@FunctionalInterface
public interface ServerConfigPacketReadyCallback {

    void onConfig(ServerConfigConnectionContext context);

}
