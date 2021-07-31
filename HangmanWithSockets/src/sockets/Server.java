package sockets;

import model.Response;
import thread.ClientHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private static final int PORT = 12345;

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private List<PrintStream> streams;
    private ObjectOutputStream output;

    public Server() {
        this.streams = new ArrayList<>();
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.startSockets();
    }

    private void startSockets() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
        System.out.println("Server's up on " + PORT + " port.");

        while (true) {
            // Accept a client
            this.clientSocket = serverSocket.accept();
            System.out.println("A new client has connected: " + clientSocket.getInetAddress().getHostAddress());

            output = new ObjectOutputStream(clientSocket.getOutputStream());

            // Add the client's output to the list
            PrintStream ps = new PrintStream(this.clientSocket.getOutputStream());
            this.streams.add(ps);

            // Handle with the client on a new Thread
            ClientHandler handler = new ClientHandler(this.clientSocket.getInputStream(), this);
            new Thread(handler).start();
        }
    }

    private void closeSockets() throws IOException {
        this.clientSocket.close();
        this.serverSocket.close();

        if (this.serverSocket.isClosed() && this.clientSocket.isClosed()) {
            System.exit(0);
        }
    }

    public void sendMessage(Response response) throws IOException {
        switch (response.getStatus()) {
            case EXIT, FAIL, SUCCESS -> {
                output.writeObject(response);
                this.closeSockets();
            }
            case IN_PROGRESS -> output.writeObject(response);
            default -> throw new RuntimeException("Message not found.");
        }
    }
}
