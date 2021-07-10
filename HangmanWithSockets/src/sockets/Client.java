package sockets;

import threads.Receiver;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

    private String host;
    private int port;

    public static void main(String[] args) throws UnknownHostException, IOException {
        // dispara cliente
        new Client("127.0.0.1", 12345).execute();
    }

    public Client (String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void execute() throws UnknownHostException, IOException {
        Socket clientSocket = new Socket(this.host, this.port);
        System.out.println("O clientSocket se conectou ao servidor!");

        // thread para receber mensagens do servidor
        Receiver r = new Receiver(clientSocket.getInputStream());
        new Thread(r).start();

        // lê msgs do input e manda pro servidor
        Scanner input = new Scanner(System.in);
        PrintStream output = new PrintStream(clientSocket.getOutputStream());
        while (input.hasNextLine()) {
            output.println(input.nextLine());
        }

        output.close();
        input.close();
        clientSocket.close();
    }
}
