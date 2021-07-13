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

    private Socket clientSocket;

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.startSocket();
    }

    private void startSocket() throws IOException{
        this.clientSocket = new Socket(HOST, PORT);
        System.out.println("You're connected!");

        // Thread to receive server's messages
        Receiver receiver = new Receiver(this.clientSocket.getInputStream());
        new Thread(receiver).start();

        // Read messages and send to the server
        Scanner input = new Scanner(System.in);

        PrintStream output = new PrintStream(this.clientSocket.getOutputStream());
        while (input.hasNextLine()) {
            String msg = input.nextLine();
            checkExitMessage(msg);
            output.println(msg);
        }
    }

    private void closeSocket(Socket clientSocket) throws IOException{
        clientSocket.close();
    }

    private void checkExitMessage(String msg) throws IOException {
        if(EXIT.equalsIgnoreCase(msg)){
            System.out.println("Exiting game...");
            this.closeSocket(this.clientSocket);
            System.out.println("Good bye!");
        }
    }
}
