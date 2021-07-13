package sockets;

import service.GameService;
import thread.ClientHandler;

import static model.Gibbet.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private static final int PORT = 12345;
    private static final String EXIT = "exit";
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private List<PrintStream> streams;

    public Server(){
        this.streams = new ArrayList<>();
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Server server = new Server();
        server.startSockets();
    }

    private void startSockets() throws IOException{
            this.serverSocket = new ServerSocket(PORT);
            System.out.println("Server's up on " + PORT + " port.");

            while(true){
                // Accept a client
                this.clientSocket = serverSocket.accept();
                System.out.println("A new client has connected: " + clientSocket.getInetAddress().getHostAddress());

                // Add the client's output to the list
                PrintStream ps = new PrintStream(this.clientSocket.getOutputStream());
                this.streams.add(ps);

                // Handle with the client on a new Thread
                ClientHandler handler = new ClientHandler(this.clientSocket.getInputStream(), this);
                new Thread(handler).start();
            }
    }

    private void closeSockets(ServerSocket serverSocket, Socket clientSocket) throws IOException{
        System.out.println("Exiting game...");
        this.clientSocket.close();
        this.serverSocket.close();
        System.out.println("Good bye!");
    }

    public void sendMessage(String msg) throws IOException {
        if(EXIT.equalsIgnoreCase(msg)){
            this.closeSockets(this.serverSocket, this.clientSocket);
            return;
        }

        // Send message to everybody
//        for (PrintStream cliente : this.streams) {
//            cliente.println(msg);
//        }
    }
}
