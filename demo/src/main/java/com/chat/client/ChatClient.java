package com.chat.client;

/**
 *
 * @author 
 */
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import com.chat.AESEncryption;

import java.nio.charset.StandardCharsets;
import java.security.Key;

public class ChatClient {

    private final int SERVER_PORT = 49999;
    private Socket clientSocket;
    private BufferedReader inputFromServer;
    private PrintWriter outputToServer;
    private Scanner sc = new Scanner(System.in);
    private com.chat.AESEncryption AESEnc = new com.chat.AESEncryption();

    /**
     * Creates a connection to a server.
     */
    private void connectToServer() {
        try {
            // -- Step 1) Create a client socket to connect to the server.
            // This is the endpoint of the client/server communication.
            // TODO: change the IP address below if you would like to connect
            // to a server running on another computer.
            // @see: https://docs.oracle.com/javase/7/docs/api/java/net/Socket.html
            // --
            clientSocket = new Socket(InetAddress.getLocalHost(), SERVER_PORT);

            // -- Step 2) Create an output stream to send data to the server.
            // Used for sending a request to the server.
            outputToServer = new PrintWriter(clientSocket.getOutputStream());

            // -- Step 3)Create an input stream to read data from the server
            // Used for receiving a rerequest from the server.
            inputFromServer = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
        } catch (IOException e) {
            System.out.println("CLIENT: Cannot connect to server");
            System.exit(-1);
        }
    }

    /**
     * Disconnect from the server.
     */
    private void disconnectFromServer() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("CLIENT: Cannot disconnect from server");
        }
    }

    private void askForTime() throws Exception {
        connectToServer();
        
        // Prepare the REQUEST to be sent to the server.
        outputToServer.println(ProtocolConstants.CMD_GET_TIME);
        outputToServer.println("What Time is It ?");
        outputToServer.flush();
    
        try {
            // Receive the encrypted bytes from the server
            InputStream inputStream = clientSocket.getInputStream();
            byte[] encryptedBytes = new byte[128]; // Adjust the buffer size as needed
            int bytesRead = inputStream.read(encryptedBytes, 0, encryptedBytes.length);
    
            // Print the encrypted message as characters using UTF-8 encoding
            String encryptedString = new String(encryptedBytes, StandardCharsets.UTF_8);
    
            // Decrypt the bytes using AESEncryption
            String decryptedString = AESEnc.decrypt(Arrays.copyOf(encryptedBytes, bytesRead));
            System.out.println("CLIENT> The current time is " + decryptedString);
            System.out.println("CLIENT> Encrypted message of the time: " + encryptedString);
        } catch (IOException e) {
            System.out.println("CLIENT> Cannot receive time from server");
        }
    
        disconnectFromServer();
    }
    

    /**
     * Ask the server for domain
     */
    private void askForDomain() throws Exception {
        try {
            connectToServer();
            outputToServer.println(ProtocolConstants.CMD_GET_DOMAIN);
            outputToServer.println("What is the domain of the server?");
            outputToServer.flush();
    
            InputStream inputStream = clientSocket.getInputStream();
            byte[] encryptedBytes = new byte[128]; // Adjust the buffer size as needed
            int bytesRead = inputStream.read(encryptedBytes, 0, encryptedBytes.length);
    
            // Print the encrypted message as characters using UTF-8 encoding
            String encryptedString = new String(encryptedBytes, StandardCharsets.UTF_8);
    
            // Decrypt the message
            String decryptedString = AESEnc.decrypt(Arrays.copyOf(encryptedBytes, bytesRead));
    
            // Print the decrypted message
            System.out.println("CLIENT> Server's domain is " + decryptedString);
            System.out.println("CLIENT> Encrypted domain address: " + encryptedString);
            disconnectFromServer();
        } catch (IOException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    private void promptForTimeRequest() throws Exception {

        System.out.println("CLIENT> Do you want to request the time from the server? (yes/no)");
        String response = sc.nextLine().toLowerCase();

        if (response.equals("yes")) {
            askForTime();
        } else {
            System.out.println("CLIENT> Okay, not requesting time.");
        }
    }

    private void promptForDomainRequest() throws Exception{

        System.out.println("CLIENT> Do you want to request the domain of the server? (yes/no)");
        String response = sc.nextLine().toLowerCase();
        sc.close();

        if (response.equals("yes")) {
            askForDomain();
        } else {
            System.out.println("CLIENT> Okay, not requesting domain.");
        }
    }

    private static void Delay() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
    }

    public static void main(String[] args) throws Exception {
        ChatClient client = new ChatClient();
        Delay();
        client.promptForTimeRequest();
        Delay();
        client.promptForDomainRequest();
        Delay();
    }

}
