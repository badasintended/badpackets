package lol.bai.badpackets.api.play;

@FunctionalInterface
public interface ServerPlayPacketReceiver<P> {

    void receive(ServerPlayConnectionContext context, P payload);

}
