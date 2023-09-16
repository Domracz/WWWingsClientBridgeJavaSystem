package client.wwwings.packets;

public class ChatMessageB2IPacket extends Packet{
    String[] data;
    String id;
    public ChatMessageB2IPacket(String data) {
        this.data = new String[]{data};
        id = "ChatMessageB2I";
    }

    @Override
    public Object[] data() {
        return data;
    }

    @Override
    public String id() {
        return "ChatmessageB2I";
    }

}
