package threads;

import sockets.Server;

import java.io.InputStream;
import java.util.Scanner;

public class ClientHandler implements Runnable{

    private InputStream client;
    private Server server;

    public ClientHandler(InputStream client, Server server) {
        this.client = client;
        this.server = server;
    }

    @Override
    public void run() {
        // quando chegar uma msg, distribui pra todos
        Scanner s = new Scanner(this.client);
        while (s.hasNextLine()) {
            server.distribuiMensagem(s.nextLine());
        }
        s.close();
    }
}
