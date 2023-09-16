//MCCScript 1.0

MCC.LoadBot(new ExampleChatBot());

//MCCScript Extensions

// The code and comments above are defining a "Script Metadata" section

// Every single chat bot (script) must be a class that extends the ChatBot class.
// Your class must be instantiated in the "Script Metadata" section and passed to MCC.LoadBot function.
public abstract class Packet
{
    public abstract object[] getData();
    public abstract string getId();
}

public class ChatMessageI2BPacket : Packet
{
    public string[] data { get; set; }
    public string id {get; set;}

    public override object[] getData()
    {
        return this.data;
    }

    public override string getId()
    {
        return id;
    }

    public ChatMessageI2BPacket() {
        id = "ChatMessageI2B";
    }
}

public class ChatMessageB2IPacket : Packet
{
    public string[] data { get; set; }
    public string id {get; set;}

    public override object[] getData()
    {
        return data;
    }

    public override string getId()
    {
        return id;
    }

    public ChatMessageB2IPacket(string dat)
    {
        data = new string[] { dat };
        id = "ChatMessageB2I";
    }

    public ChatMessageB2IPacket() {

    }
}

public class BlockPlacedEvent : Packet
{
    public object[] data { get; set; }
    public string id {get; set;}
    public override object[] getData()
    {
        return data;
    }

    public override string getId()
    {
        return id;
    }
    public BlockPlacedEvent(string block, int x, int y, int z, string player) {
        id = "BlockPlacedEvent";
        data = new object[]{block, x, y, z, player};
    }

    public BlockPlacedEvent() {

    }
}

public class BlockBrokenEvent : Packet
{
    public object[] data { get; set; }
    public string id {get; set;}
    public override object[] getData()
    {
        return data;
    }

    public override string getId()
    {
        return id;
    }
    public BlockBrokenEvent(string block, int x, int y, int z, string player) {
        id = "BlockBrokenEvent";
        data = new object[]{block, x, y, z, player};
    }

    public BlockBrokenEvent() {

    }
}

public class POSTPlayers : Packet
{
    public object[] data { get; set; }
    public string id {get; set;}
    public override object[] getData()
    {
        return data;
    }

    public override string getId()
    {
        return id;
    }

    public POSTPlayers(Player[] dat)
    {
        this.data = dat;
        id = "POSTPlayers";
    }

    public POSTPlayers() {

    }
}

public class GETPlayers : Packet
{
    public string id {get; set;}
    public override object[] getData()
    {
        return new object[0];
    }

    public override string getId()
    {
        return id;
    }
    public GETPlayers() {
        id = "GETPlayers";
    }

}

public class Player
{
    public string uuid { get; set; }
    public string name { get; set; }
    public double[] position { get; set; }

    public Player(string uuid, string name, double x, double y, double z)
    {
        this.uuid = uuid;
        this.name = name;
        this.position = new double[] { x, y, z };
    }
}

class ChatBotUtil : ChatBot
{
    public ChatBotUtil()
    {

    }
}

class ExampleChatBot : ChatBot
{
    private static System.Net.Sockets.TcpClient client;
    private static System.Net.Sockets.NetworkStream stream;
    private static System.Runtime.Serialization.Formatters.Binary.BinaryFormatter formatter;

    public override void Initialize()
    {
        Thread newThread = new Thread(runClient);
        newThread.Start();

        LogToConsole("Attempting to connect.");
    }

    public void runClient() {
        LogToConsole("Connecting to the server.");
        string serverIp = "localhost"; // Change to the IP address of your Java server
        int serverPort = 25465;
        client = new System.Net.Sockets.TcpClient(serverIp, serverPort); // Initialize TcpClient
        stream = client.GetStream();
        formatter = new System.Runtime.Serialization.Formatters.Binary.BinaryFormatter();
        LogToConsole("Connected to server.");
        LogToConsole("Starting read listener...");
        Thread newThread2 = new Thread(readData);
        newThread2.Start();
        LogToConsole("Started thread.");

    }

    public void readData() {
        StreamReader reader = new StreamReader(stream);
        try {
            while (true) {

            
                string receivedData = reader.ReadLine();
                    
                // Check for disconnection or other conditions
                if (receivedData == null)
                {
                    Console.WriteLine("Disconnected.");
                    break;
                }
                ProcessPacket(receivedData);
    // Handle disconnection or other conditions that break the loop
                if (!client.Connected)
                {
                    Console.WriteLine("Disconnected.");
                    break;
                }
            }
        }
        finally
        {
            client.Close();
            stream.Close();
            reader.Close();
        }
        client.Close();
        stream.Close();
        reader.Close();
        PerformInternalCommand("exit");
    }

    public static string Serialize(object packet)
    {
        return System.Text.Json.JsonSerializer.Serialize(packet);
    }

    public Packet Deserialize(string json)
    {
        json = json.Trim();
        LogToConsole("Parsing using " + json);
        System.Text.Json.JsonDocument doc = System.Text.Json.JsonDocument.Parse(json);
        System.Text.Json.JsonElement root = doc.RootElement;
        string id = root.GetProperty("id").GetString();
        switch (id)
        {
            case "ChatMessageI2B":
                LogToConsole("Packet Type: CMIB");
                return System.Text.Json.JsonSerializer.Deserialize<ChatMessageI2BPacket>(json);
            case "ChatMessageB2I":
                LogToConsole("Packet Type: CMBI");
                return System.Text.Json.JsonSerializer.Deserialize<ChatMessageB2IPacket>(json);
            case "BlockPlacedEvent":
                return System.Text.Json.JsonSerializer.Deserialize<BlockPlacedEvent>(json);
            case "BlockBrokenEvent":
                return System.Text.Json.JsonSerializer.Deserialize<BlockBrokenEvent>(json);
            case "POSTPlayers":
                return System.Text.Json.JsonSerializer.Deserialize<POSTPlayers>(json);
            case "GETPlayers":
                return System.Text.Json.JsonSerializer.Deserialize<GETPlayers>(json);
            default:
                throw new NotSupportedException($"Unknown packet type: {id}");
        }
    }

    public void ProcessPacket(string packetData)
    {
        // Implement packet deserialization and processing logic here
        LogToConsole("Received packet: " + packetData);
        Packet receivedPacket = Deserialize(packetData);
        if (receivedPacket is GETPlayers)
        {
            List<Entity> list = GetEntities().Values.ToList().Where(item => item.Type == EntityType.Player).ToList();
            List<Player> pl = new List<Player>();
            for (int i = 0; i < list.Count; i++)
            {
                pl.Add(new Player(list[i].UUID.ToString(), list[i].Name, list[i].Location.X, list[i].Location.Y, list[i].Location.Z));
            }
            var spacket = new POSTPlayers(pl.ToArray());
            SendPacket(spacket);
        }
        else if (receivedPacket is ChatMessageI2BPacket)
        {
            ChatMessageB2IPacket packet = System.Text.Json.JsonSerializer.Deserialize<ChatMessageB2IPacket>(packetData);
            SendText((string)packet.getData()[0]);
        }else{
            LogToConsole("Failure to detect packet after deserialization.");
        }
    }

    public override void GetText(string text)
    {
        var chatPacket = new ChatMessageB2IPacket(text);
        SendPacket(chatPacket);
    }

    public void SendPacket(Packet packet)
    {
    
        string message = Serialize(packet);
        LogToConsole("Send Packet: " + message);
        stream.Write(Encoding.ASCII.GetBytes(message), 0, message.Length);
        stream.Flush();
    }
}
