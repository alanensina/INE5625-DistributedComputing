package service;

import enumeration.Status;
import model.Response;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.nio.file.Files.readAllLines;
import static model.Gibbet.*;
import static utils.Messages.*;

public class GameService {

    private final int TOTAL_WORDS = 50;
    private final String EN = "words-en.txt";
    private final String PTBR = "words-pt.txt";
    private static final String EXIT = "exit";

    private char[] letters;
    private boolean finish = false;
    private List<String> usedLetters = new ArrayList<>();
    private List<String> usedWords = new ArrayList<>();
    private String word;
    private int attempts = 6;
    private boolean isAlive = true;

    public void startGame() {
        this.generateWord();
    }

    public Response makeAGuess(String guess) {

        Response response = new Response();

        if (EXIT.equalsIgnoreCase(guess)) {
            return response
                    .setStatus(Status.EXIT)
                    .addMessage(EXIT_GAME_MESSAGE);
        }

        if (this.isAlive()) {
            if (guess.length() > 1) {
                usedWords.add(guess);
                checkWord(guess);
            } else {
                usedLetters.add(guess);
                checkLetter(guess);
            }

            response.addMessages(
                    getGibbet(),
                    buildArrayOfLetters(),
                    buildUsedWordsAndLetters()
            );

            if (!checkArrayOfLetters() || this.finish) {
                return response
                        .setStatus(Status.SUCCESS)
                        .addMessages(
                                CONGRATULATIONS_WIN_MESSAGE,
                                String.format(THE_WORLD_IS_MESSAGE, this.getWord())
                        );
            }
        }

        if (!this.isAlive()) {
            return response
                    .setStatus(Status.FAIL)
                    .addMessages(
                            getGibbetGameOver(),
                            String.format(THE_WORLD_IS_MESSAGE, this.getWord()),
                            FAILED_GAME_MESSAGE
                    );
        }

        return response
                .setStatus(Status.IN_PROGRESS)
                .addMessage(ANOTHER_GUESS_MESSAGE);
    }

    public boolean isAlive() {
        return this.isAlive;
    }

    private boolean checkArrayOfLetters() {
        for (int i = 0; i < this.letters.length; i++) {
            if (this.letters[i] == '_') {
                return true;
            }
        }

        return false;
    }

    private void checkLetter(String letter) {
        boolean aux = false;
        char character = letter.charAt(0);

        for (int i = 0; i < this.getWord().length(); i++) {
            if (character == this.getWord().charAt(i)) {
                aux = true;
                this.letters[i] = character;
            }
        }

        if (!aux) {
            this.fail();
        }
    }

    private void checkWord(String letter) {
        if (letter.equalsIgnoreCase(this.getWord())) {
            this.finish = true;
        } else {
            this.fail();
        }
    }

    private void generateWord() {
        try {
            List<String> allWords = readAllLines(Paths.get("src/resources/" + EN), Charset.defaultCharset());
            this.word = allWords.get(new Random().nextInt(TOTAL_WORDS));
        } catch (IOException e) {
            throw new RuntimeException("Error to read the file.");
        }

        this.letters = new char[word.length()];

        for (int i = 0; i < word.length(); i++) {
            this.letters[i] = '_';
        }
    }

    private String getGibbet() {
        switch (this.getAttempts()) {
            case 1:
                return getGibbet1Attempts();

            case 2:
                return getGibbet2Attempts();
            case 3:
                return getGibbet3Attempts();
            case 4:
                return getGibbet4Attempts();
            case 5:
                return getGibbet5Attempts();
            case 6:
                return getInitialGibbet();
            default:
                System.out.println("Game finished!");
                break;
        }
        return null;
    }

    private String buildUsedWordsAndLetters() {
        StringBuilder message = new StringBuilder();

        if (usedWords.size() > 0) {
            message.append(USED_WORDS_MESSAGE);
            this.usedWords.forEach(letter -> message.append(letter).append(", "));
            message.append("\n");
        }

        if (usedLetters.size() > 0) {
            message.append(USED_LETTERS_MESSAGE);
            this.usedLetters.forEach(letter -> message.append(letter).append(", "));
            message.append("\n");
        }

        return message.toString();
    }

    public String buildArrayOfLetters() {
        StringBuilder message = new StringBuilder();
        message.append(WORDS_LETTERS_MESSAGE);
        for (char letter : this.letters) {
            message.append(letter).append(" ");
        }
        return message.toString();
    }

    private void fail() {
        this.attempts--;
        this.isAlive = this.getAttempts() > 0;
    }

    public String getWord() {
        return word;
    }

    public int getAttempts() {
        return attempts;
    }

}
