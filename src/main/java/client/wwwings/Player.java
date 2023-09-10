package client.wwwings;
import java.util.UUID;

public class Player {
    private String uuid;
    private String name;
    private int[] position;
    public Player(String uuid, String name, int x, int y, int z) {
        this.name = name;
        this.uuid = uuid;
        this.position = new int[]{x, y, z};
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return uuid;
    }
}
