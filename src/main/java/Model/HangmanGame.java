package Model;

import java.util.HashSet;
import java.util.Set;

public class HangmanGame {

    private final String targetWord; // UML: targetWord
    private StringBuilder revealed;  // UML: revealed: StringBuilder
    private Set<Character> wrongGuesses; // UML: wrongGuesses: Set<Character>
    private final int maxErrors; // UML: maxErrors: int
    private int pool;            // UML: pool: int (vermutlich Punktzahl)

    public HangmanGame(String word, int maxErrors) {
        this.targetWord = word.toUpperCase();
        this.maxErrors = maxErrors;
        this.wrongGuesses = new HashSet<>();
        this.pool = 0;

        // Initialisiere revealed (z.B. "_ _ _ _")
        this.revealed = new StringBuilder();
        for (int i = 0; i < targetWord.length(); i++) {
            revealed.append(targetWord.charAt(i) == ' ' ? " " : "_");
        }
    }

    // UML: guess(String)
    public boolean guess(String input) {
        input = input.toUpperCase();
        if (input.length() != 1) {
            // Wenn der Controller nur einzelne Buchstaben erlaubt, sollte dies nicht passieren
            return false;
        }

        char letter = input.charAt(0);
        boolean found = false;

        if (wrongGuesses.contains(letter) || revealed.toString().contains(String.valueOf(letter))) {
            return true; // Bereits geraten, keine Änderung
        }

        for (int i = 0; i < targetWord.length(); i++) {
            if (targetWord.charAt(i) == letter) {
                revealed.setCharAt(i, letter);
                found = true;
            }
        }

        if (!found) {
            wrongGuesses.add(letter);
        }
        return found;
    }

    // UML: guessWord(String)
    public boolean guessWord(String word) {
        return targetWord.equalsIgnoreCase(word.trim());
    }

    // UML: isWon()
    public boolean isWon() {
        return targetWord.equals(revealed.toString());
    }

    // UML: isLost()
    public boolean isLost() {
        return wrongGuesses.size() >= maxErrors;
    }

    // Getter für den Controller
    public String getRevealedWord() { return revealed.toString(); }
    public int getErrorsLeft() { return maxErrors - wrongGuesses.size(); }
    public Set<Character> getWrongGuesses() { return wrongGuesses; }
}