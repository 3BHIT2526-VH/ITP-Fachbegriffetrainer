package control;

import Model.HangmanGame;
import util.QuestionPoolRepo;
import view.GameDialog;
import java.util.List;
import java.util.Random;

public class GameController {

    private final GameDialog view;
    private final QuestionPoolRepo poolRepo;
    private HangmanGame currentGame;

    private final int MAX_ERRORS = 7;

    public GameController(GameDialog view, QuestionPoolRepo poolRepo) {
        this.view = view;
        this.poolRepo = poolRepo;
        // initGameListeners() würde hier Listener auf btnGuess, btnRestart etc. registrieren
    }

    // UML: newGame(QuestionPool)
    public void startNewGame() {
        // Annahme: Es wird ein zufälliger Begriff aus einem Default-Pool gewählt.
        try {
            // Dies ist vereinfachte Logik. Ein echtes System würde den PoolRepo nutzen.
            List<String> terms = List.of("Software", "Architektur", "Polymorphie", "Interface");
            String randomWord = terms.get(new Random().nextInt(terms.size()));

            currentGame = new HangmanGame(randomWord, MAX_ERRORS);

            // View initialisieren
            updateGameView();

        } catch (Exception e) {
            // view.showError("Fehler beim Starten des Spiels.");
        }
    }

    // Logik für den Raten-Button (Buchstabe oder Wort)
    public void handleGuess(String input) {
        if (currentGame == null || currentGame.isLost() || currentGame.isWon()) {
            return;
        }

        input = input.trim();
        boolean successfulGuess = false;

        if (input.length() == 1) {
            // Buchstabe geraten
            successfulGuess = currentGame.guess(input);
        } else if (input.length() > 1) {
            // Ganzes Wort geraten (UML: guessWord())
            if (currentGame.guessWord(input)) {
                // Erfolg: Spiel beenden
                // view.showSuccess("Gewonnen!");
                return;
            } else {
                // Falsches Wort = 1 Fehler (optional)
                // currentGame.wrongGuesses().add(' '); // Nur ein Platzhalter-Fehler
            }
        }

        updateGameView();
        checkGameStatus();
    }

    private void updateGameView() {
        // View aktualisieren
        // view.setSecretWordDisplay(currentGame.getRevealedWord());
        // view.updateLives(currentGame.getErrorsLeft());
        // view.refresh(); // Sorgt dafür, dass der Galgen neu gezeichnet wird
    }

    private void checkGameStatus() {
        if (currentGame.isWon()) { // UML: isWon()
            // view.showSuccess("Du hast gewonnen! Das Wort war: " + currentGame.targetWord());
        } else if (currentGame.isLost()) { // UML: isLost()
            // view.showFailure("Du hast verloren! Das Wort war: " + currentGame.targetWord());
            // view.enableRestartOnly();
        }
    }
}