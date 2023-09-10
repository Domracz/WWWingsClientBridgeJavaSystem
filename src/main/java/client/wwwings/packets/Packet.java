package client.wwwings.packets;

import java.io.Serializable;

public abstract class Packet implements Serializable {
    public abstract String getId();
    public abstract Object[] data();
}
