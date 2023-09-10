package client.wwwings.packets;

public class BlockPlacedEvent extends Packet{
    Object[] data;
    public BlockPlacedEvent(int x, int y, int z, String blockid) {
        data = new Object[]{x, y, z, blockid};
    }
    @Override
    public String getId() {
        return "BlockPlaced";
    }

    @Override
    public Object[] data() {
        return data;
    }
}
