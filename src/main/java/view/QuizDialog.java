package view;

import javax.swing.*;
import java.awt.*;

public class QuizDialog extends JDialog {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Config Components
    private JComboBox<String> poolSelector;
    private JSpinner spinnerTime;
    private JSpinner spinnerQuestionCount;
    private JCheckBox checkRandom;
    private JButton btnStartQuiz;

    // Quiz Components
    private JLabel lblProgress;
    private JLabel lblTimer;
    private JTextArea lblQuestionText; // TextArea für Zeilenumbruch
    private JLabel lblQuestionImage;
    private JTextField txtAnswer;
    private JButton btnCheck;
    private JButton btnNext;
    private JButton btnPause;

    public QuizDialog(Frame owner) {
        super(owner, "Quizmodus", true);
        setSize(800, 600);
        setLocationRelativeTo(owner);
        buildUI();
    }

    public void start() {
        cardLayout.show(mainPanel, "CONFIG");
        setVisible(true);
    }

    public void nextQuestion() {
        // Logik für UI-Reset (Feld leeren etc.)
        txtAnswer.setText("");
        txtAnswer.requestFocus();
    }

    public void buildUI() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // --- Karte 1: Konfiguration ---
        JPanel configPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; configPanel.add(new JLabel("Pool wählen:"), gbc);
        poolSelector = new JComboBox<>();
        gbc.gridx = 1; configPanel.add(poolSelector, gbc);

        gbc.gridx = 0; gbc.gridy = 1; configPanel.add(new JLabel("Zeitlimit (Sek):"), gbc);
        spinnerTime = new JSpinner(new SpinnerNumberModel(60, 10, 600, 10));
        gbc.gridx = 1; configPanel.add(spinnerTime, gbc);

        gbc.gridx = 0; gbc.gridy = 2; configPanel.add(new JLabel("Anzahl Fragen:"), gbc);
        spinnerQuestionCount = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
        gbc.gridx = 1; configPanel.add(spinnerQuestionCount, gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        checkRandom = new JCheckBox("Zufällige Reihenfolge");
        configPanel.add(checkRandom, gbc);

        btnStartQuiz = new JButton("Quiz Starten");
        btnStartQuiz.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx = 1; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE;
        configPanel.add(btnStartQuiz, gbc);

        // --- Karte 2: Quiz läuft ---
        JPanel gamePanel = new JPanel(new BorderLayout());

        // Header (Status)
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        lblProgress = new JLabel("Frage 1/10 | Punkte: 0");
        lblTimer = new JLabel("00:60");
        lblTimer.setFont(new Font("Monospaced", Font.BOLD, 16));
        lblTimer.setForeground(Color.RED);
        statusPanel.add(lblProgress, BorderLayout.WEST);
        statusPanel.add(lblTimer, BorderLayout.EAST);
        gamePanel.add(statusPanel, BorderLayout.NORTH);

        // Center (Frage & Bild)
        JPanel questionPanel = new JPanel(new GridBagLayout());
        lblQuestionText = new JTextArea("Hier steht die Frage...");
        lblQuestionText.setWrapStyleWord(true);
        lblQuestionText.setLineWrap(true);
        lblQuestionText.setEditable(false);
        lblQuestionText.setOpaque(false);
        lblQuestionText.setFont(new Font("Segoe UI", Font.BOLD, 18));

        lblQuestionImage = new JLabel(); // Platzhalter für Bild
        lblQuestionImage.setHorizontalAlignment(SwingConstants.CENTER);

        GridBagConstraints qGbc = new GridBagConstraints();
        qGbc.gridx = 0; qGbc.gridy = 0; qGbc.weightx = 1.0; qGbc.fill = GridBagConstraints.HORIZONTAL;
        qGbc.insets = new Insets(20, 20, 20, 20);
        questionPanel.add(lblQuestionText, qGbc);

        qGbc.gridy = 1; qGbc.weighty = 1.0; qGbc.fill = GridBagConstraints.BOTH;
        questionPanel.add(lblQuestionImage, qGbc);

        gamePanel.add(questionPanel, BorderLayout.CENTER);

        // Footer (Eingabe & Steuerung)
        JPanel controlPanel = new JPanel(new FlowLayout());
        txtAnswer = new JTextField(20);
        btnCheck = new JButton("Prüfen");
        btnNext = new JButton("Weiter");
        btnPause = new JButton("Pause");

        controlPanel.add(new JLabel("Antwort:"));
        controlPanel.add(txtAnswer);
        controlPanel.add(btnCheck);
        controlPanel.add(btnNext);
        controlPanel.add(btnPause);

        gamePanel.add(controlPanel, BorderLayout.SOUTH);

        // Karten hinzufügen
        mainPanel.add(configPanel, "CONFIG");
        mainPanel.add(gamePanel, "GAME");
        add(mainPanel);
    }

    // Methode zum Wechseln der View (vom Controller aufgerufen)
    public void showGameView() {
        cardLayout.show(mainPanel, "GAME");
    }

    // Getter
    public JButton getBtnStartQuiz() { return btnStartQuiz; }
    public JButton getBtnCheck() { return btnCheck; }
    public JButton getBtnNext() { return btnNext; }
    public JTextField getTxtAnswer() { return txtAnswer; }
    public JLabel getLblTimer() { return lblTimer; }
    // ... weitere Getter
}