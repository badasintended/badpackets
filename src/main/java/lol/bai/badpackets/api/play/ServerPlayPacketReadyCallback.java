package lol.bai.badpackets.api.play;

@FunctionalInterface
public interface ServerPlayPacketReadyCallback {

    void onReady(ServerPlayConnectionContext context);

}
