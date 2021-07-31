package thread;

import enumeration.Status;
import model.Response;
import service.GameService;
import sockets.Server;

import static model.Gibbet.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ClientHandler implements Runnable {

    private static final String EXIT = "exit";

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
        List<Response> responses = Arrays.asList(
                new Response().setStatus(Status.WELCOME).addMessage("Welcome to the Hangman"),
                new Response().setStatus(Status.WELCOME).addMessage(getInitialGibbet()),
                new Response().setStatus(Status.WELCOME).addMessage(this.service.buildArrayOfLetters()),
                new Response().setStatus(Status.WELCOME).addMessage("Insert a guess or type 'exit' if you want to quit: "));

        socketServer.sendWelcomeMessage(responses);

        // When a message arrives, send to everybody
        Scanner s = new Scanner(this.clientInput);
        while (s.hasNextLine()) {
            try {
                String msg = s.nextLine();
                Response response = this.service.makeAGuess(msg);
                socketServer.sendMessage(response);
            } catch (IOException e) {
                throw new RuntimeException("Error to send a message to socket: " + e.getMessage());
            }
        }
        s.close();
    }

}
