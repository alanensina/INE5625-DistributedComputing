package thread;

import sockets.Server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class ClientHandler implements Runnable{

    private InputStream clientInput;
    private Server socketServer;

    public ClientHandler(InputStream clientInput, Server socketServer) {
        this.clientInput = clientInput;
        this.socketServer = socketServer;
    }

    public void run() {
        // When a message arrives, send to everybody
        Scanner s = new Scanner(this.clientInput);
        while (s.hasNextLine()) {
            try {
                String msg = s.nextLine();
                socketServer.sendMessage(msg);
            } catch (IOException e) {
                throw new RuntimeException("Error to blah" + e.getMessage());
            }
        }
        s.close();
    }
}
