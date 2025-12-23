package control;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Optional;

import view.*;
import util.*;
import Model.*;

/**
 * Die Hauptanwendungsklasse, die das System initialisiert und die Controller,
 * Views und Repositories zusammenfügt (Bootstrap).
 * Entspricht dem ITPAPP im UML-Diagramm.
 */
public class ITPAPP {

    // Referenzen auf die Views (Dialoge)
    private AdminDialog adminDialog;
    private QuizDialog quizDialog;
    private GameDialog gameDialog;

    // Referenzen auf die Controller (Interfaces oder Klassen)
    private AdminController adminController;
    private QuizController quizController;
    private GameController gameController;

    // Referenzen auf die Repositories (Interfaces, da keine Impl-Klassen im UML)
    private QuestionPoolRepo poolRepo;
    private StatisticsRepo statsRepo;

    // Das Hauptfenster
    private JFrame mainWindow;

    public static void main(String[] args) {
        // Sicherstellen, dass GUI im Event-Dispatch-Thread läuft
        SwingUtilities.invokeLater(() -> {
            try {
                // Erstelle den Datenordner, falls er nicht existiert (Hilfsfunktion)
                new java.io.File("data").mkdirs();

                new ITPAPP().start();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Fehler beim Starten: " + e.getMessage());
            }
        });
    }

    public void start() {
        // HINWEIS: Da keine Impl-Klassen im UML existieren, müssen wir hier
        // eine "Fake"-Initialisierung durchführen, um Kompilierfehler zu vermeiden.

        // 1. Repositories initialisieren (Anonyme Klassen-Stubs)

        poolRepo = new QuestionPoolRepo() {
            @Override
            public QuestionPool load(File file) {
                // KORREKTUR: Muss den Zwei-Argumente-Konstruktor verwenden
                return new QuestionPool("Mock Load Pool", new ArrayList<>());
            }
            @Override
            public void save(QuestionPool p, File f) throws StorageException { }
        };
        statsRepo = new StatisticsRepo() {
            @Override
            public Optional<QuizStatistics> load(File file) throws StorageException { return Optional.empty(); }
            @Override
            public void save(QuizStatistics s, File f) throws StorageException { }
        };

        // 2. Hauptfenster (Menu) konfigurieren
        mainWindow = new JFrame("ITP-Fachbegriffe-Trainer");
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setSize(400, 350);
        mainWindow.setLocationRelativeTo(null); // Zentrieren

        // 3. Views initialisieren (Die Dialoge benötigen das MainWindow als Owner)
        adminDialog = new AdminDialog(mainWindow);
        quizDialog = new QuizDialog(mainWindow);
        gameDialog = new GameDialog(mainWindow);

        // 4. Controller initialisieren (Verbinden von Model und View)
        initControllers();

        // 5. GUI-Inhalt für das Hauptmenü bauen
        buildMainMenu();

        // 6. Anwendung sichtbar machen
        mainWindow.setVisible(true);
    }

    /**
     * Erstellt die Controller und injiziert die Abhängigkeiten gemäß UML.
     */
    private void initControllers() {
        // Da wir AdminController als Klasse im UML haben, muss er instanziiert werden
        adminController = new AdminController(poolRepo, adminDialog);

        // Instanziierung der anderen Controller, Annahme: sie existieren als Klassen
        quizController = new QuizController(statsRepo, quizDialog, poolRepo);
        gameController = new GameController(gameDialog, poolRepo);
    }

    /**
     * Baut das UI des Hauptmenüs direkt im JFrame auf und registriert die Listener.
     */
    private void buildMainMenu() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        // Titel
        JLabel titleLabel = new JLabel("Fachbegriffe Trainer", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        gbc.gridy = 0;
        mainPanel.add(titleLabel, gbc);

        // Buttons erstellen
        JButton btnAdmin = createMenuButton("Verwaltung (Admin)");
        JButton btnQuiz = createMenuButton("Quizmodus starten");
        JButton btnGame = createMenuButton("Spielmodus (Hangman)");
        JButton btnExit = createMenuButton("Beenden");

        // Buttons zum Layout hinzufügen
        gbc.gridy = 1; mainPanel.add(btnAdmin, gbc);
        gbc.gridy = 2; mainPanel.add(btnQuiz, gbc);
        gbc.gridy = 3; mainPanel.add(btnGame, gbc);

        gbc.insets = new Insets(30, 0, 0, 0); // Abstand zum Exit
        gbc.gridy = 4; mainPanel.add(btnExit, gbc);

        mainWindow.add(mainPanel);

        // --- Event Handling (Navigation) ---

        btnAdmin.addActionListener((ActionEvent e) -> {
            adminController.refreshPoolData();
            adminDialog.show();
        });

        btnQuiz.addActionListener((ActionEvent e) -> {
            quizController.prepareSession();
            // Annahme: QuizDialog hat show()
            quizDialog.show();
        });

        btnGame.addActionListener((ActionEvent e) -> {
            gameController.startNewGame();
            // Annahme: GameDialog hat show()
            gameDialog.show();
        });

        btnExit.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });
    }

    // Hilfsmethode für einheitliches Button-Design
    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setFocusPainted(false);
        return btn;
    }
}