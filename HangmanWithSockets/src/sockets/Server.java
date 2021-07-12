package sockets;

import service.GameService;
import static model.Gibbet.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final int PORT = 12345;
    private static final String EXIT = "exit";
    private ServerSocket serverSocket;
    private Socket clientSocket;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Server server = new Server();
        server.startSockets();
        server.startHangman();
    }

    private void startSockets() throws IOException{
            this.serverSocket = new ServerSocket(PORT);
            System.out.println("Server's up on " + PORT + " port.");
            this.clientSocket = serverSocket.accept();
            System.out.println("A new client has connected: " + clientSocket.getInetAddress().getHostAddress());
    }

    private void startHangman() throws IOException, ClassNotFoundException {
        GameService service = new GameService();
        service.startGame();
        System.out.println("Welcome to Hangman!");
        printGibbet();
        service.printArrayOfLetters();

            while (service.isAlive()) {

                ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
                String guess = (String) input.readObject();
                if(EXIT.equalsIgnoreCase(guess)){
                    closeSockets(this.serverSocket, this.clientSocket);
                    input.close();
                    break;
                }

                service.makeAGuess(guess);
            }
    }

    private void closeSockets(ServerSocket serverSocket, Socket clientSocket) throws IOException{
        this.clientSocket.close();
        this.serverSocket.close();
    }
}
