package lol.bai.badpackets.api;

@FunctionalInterface
public interface PacketReadyCallback<C> {

    void onReady(C context);

}
