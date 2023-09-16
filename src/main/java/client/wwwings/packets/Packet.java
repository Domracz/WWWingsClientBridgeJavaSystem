package client.wwwings.packets;

import java.io.Serializable;

public abstract class Packet implements Serializable {
    public abstract Object[] data();
    public abstract String id();
}
