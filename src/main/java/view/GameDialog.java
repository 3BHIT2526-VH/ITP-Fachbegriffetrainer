package view;

import javax.swing.*;
import java.awt.*;

public class GameDialog extends JDialog {

    private JPanel gallowsPanel; // Hier würde gezeichnet werden
    private JLabel lblSecretWord;
    private JLabel lblLives;
    private JLabel lblScore;
    private JTextField txtInput;
    private JButton btnGuess;
    private JButton btnRestart;
    private JButton btnGiveUp;

    public GameDialog(Frame owner) {
        super(owner, "Spielmodus - Hangman", true);
        setSize(600, 500);
        setLocationRelativeTo(owner);
        buildUI();
    }

    public void start() {
        setVisible(true);
    }

    public void refresh() {
        gallowsPanel.repaint();
    }

    public void buildUI() {
        setLayout(new BorderLayout());

        // Top: Status
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        lblLives = new JLabel("Leben: 7");
        lblLives.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblScore = new JLabel("Punkte: 0");
        topPanel.add(lblLives);
        topPanel.add(lblScore);
        add(topPanel, BorderLayout.NORTH);

        // Center: Zeichnung und Wort
        JPanel centerPanel = new JPanel(new BorderLayout());

        // Custom Panel für Galgenmännchen Zeichnung
        gallowsPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(Color.BLACK);
                g.drawString("Hier wird der Galgen gezeichnet (Controller Logic)", 50, 50);
                // Hinweis: Controller würde hier Zeichnungs-Daten bereitstellen oder 
                // dieses Panel würde auf Zustandsänderungen reagieren.
            }
        };
        centerPanel.add(gallowsPanel, BorderLayout.CENTER);

        lblSecretWord = new JLabel("_ _ _ _ _ _ _", SwingConstants.CENTER);
        lblSecretWord.setFont(new Font("Monospaced", Font.BOLD, 24));
        lblSecretWord.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        centerPanel.add(lblSecretWord, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        // Bottom: Steuerung
        JPanel bottomPanel = new JPanel(new FlowLayout());
        txtInput = new JTextField(5);
        btnGuess = new JButton("Raten");
        btnRestart = new JButton("Neustart");
        btnGiveUp = new JButton("Aufgeben");

        bottomPanel.add(new JLabel("Buchstabe/Wort:"));
        bottomPanel.add(txtInput);
        bottomPanel.add(btnGuess);
        bottomPanel.add(Box.createHorizontalStrut(20)); // Abstand
        bottomPanel.add(btnRestart);
        bottomPanel.add(btnGiveUp);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Methoden für den Controller, um UI zu aktualisieren
    public void setSecretWordDisplay(String text) {
        lblSecretWord.setText(text);
    }

    public JPanel getGallowsPanel() { return gallowsPanel; }
    public JButton getBtnGuess() { return btnGuess; }
    public JTextField getTxtInput() { return txtInput; }
    // ... weitere Getter
}