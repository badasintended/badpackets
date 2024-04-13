package lol.bai.badpackets.api;

@FunctionalInterface
public interface PacketReceiver<C, P> {

    void receive(C context, P payload);

}
