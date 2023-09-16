package client.wwwings;

import client.wwwings.packets.*;
import client.wwwings.packets.datagetter.GETPlayers;
import client.wwwings.packets.datagetter.POSTPlayers;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class BeginClient {
    static AtomicReference<PrintWriter> writer = new AtomicReference<>();
    public static void main(String[] args) {
        System.out.println("Starting proccess...");
        //Start MCC
        AtomicReference<Consumer<Player[]>> playercallback = new AtomicReference<>();
        Thread mccthread = new Thread(() -> {
            try {
                ProcessBuilder processBuilder = new ProcessBuilder("mcc.exe", "arg1", "arg2");

                processBuilder.directory(new File(".\\"));

                // Start the process
                Process process = processBuilder.start();

                // Wait for the process to complete
                int exitCode = process.waitFor();
                System.out.println("MCC has stopped with exit code: " + exitCode);
            }catch (Exception e) {
                System.out.println("MCC failure..");
            }
        });
        //Start server and connect to MCC
        Thread serverthread = new Thread(() -> {
            try {
                ServerSocket server = new ServerSocket(25465);
                System.out.println("Server started.");
                Socket clientSocket = server.accept();
                writer.set(new PrintWriter(clientSocket.getOutputStream()));
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                System.out.println(new Gson().toJson(new ChatMessageI2BPacket("")));
                System.out.println(new Gson().toJson(new ChatMessageB2IPacket("")));
                System.out.println(new Gson().toJson(new BlockPlacedEvent(0, 0, 0, "")));
                System.out.println(new Gson().toJson(new GETPlayers()));
                System.out.println(new Gson().toJson(new POSTPlayers(null)));
                // Handle client communication here
                // You can create input and output streams to send/receive data
                // Example:
                try {
                    // Receive packets from the client
                    BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    String receivedPacketString = reader.readLine();
                    System.out.println(receivedPacketString);
                    while (receivedPacketString != null){
                        receivedPacketString = reader.readLine();
                        System.out.println("Packet: " + receivedPacketString);
                        JsonObject jobj = new Gson().fromJson(receivedPacketString, JsonObject.class);
                        String id = jobj.get("id").getAsString();
                        Packet receivepacket;
                        if (id.equals("ChatMessageB2I")) {
                            receivepacket = new Gson().fromJson(receivedPacketString, ChatMessageB2IPacket.class);
                        }else if (id.equals("BlockPlacedEvent")) {
                            receivepacket = new Gson().fromJson(receivedPacketString, BlockPlacedEvent.class);
                        }else if (id.equals("BlockBrokenEvent")) {
                            receivepacket = new Gson().fromJson(receivedPacketString, BlockBrokenEvent.class);
                        }else if (id.equals("POSTPlayers")) {
                            receivepacket = new Gson().fromJson(receivedPacketString, POSTPlayers.class);
                        }else {
                            throw new ClassNotFoundException();
                        }


                        if (receivepacket instanceof ChatMessageB2IPacket packet) {
                            System.out.println("[CHAT] " + packet.data()[0]);
                        }else if (receivepacket instanceof BlockPlacedEvent packet) {
                            System.out.println("[Block] Place: " + "X: " + packet.data()[0]+ "Y: " + packet.data()[1]+ "Z: " + packet.data()[2] + "Block: " + packet.data()[3]);
                        }else if (receivepacket instanceof BlockBrokenEvent packet) {
                            System.out.println("[Block] Break: " + "X: " + packet.data()[0]+ "Y: " + packet.data()[1]+ "Z: " + packet.data()[2] + "Block: " + packet.data()[3]);
                        } else if (receivepacket instanceof POSTPlayers packet) {
                            System.out.println("Players received.");
                            playercallback.get().accept((Player[]) packet.data()[0]);
                        }else{
                            System.out.println("No packet found.");
                        }
                    }
                    System.out.println("Connection cleared.");
                } catch (IOException e) {

                } finally {
                    // Close the client socket (you may want to handle this more gracefully)
                    clientSocket.close();
                }
                // Close the client socket when done
            }catch (Exception e) {
                System.out.println("Failure to begin or run server. Closed.");
                e.printStackTrace();
                System.exit(-1);
            }
        });
        serverthread.start();
        Scanner stop = new Scanner(System.in);
        String input;
        while (!Objects.equals(input = stop.nextLine(), "$stop")) {
            sendPacket(new ChatMessageI2BPacket(input));
            System.out.println("Sent chat message or command: " + input);
            playercallback.set(players -> {
                for (Player player : players
                     ) {
                    System.out.println(player.getName());
                }
            });
            sendPacket(new GETPlayers());
        }
    }

    public static void sendPacket(Packet packet) {
        String json = new Gson().toJson(packet);
        System.out.println(json);
        writer.get().println(json);
        writer.get().flush();
    }
}
