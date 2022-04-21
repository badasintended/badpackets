package lol.bai.badpackets.impl.registry;

import java.util.ArrayList;

import lol.bai.badpackets.api.event.PacketSenderReadyCallback;

public class CallbackRegistry {

    public static final ArrayList<PacketSenderReadyCallback.Client> CLIENT_PLAYER_JOIN = new ArrayList<>();
    public static final ArrayList<PacketSenderReadyCallback.Server> SERVER_PLAYER_JOIN = new ArrayList<>();

}
