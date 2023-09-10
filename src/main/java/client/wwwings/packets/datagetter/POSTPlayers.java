package client.wwwings.packets.datagetter;

import client.wwwings.Player;
import client.wwwings.packets.Packet;

public class POSTPlayers extends Packet {
    Object[] data;

    public POSTPlayers(String uuid, String name, int x, int y, int z) {
        data = new Object[]{new Player(uuid, name, x, y, z)};
    }

    @Override
    public String getId() {
        return "POSTPlayers";
    }
    @Override
    public Object[] data() {
        return data;
    }
}
