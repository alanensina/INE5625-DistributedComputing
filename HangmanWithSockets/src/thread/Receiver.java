package thread;

import model.Response;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class Receiver implements Runnable {

    private final ObjectInputStream server;

    public Receiver(InputStream inputStream) throws IOException {
        this.server = new ObjectInputStream(inputStream);
    }

    public void run() {
        // Receive messages from server and print
        while (true) {
            try {
                Object o = server.readObject();
                Response response = (Response) o;
                response.getMessages().forEach(System.out::println);
            } catch (Exception e) {
                try {
                    server.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }
}
