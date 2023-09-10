package client.wwwings.packets.datagetter;

import client.wwwings.packets.Packet;

public class GETPlayers extends Packet {

    @Override
    public String getId() {
        return "GETPlayers";
    }

    @Override
    public Object[] data() {
        return new Object[0];
    }
}
