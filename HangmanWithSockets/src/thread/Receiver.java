package thread;

import java.io.InputStream;
import java.util.Scanner;

public class Receiver implements Runnable{

    private InputStream server;

    public Receiver(InputStream server) {
        this.server = server;
    }

    public void run() {
        // Receive messages from server and print
        Scanner s = new Scanner(this.server);
        while (s.hasNextLine()) {
            String msg = s.nextLine();
            System.out.println(msg);
        }
    }
}
