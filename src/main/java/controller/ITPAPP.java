package controller;

import model.QuestionPool;
import view.MainMenuDialog;
import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class ITPAPP {
    private MainMenuDialog menuView;
    // Zentrale Speicherung aller Pools!
    private Map<String, QuestionPool> pools = new HashMap<>();

    public ITPAPP() {
        showMainMenu();
    }
    public Map<String, QuestionPool> getPools() {
        return pools;
    }

    public void showMainMenu() {
        if (menuView == null) {
            menuView = new MainMenuDialog();

            menuView.setAdminListener(e -> openAdmin());
            menuView.setHangmanListener(e -> openHangman());
            menuView.setQuizListener(e -> openQuiz());
            menuView.setExitListener(e -> menuView.showExitConfirmation());
        }
        menuView.setVisible(true);
    }

    private void openAdmin() {
        menuView.setVisible(false);
        new AdminController(this);
    }

    private void openQuiz() {
        menuView.setVisible(false);
        new QuizController(this);
    }

    private void openHangman() {
        menuView.setVisible(false);
        new GameController(this);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(ITPAPP::new);
    }
}