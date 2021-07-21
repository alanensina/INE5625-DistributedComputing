package service;

import enumeration.Status;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.nio.file.Files.readAllLines;
import static model.Gibbet.*;

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

    public Status makeAGuess(String guess) {

        if (EXIT.equalsIgnoreCase(guess)) {
            return Status.EXIT;
        }

        if (this.isAlive()) {

            if (guess.length() > 1) {
                usedWords.add(guess);
                checkWord(guess);
            } else {
                usedLetters.add(guess);
                checkLetter(guess);
            }

            if (!checkArrayOfLetters() || this.finish) {
                System.out.println("Congratulations, you win!");
                System.out.println("The word was: " + this.getWord());
                return Status.SUCCESS;
            }
        }

        if (!this.isAlive()) {
            printGibbetGameOver();
            System.out.println("The word was: " + this.getWord());
            return Status.FAIL;
        }

        return Status.IN_PROGRESS;
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
        checkGibbet();
    }

    private void checkWord(String letter) {
        if (letter.equalsIgnoreCase(this.getWord())) {
            this.finish = true;
            return;
        } else {
            this.fail();
        }
        checkGibbet();
    }

    private void generateWord() {
        try {
            List<String> allWords = readAllLines(Paths.get("src/resources/" + PTBR), Charset.defaultCharset());
            this.word = allWords.get(new Random().nextInt(TOTAL_WORDS));
        } catch (IOException e) {
            throw new RuntimeException("Error to read the file.");
        }

        this.letters = new char[word.length()];

        for (int i = 0; i < word.length(); i++) {
            this.letters[i] = '_';
        }
    }

    private void checkGibbet() {
        switch (this.getAttempts()) {
            case 1:
                printGibbet1Attempts();
                break;

            case 2:
                printGibbet2Attempts();
                break;

            case 3:
                printGibbet3Attempts();
                break;

            case 4:
                printGibbet4Attempts();
                break;

            case 5:
                printGibbet5Attempts();
                break;

            case 6:
                printGibbet();
                break;

            default:
                System.out.println("Error");
                break;
        }
        printArrayOfLetters();
        printUsedWordsAndLetters();
    }

    private void printUsedWordsAndLetters() {

        if (usedWords.size() > 0) {
            System.out.print("Used words: ");
            this.usedWords.forEach(letter -> {
                System.out.print(letter + ", ");
            });
            System.out.println();
        }

        if (usedLetters.size() > 0) {
            System.out.print("Used letters: ");
            this.usedLetters.forEach(letter -> {
                System.out.print(letter + ", ");
            });
            System.out.println();
        }
    }

    public void printArrayOfLetters() {
        System.out.print("Word: ");
        for (int i = 0; i < this.letters.length; i++) {
            System.out.print(this.letters[i] + " ");
        }
        System.out.println();
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
