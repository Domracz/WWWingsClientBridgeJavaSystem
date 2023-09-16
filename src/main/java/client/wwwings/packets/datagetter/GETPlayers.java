package client.wwwings.packets.datagetter;

import client.wwwings.packets.Packet;

public class GETPlayers extends Packet {
    public String id;

    @Override
    public Object[] data() {
        return new Object[0];
    }

    @Override
    public String id() {
        return "GETPlayers";
    }

    public GETPlayers() {
        id = "GETPlayers";
    }
}
