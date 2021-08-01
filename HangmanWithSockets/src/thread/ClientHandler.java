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

import static utils.Messages.*;

public class ClientHandler implements Runnable {

    private static final String EXIT = "exit";
    private static final String RESTART = "restart";

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
                new Response().setStatus(Status.WELCOME).addMessage(WELCOME_MESSAGE),
                new Response().setStatus(Status.WELCOME).addMessage(getInitialGibbet()),
                new Response().setStatus(Status.WELCOME).addMessage(this.service.buildArrayOfLetters()),
                new Response().setStatus(Status.WELCOME).addMessage(ANOTHER_GUESS_MESSAGE));

        socketServer.sendWelcomeMessage(responses);

        // When a message arrives, send to everybody
        Scanner s = new Scanner(this.clientInput);
        while (s.hasNextLine()) {
            try {
                String msg = s.nextLine();
                Response response;
                if(checkRestartMessage(msg)){
                    response = new Response().setStatus(Status.RESTART).addMessage(RESTART_MESSAGE);
                }else{
                    response = this.service.makeAGuess(msg);
                }
                socketServer.sendMessage(response);
            } catch (IOException e) {
                throw new RuntimeException("Error to send a message to socket: " + e.getMessage());
            }
        }
        s.close();
    }

    private boolean checkRestartMessage(String message){
        return RESTART.equalsIgnoreCase(message);
    }
}
