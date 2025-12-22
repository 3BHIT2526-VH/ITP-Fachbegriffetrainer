package control;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import view.*;

public class ITPAPP {

    // Referenzen auf die Views (Dialoge)
    private AdminDialog adminDialog;
    private QuizDialog quizDialog;
    private GameDialog gameDialog;

    // Referenzen auf die Controller
    private AdminController adminController;
    private QuizController quizController;
    private GameController gameController;

    // Referenzen auf die Repositories (Daten)
    private QuestionPoolRepo poolRepo;
    private StatisticsRepo statsRepo;

    // Das Hauptfenster (hier direkt als JFrame Instanz)
    private JFrame mainWindow;

    public static void main(String[] args) {
        // Sicherstellen, dass GUI im Event-Dispatch-Thread läuft
        SwingUtilities.invokeLater(() -> {
            try {
                new ITPAPP().start();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Fehler beim Starten: " + e.getMessage());
            }
        });
    }

    /**
     * Initialisiert die Anwendung, verknüpft MVC-Komponenten und zeigt das Hauptmenü.
     */
    public void start() {
        // 1. Repositories initialisieren (Datenhaltung)
        // Hinweis: Hier werden konkrete Implementierungen instanziiert
        poolRepo = new QuestionPoolRepoImpl(); // Mock/Impl
        statsRepo = new StatisticsRepoImpl();   // Mock/Impl

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
        // AdminController steuert den AdminDialog und nutzt das PoolRepo
        adminController = new AdminController(poolRepo, adminDialog);

        // QuizController steuert den QuizDialog, nutzt StatsRepo und PoolRepo (für Fragen)
        quizController = new QuizController(statsRepo, quizDialog, poolRepo);

        // GameController steuert den GameDialog
        // (Im Diagramm hat er keine Repo-Abhängigkeit im Konstruktor, erzeugt aber 'HangmanGame')
        gameController = new GameController(gameDialog, poolRepo);
    }

    /**
     * Baut das UI des Hauptmenüs direkt im JFrame auf.
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
            // Optional: Daten neu laden, bevor Dialog aufgeht
            adminController.refreshData();
            adminDialog.showDialog();
        });

        btnQuiz.addActionListener((ActionEvent e) -> {
            quizController.prepareSession();
            quizDialog.start();
        });

        btnGame.addActionListener((ActionEvent e) -> {
            gameController.startNewGame();
            gameDialog.start();
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

    // --- Platzhalter für Interfaces/Klassen, damit der Code ohne Errors lesbar ist ---
    // (Diese existieren in deinem Projekt in separaten Dateien)

    // Repositories
    interface QuestionPoolRepo {}
    class QuestionPoolRepoImpl implements QuestionPoolRepo {}

    interface StatisticsRepo {}
    class StatisticsRepoImpl implements StatisticsRepo {}

    // Controller Stubs (zum Verständnis der Aufrufe)
    class AdminController {
        public AdminController(QuestionPoolRepo r, AdminDialog v) {}
        public void refreshData() {}
    }
    class QuizController {
        public QuizController(StatisticsRepo s, QuizDialog v, QuestionPoolRepo p) {}
        public void prepareSession() {}
    }
    class GameController {
        public GameController(GameDialog v, QuestionPoolRepo p) {}
        public void startNewGame() {}
    }
}