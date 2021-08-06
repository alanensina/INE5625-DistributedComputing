package sockets;

import model.Response;
import thread.ClientHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Server {

    private static final int PORT = 12345;

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private ObjectOutputStream output;

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

            // Output used to send message to client
            output = new ObjectOutputStream(clientSocket.getOutputStream());

            // Handle with the client on a new Thread
            ClientHandler handler = new ClientHandler(this.clientSocket.getInputStream(), this);
            new Thread(handler).start();
        }
    }

    public void sendMessage(Response response) throws IOException {
        switch (response.getStatus()) {
            case EXIT, FAIL, SUCCESS, IN_PROGRESS -> {
                output.writeObject(response);
            }
            case RESTART -> System.out.println();
            default -> throw new RuntimeException("Message not found.");
        }
    }

    public void sendWelcomeMessage(List<Response> responses) {
        responses.forEach(response -> {
            try {
                output.writeObject(response);
            } catch (IOException e) {
                throw new RuntimeException("Message not found.");
            }
        });
    }
}