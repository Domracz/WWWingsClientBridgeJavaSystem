package client.wwwings;
import java.util.UUID;

public class Player {
    private String uuid;
    private String name;
    private double[] position;
    public Player(String uuid, String name, double x, double y, double z) {
        this.name = name;
        this.uuid = uuid;
        this.position = new double[]{x, y, z};
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return uuid;
    }
}
