package controller;

import model.HangmanGame;
import util.HangmanVisualizer;
import model.Question;
import model.QuestionPool;
import view.GameDialog;
import javax.swing.*;
import java.util.List;
import java.util.Random;

public class GameController {
    private GameDialog view;
    private ITPAPP mainController;
    private HangmanGame currentGame;
    private String currentPoolName;
    private HangmanVisualizer visualizer;

    public GameController(ITPAPP mainController) {
        this.mainController = mainController;
        this.view = new GameDialog();
        this.visualizer = new HangmanVisualizer(view.getTxtHangmanVisual());

        if (mainController.getPools() != null) {
            mainController.getPools().keySet().forEach(view::addPoolName);
        }

        view.setStartGameListener(e -> {
            String selectedPool = view.getSelectedPool();
            if (selectedPool != null) {
                this.currentPoolName = selectedPool;
                startNewRound();
            } else {
                JOptionPane.showMessageDialog(view, "Bitte wähle zuerst einen Pool aus!");
            }
        });

        view.setGuessListener(e -> handleGuess());

        view.setRestartListener(e -> {
            if (JOptionPane.showConfirmDialog(view, "Neustart?", "Bestätigen", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                startNewRound();
        });

        view.setGiveUpListener(e -> {
            if (currentGame != null) {
                JOptionPane.showMessageDialog(view, "Lösung: " + currentGame.getTargetWord());
                view.showSetup();
            }
        });

        view.setBackToSetupListener(e -> view.showSetup());
        view.setBackToMainListener(e -> { view.dispose(); mainController.showMainMenu(); });
        view.setVisible(true);
    }

    private void startNewRound() {
        QuestionPool pool = mainController.getPools().get(currentPoolName);
        if (pool == null || pool.getQuestions().isEmpty()) return;

        List<Question> questions = pool.getQuestions();
        String target = questions.get(new Random().nextInt(questions.size())).getAnswer();

        this.currentGame = new HangmanGame(view.getMaxErrors(), target);

        visualizer.reset();

        updateUI(false);
        view.showGame();
    }

    private void handleGuess() {
        if (currentGame == null || currentGame.isWon() || currentGame.isLost()) return;

        String input = view.getInput().trim().toUpperCase();

        if (input.isEmpty()) return;

        if (input.length() != 1 && input.length() != currentGame.getTargetWord().length()) {
            JOptionPane.showMessageDialog(view, "Ungültige Eingabe! Gib entweder einen einzelnen Buchstaben " +
                            "oder das komplette Wort mit " + currentGame.getTargetWord().length() + " Zeichen ein.",
                    "Hinweis", JOptionPane.WARNING_MESSAGE);
            view.clearInput();
            return;
        }

        int oldErrorCount = currentGame.getWrongGuesses().size();
        currentGame.processInput(input);
        int newErrorCount = currentGame.getWrongGuesses().size();

        updateUI(newErrorCount > oldErrorCount);

        if (currentGame.isWon()) {
            JOptionPane.showMessageDialog(view, "Sieg! Wort: " + currentGame.getTargetWord());
            view.showSetup();
        } else if (currentGame.isLost()) {
            JOptionPane.showMessageDialog(view, "Game Over! Lösung: " + currentGame.getTargetWord());
            view.showSetup();
        }
    }

    private void updateUI(boolean errorChanged) {
        String revealed = currentGame.getRevealedWord();
        int remaining = currentGame.getRemainingErrors();
        String wrong = currentGame.getWrongGuesses().toString();

        view.updateDisplay(revealed, remaining, wrong, view.getTxtHangmanVisual().getText());

        if (errorChanged) {
            visualizer.updateVisual(currentGame.getWrongGuesses().size(), view.getMaxErrors());
        }
    }
}