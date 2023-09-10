package client.wwwings.packets;

public class ChatMessageI2BPacket extends Packet{
    String[] data;
    public ChatMessageI2BPacket(String[] data) {
        this.data = data;
    }
    @Override
    public String getId() {
        return "ChatMessageI2B";
    }

    @Override
    public Object[] data() {
        return this.data;
    }
}
