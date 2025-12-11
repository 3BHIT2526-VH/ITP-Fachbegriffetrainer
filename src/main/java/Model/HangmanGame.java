package Model;

import java.util.HashSet;
import java.util.Set;

public class HangmanGame {

    private String targetWord;
    private StringBuilder revealed;
    private Set<Character> wrongGuesses;
    private int maxErrors;
    private int pool;

    public HangmanGame(int maxErrors, String targetWord) {
        this.maxErrors = maxErrors;
        this.targetWord = targetWord;

        this.revealed = new StringBuilder(targetWord.replaceAll(".", "_"));
        this.wrongGuesses = new HashSet<>();
    }

    public void start(String word) {
        this.targetWord = word;
        this.revealed = new StringBuilder(word.replaceAll(".", "_"));
        this.wrongGuesses.clear();
    }

    public void guess(char c) {
        // No logic required, UML only defines signature
    }

    public boolean isWon() {
        return false; // placeholder
    }

    public boolean isLost() {
        return false; // placeholder
    }
}