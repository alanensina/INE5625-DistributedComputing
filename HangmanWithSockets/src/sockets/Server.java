package sockets;

import threads.ClientHandler;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private int port;
    private List<PrintStream> clients;

    public static void main(String[] args) throws IOException {
        // inicia o servidor
        new Server(12345).executa();
    }

    public Server (int porta) {
        this.port = porta;
        this.clients = new ArrayList<PrintStream>();
    }

    public void executa () throws IOException {
        ServerSocket serverSocket = new ServerSocket(this.port);
        System.out.println("Porta 12345 aberta!");

        while (true) {
            // aceita um clientSocket
            Socket clientSocket = serverSocket.accept();
            System.out.println("Nova conexão com o clientSocket " +
                    clientSocket.getInetAddress().getHostAddress()
            );

            // adiciona saida do clientSocket à lista
            PrintStream ps = new PrintStream(clientSocket.getOutputStream());
            this.clients.add(ps);

            // cria tratador de clientSocket numa nova thread
            ClientHandler clientHandler = new ClientHandler(clientSocket.getInputStream(), this);
            new Thread(clientHandler).start();
        }
    }

    public void distribuiMensagem(String msg) {
        // envia msg para todo mundo
        for (PrintStream cliente : this.clients) {
            cliente.println(msg);
        }
    }
}
