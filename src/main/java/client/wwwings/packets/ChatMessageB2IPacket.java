package client.wwwings.packets;

public class ChatMessageB2IPacket extends Packet{
    String[] data;
    public ChatMessageB2IPacket(String data) {
        this.data = new String[]{data};
    }
    @Override
    public String getId() {
        return "ChatMessageB2I";
    }

    @Override
    public Object[] data() {
        return data;
    }
}
