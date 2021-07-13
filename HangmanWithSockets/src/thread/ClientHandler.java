package thread;

import service.GameService;
import sockets.Server;

import static model.Gibbet.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class ClientHandler implements Runnable{

    private InputStream clientInput;
    private Server socketServer;
    private GameService service;

    public ClientHandler(InputStream clientInput, Server socketServer) {
        this.clientInput = clientInput;
        this.socketServer = socketServer;
        this.service = new GameService();
        this.service.startGame();
    }

    public void run() {
        System.out.println("Welcome to the Hangman!");
        printGibbet();
        this.service.printArrayOfLetters();
        System.out.println("Insert a letter: ");

        // When a message arrives, send to everybody
        Scanner s = new Scanner(this.clientInput);
        while (s.hasNextLine()) {
           // try {
                String msg = s.nextLine();
                this.service.makeAGuess(msg);



//                socketServer.sendMessage(msg);
//            } catch (IOException e) {
//                throw new RuntimeException("Error to send a message to socket: " + e.getMessage());
//            }
        }
        s.close();
    }
}
