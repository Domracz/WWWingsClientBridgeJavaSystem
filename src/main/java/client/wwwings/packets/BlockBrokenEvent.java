package client.wwwings.packets;

public class BlockBrokenEvent extends Packet{
    Object[] data;
    String id;
    public BlockBrokenEvent(int x, int y, int z, String blockid) {
        data = new Object[]{x, y, z, blockid};
        id = "BlockBroken";
    }

    @Override
    public Object[] data() {
        return data;
    }

    @Override
    public String id() {
        return "BlockBrokenEvent";
    }


}
