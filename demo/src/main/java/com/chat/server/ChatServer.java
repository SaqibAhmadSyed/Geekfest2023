package com.chat.server;

/**
 *
 * @author
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import com.chat.AESEncryption;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatServer {

    // The port number on wich the server will be listening to incoming
    // client requests.
    public static int SERVER_PORT = 49999;
    private int counter = 0;
    private AESEncryption AESEnc = new AESEncryption();

    /**
     * Creates and starts a server socket.
     *
     * @return the newly created server socket.
     */
    private ServerSocket startServer() {
        ServerSocket serverSocket = null;
        try {
            // -- Step 1) create a server socket.
            serverSocket = new ServerSocket(SERVER_PORT);
        } catch (IOException e) {
            System.out.println("SERVER> Error creating network connection");
        }
        System.out.println("SERVER> up and running...");
        return serverSocket;
    }

    // Handle all requests
    private void handleRequests(ServerSocket serverSocket) throws Exception {
        // Continuously serve the client.
        while (true) {
            Socket clientSocket = null;
            BufferedReader inputFromClient = null;
            PrintWriter outputToClient = null;

            try {
                // -- Step 2) Wait for an incoming client request.
                clientSocket = serverSocket.accept();
                // At this point, a client connection has been established.

                // -- Step 3) Create reading and writing streams.
                // --> inputFromClient: Holds the incoming requests.
                inputFromClient = new BufferedReader(new InputStreamReader(
                        clientSocket.getInputStream()));
                // --> outputToClient: Holds the responses to be sent to the client.
                outputToClient = new PrintWriter(clientSocket.getOutputStream());

            } catch (IOException e) {
                System.out.println("SERVER> Error connecting to client");
                System.exit(-1);
            }
            try {
                // Parse the client's request (aka the message received).
                // a) Retrieve the command sent by the client (from the received message).
                String requestCommand = inputFromClient.readLine();
                // b) Retrieve the body of the message sent by the client.
                String requestBody = inputFromClient.readLine();
                System.out.println("SERVER> Client Message Received: " + requestBody);
                // Process the client's request.
                switch (requestCommand) {
                    case ProtocolConstants.CMD_GET_TIME -> {
                        Date currentDate = new Date();
                        String dateString = currentDate.toString();
                    
                        // Encrypt the date string using AESEncryption
                        byte[] encryptedBytes = AESEnc.encrypt(dateString);
                    
                        // Print the encrypted message as characters using UTF-8 encoding
                        String encryptedString = new String(encryptedBytes, StandardCharsets.UTF_8);
                        System.out.println("SERVER> Encrypted message sent: " + encryptedString);
                    
                        // Send the encrypted bytes to the client
                        OutputStream outputStream = clientSocket.getOutputStream();
                        outputStream.write(encryptedBytes, 0, encryptedBytes.length);
                        outputStream.flush();
                    
                        counter++;
                        System.out.println("SERVER> Successful!");
                    }
                    case ProtocolConstants.CMD_GET_DOMAIN -> {
                        // Send back server's domain.
                        String domain = "www.geekfest2023.com";
                        byte[] encryptedBytes = AESEnc.encrypt(domain);
                    
                        // Print the encrypted message as characters using UTF-8 encoding
                        String encryptedString = new String(encryptedBytes, StandardCharsets.UTF_8);
                        System.out.println("SERVER> Encrypted message sent: " + encryptedString);
                    
                        OutputStream outputStream = clientSocket.getOutputStream();
                        outputStream.write(encryptedBytes, 0, encryptedBytes.length);
                        outputStream.flush();
                    
                        counter++;
                        System.out.println("SERVER> Successful!");
                    }
                    default -> {
                        System.out.println("SERVER> Unknown request: " + requestBody + " " + requestCommand);
                    }
                }
                // Now make sure that the response is sent
                // NOTES: Communication through sockets is always buffered.
                // This means nothing is sent or received until the buffers fill up,
                // or you explicitly flush the buffer.
                outputToClient.flush();
                clientSocket.close(); // We are done with the client's request

            } catch (IOException e) {
                System.out.println("SERVER> Error communicating with client");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        ChatServer serverApp = new ChatServer();
        ServerSocket serverSocket = serverApp.startServer();
        if (serverSocket != null) {
            serverApp.handleRequests(serverSocket);
        }

    }
}
