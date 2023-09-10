package client.wwwings;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                String line = null;
                while ((line = in.readLine()) != null) {
                    if (line.startsWith("chat{")) {
                        String msg = line.replace("chat{", "").replace("}");

                    }
                }
                server.close();
                // Close the client socket when done
            }catch (Exception e) {
                System.out.println("Failure to begin or run server. Closed.");
                System.exit(-1);
            }
        });
    }
}
