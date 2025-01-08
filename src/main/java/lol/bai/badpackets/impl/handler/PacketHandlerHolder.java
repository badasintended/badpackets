package lol.bai.badpackets.impl.handler;

public interface PacketHandlerHolder<T extends AbstractPacketHandler<?>> {

    T badpackets_handler();

}
