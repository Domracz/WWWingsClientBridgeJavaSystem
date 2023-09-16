package client.wwwings.packets;

public class BlockPlacedEvent extends Packet{
    Object[] data;
    String id;
    public BlockPlacedEvent(int x, int y, int z, String blockid) {
        data = new Object[]{x, y, z, blockid};
        id = "BlockPlacedEvent";
    }

    @Override
    public Object[] data() {
        return data;
    }

    @Override
    public String id() {
        return "BlockPlacedEvent";
    }
}
