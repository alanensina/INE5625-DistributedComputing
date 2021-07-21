package sockets;

import thread.ClientHandler;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private static final int PORT = 12345;
    private static final String EXIT = "EXIT";
    private static final String FAIL = "FAIL";
    private static final String SUCCESS = "SUCCESS";
    private static final String IN_PROGRESS = "IN_PROGRESS";
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private List<PrintStream> streams;
    private PrintWriter output;

    public Server() {
        this.streams = new ArrayList<>();
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
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

            output = new PrintWriter(clientSocket.getOutputStream(), true);

            // Add the client's output to the list
            PrintStream ps = new PrintStream(this.clientSocket.getOutputStream());
            this.streams.add(ps);

            // Handle with the client on a new Thread
            ClientHandler handler = new ClientHandler(this.clientSocket.getInputStream(), this);
            new Thread(handler).start();
        }
    }

    private void closeSockets(ServerSocket serverSocket, Socket clientSocket) throws IOException {
        System.out.println("Exiting game...");
        this.clientSocket.close();
        this.serverSocket.close();
        System.out.println("Good bye!");

        if (this.serverSocket.isClosed() && this.clientSocket.isClosed()) {
            System.exit(0);
        }
    }

    public void sendMessage(String msg) throws IOException {
        switch (msg) {
            case EXIT:
                this.closeSockets(this.serverSocket, this.clientSocket);
                break;
            case FAIL:
                output.println("You failed! Game over. Type 'exit' to quit.");
                this.closeSockets(serverSocket, clientSocket);
                break;
            case IN_PROGRESS:
                output.println("Insert a guess or type 'exit' if you want to quit: ");
                break;
            case SUCCESS:
                output.println("Congratulations, you win! Type 'exit' to quit.");
                this.closeSockets(serverSocket, clientSocket);
                break;
            default:
                throw new RuntimeException("Message not found.");
        }
    }
}
