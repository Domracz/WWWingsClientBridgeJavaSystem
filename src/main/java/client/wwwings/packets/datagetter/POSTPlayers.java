package client.wwwings.packets.datagetter;

import client.wwwings.Player;
import client.wwwings.packets.Packet;

public class POSTPlayers extends Packet {
    Object[] data;
    String id;

    public POSTPlayers(Player[] players) {
        data = players;
        id = "POSTPlayers";
    }

    @Override
    public Object[] data() {
        return data;
    }

    @Override
    public String id() {
        return "POSTPlayers";
    }
}
