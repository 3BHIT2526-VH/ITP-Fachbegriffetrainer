package model;

import java.util.HashSet;
import java.util.Set;

public class HangmanGame {
    private String targetWord;
    private StringBuilder revealed;
    private Set<Character> wrongGuesses;
    private int maxErrors;
    private int pool; // As per UML

    public HangmanGame(int maxErrors, String targetWord) {
        this.maxErrors = maxErrors;
        this.targetWord = targetWord.toUpperCase();
        this.wrongGuesses = new HashSet<>();
        this.revealed = new StringBuilder("_".repeat(targetWord.length()));
    }

    public void start(String word) {
        this.targetWord = word.toUpperCase();
        this.revealed = new StringBuilder("_".repeat(word.length()));
        this.wrongGuesses.clear();
    }

    public void guess(char letter) {
        letter = Character.toUpperCase(letter);
        if (targetWord.indexOf(letter) >= 0) {
            for (int i = 0; i < targetWord.length(); i++) {
                if (targetWord.charAt(i) == letter) revealed.setCharAt(i, letter);
            }
        } else {
            wrongGuesses.add(letter);
        }
    }

    public boolean isWon() { return revealed.toString().equals(targetWord); }
    public boolean isLost() { return wrongGuesses.size() >= maxErrors; }
}