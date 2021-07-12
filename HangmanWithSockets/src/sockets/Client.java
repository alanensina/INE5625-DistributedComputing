package sockets;

import java.io.IOException;
import java.io.ObjectOutputStream;
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
        client.startHangman();
    }

    private void startSocket() throws IOException{
        this.clientSocket = new Socket(HOST, PORT);
        System.out.println("You're connected!");
    }

    private void closeSocket(Socket clientSocket) throws IOException{
        clientSocket.close();
    }

    private void startHangman() throws IOException{

        Scanner input = new Scanner(System.in);
        ObjectOutputStream output = new ObjectOutputStream(this.clientSocket.getOutputStream());

        System.out.println("Welcome to Hangman!");

        while (true) {
            System.out.println("Insert a letter/word or type 'exit' to quit game:");
            String guess = input.nextLine();

            if(EXIT.equalsIgnoreCase(guess)){
                output.writeObject(guess);
                output.close();
                input.close();
                closeSocket(this.clientSocket);
                break;
            }
            output.writeObject(guess);
        }
    }
}
