package client.wwwings;

import client.wwwings.packets.ChatMessageB2IPacket;
import client.wwwings.packets.ChatMessageI2BPacket;
import client.wwwings.packets.Packet;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class BeginClient {
    public static void main(String[] args) {
        System.out.println("Starting proccess...");
        //Start MCC
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
                ServerSocket server = new ServerSocket(2863);
                System.out.println("Server started.");
                Socket clientSocket = server.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                // Handle client communication here
                // You can create input and output streams to send/receive data
                // Example:
                try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())) {
                    // Receive packets from the client
                    Packet receivedPacket;
                    while ((receivedPacket = (Packet) ois.readObject()) != null) {
                        if (receivedPacket instanceof ChatMessageI2BPacket packet) {
                            System.out.println(packet.data()[0]);
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    // Close the client socket (you may want to handle this more gracefully)
                    clientSocket.close();
                }
                // Close the client socket when done
            }catch (Exception e) {
                System.out.println("Failure to begin or run server. Closed.");
                System.exit(-1);
            }
        });
    }
}
