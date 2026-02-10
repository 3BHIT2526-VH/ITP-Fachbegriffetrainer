package model;

import java.util.HashSet;
import java.util.Set;

public class HangmanGame {
    private String targetWord;
    private StringBuilder revealed;
    private Set<Character> wrongGuesses;
    private int maxErrors;
    private boolean wordSolvedDirectly = false;

    public HangmanGame(int maxErrors, String targetWord) {
        this.maxErrors = maxErrors;
        this.targetWord = targetWord.toUpperCase();
        this.wrongGuesses = new HashSet<>();
        this.revealed = new StringBuilder();

        for (char c : this.targetWord.toCharArray()) {
            if (Character.isLetter(c)) {
                revealed.append("_");
            } else {
                revealed.append(c);
            }
        }
    }

    public void processInput(String input) {
        if (input == null || input.trim().isEmpty()) return;

        input = input.trim().toUpperCase();

        if (input.length() == 1) {
            guessLetter(input.charAt(0));
        } else {
            guessWholeWord(input);
        }
    }

    private void guessLetter(char letter) {
        if (wrongGuesses.contains(letter) || revealed.toString().indexOf(letter) >= 0) {
            return;
        }

        boolean found = false;
        for (int i = 0; i < targetWord.length(); i++) {
            if (targetWord.charAt(i) == letter) {
                revealed.setCharAt(i, letter);
                found = true;
            }
        }

        if (!found) {
            wrongGuesses.add(letter);
        }
    }

    private void guessWholeWord(String word) {
        if (word.equals(targetWord)) {
            revealed = new StringBuilder(targetWord);
            wordSolvedDirectly = true;
        } else {
            // Ein falscher Wort-Tipp zählt als ein Fehlerpunkt
            maxErrors--;
            wrongGuesses.add('?');
        }
    }

    public boolean isWon() {
        return revealed.toString().equals(targetWord) || wordSolvedDirectly;
    }

    public boolean isLost() {
        return wrongGuesses.size() >= maxErrors;
    }

    public String getRevealedWord() {
        StringBuilder display = new StringBuilder();
        for (int i = 0; i < revealed.length(); i++) {
            display.append(revealed.charAt(i)).append(" ");
        }
        return display.toString().trim();
    }

    public int getRemainingErrors() {
        return maxErrors - wrongGuesses.size();
    }

    public Set<Character> getWrongGuesses() {
        return wrongGuesses;
    }

    public String getTargetWord() {
        return targetWord;
    }
}