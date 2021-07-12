package sockets;

import service.GameService;
import threads.Receiver;

import static model.Gibbet.*;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 12345;

    private String host;
    private int port;
    private GameService service;

    public static void main(String[] args) throws UnknownHostException, IOException {
        // dispara cliente
        new Client(HOST, PORT).execute();
    }

    public Client (String host, int port) {
        this.host = host;
        this.port = port;
        this.service = new GameService();
        this.service.startGame();
    }

    public void execute() throws UnknownHostException, IOException {
        Socket clientSocket = new Socket(this.host, this.port);
        System.out.println("Welcome to the Hangman!");
        printGibbet();
        this.service.printArrayOfLetters();

        // thread para receber mensagens do servidor
        Receiver r = new Receiver(clientSocket.getInputStream());
        new Thread(r).start();

        // lê msgs do input e manda pro servidor
        Scanner input = new Scanner(System.in);
        PrintStream output = new PrintStream(clientSocket.getOutputStream());
        while (input.hasNextLine()) {
            String msg = input.nextLine();
            this.service.makeAGuess(msg);

           // output.println(input.nextLine());
        }

        output.close();
        input.close();
        clientSocket.close();
    }
}
