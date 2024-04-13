package lol.bai.badpackets.impl.registry;

import java.util.ArrayList;

import lol.bai.badpackets.api.PacketReadyCallback;
import lol.bai.badpackets.api.config.ClientConfigContext;
import lol.bai.badpackets.api.config.ServerConfigContext;
import lol.bai.badpackets.api.play.ClientPlayContext;
import lol.bai.badpackets.api.play.ServerPlayContext;

public class CallbackRegistry {

    public static final ArrayList<PacketReadyCallback<ClientConfigContext>> CLIENT_READY_CONFIG = new ArrayList<>();
    public static final ArrayList<PacketReadyCallback<ServerConfigContext>> SERVER_READY_CONFIG = new ArrayList<>();

    public static final ArrayList<PacketReadyCallback<ClientPlayContext>> CLIENT_PLAY = new ArrayList<>();
    public static final ArrayList<PacketReadyCallback<ServerPlayContext>> SERVER_PLAY = new ArrayList<>();

}
