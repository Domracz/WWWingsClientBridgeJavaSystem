package client.wwwings.packets;

public class ChatMessageI2BPacket extends Packet{
    String[] data;
    String id;
    public ChatMessageI2BPacket(String data) {
        this.data = new String[]{data};
        id = "ChatMessageI2B";
    }

    @Override
    public Object[] data() {
        return this.data;
    }

    @Override
    public String id() {
        return "ChatMessageI2B";
    }
}
