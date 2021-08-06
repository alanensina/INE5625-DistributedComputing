package sockets;

import thread.Receiver;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static final int PORT = 12345;
    private static final String HOST = "127.0.0.1";
    private static final String EXIT = "exit";
    private static final String RESTART = "restart";
    private Thread thread;

    private Socket clientSocket;

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.startSocket();
    }

    private void startSocket() throws IOException {
        this.clientSocket = new Socket(HOST, PORT);
        System.out.println("You're connected!");

        // Thread to receive server's messages
        Receiver receiver = new Receiver(this.clientSocket.getInputStream());
        thread = new Thread(receiver);
        thread.start();

        // Read messages and send to the server
        Scanner input = new Scanner(System.in);

        PrintStream output = new PrintStream(this.clientSocket.getOutputStream());

        while (true) {
            String msg = input.nextLine();
            if (EXIT.equalsIgnoreCase(msg)) {
                output.println(msg);
                this.closeConnection();
            } else if (RESTART.equalsIgnoreCase(msg)) {
                output.println(msg);
                restartGame();
            } else {
                output.println(msg);
            }
        }
    }

    private void closeConnection() throws IOException {
        System.out.println("Exiting game...");
        System.out.println("Good bye!");
        this.clientSocket.close();

        if (this.clientSocket.isClosed()) {
            System.exit(0);
        }
    }

    public void restartGame() throws IOException {
        System.out.println("Restarting game...");
        this.clientSocket.close();
        thread.interrupt();

        startSocket();
    }
}
