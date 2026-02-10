package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class GameDialog extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel cardPanel = new JPanel(cardLayout);

    // SETUP
    private JList<String> poolList;
    private DefaultListModel<String> poolListModel;
    private JSpinner spinMaxErrors = new JSpinner(new SpinnerNumberModel(10, 5, 15, 1));
    private JButton btnStartGame = new JButton("Spiel starten");
    private JButton btnBackToMain = new JButton("Hauptmenü");

    // HANGMAN GAME
    private JLabel lblWordDisplay = new JLabel("_ _ _ _ _ _", SwingConstants.CENTER);
    private JTextArea txtHangmanVisual = new JTextArea("Visualisierung"); // TextArea besser für ASCII-Art
    private JLabel lblAttempts = new JLabel("Verbleibende Versuche: 10", SwingConstants.CENTER);
    private JLabel lblWrongChars = new JLabel("Falsche Tipps: []", SwingConstants.CENTER);
    private JTextField txtInput = new JTextField();
    private JButton btnGuess = new JButton("Raten");
    private JButton btnRestart = new JButton("Neustart");
    private JButton btnGiveUp = new JButton("Aufgeben");
    private JButton btnBackToSetup = new JButton("Zurück");

    public GameDialog() {
        setTitle("ITP-Master - Hangman Modus");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(950, 650); // Gleiche Größe wie QuizDialog
        setLocationRelativeTo(null);

        initSetupPanel();
        initGamePanel();

        add(cardPanel);
    }

    private void initSetupPanel() {
        JPanel pnl = new JPanel(new BorderLayout(15, 15));
        pnl.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblHeader = new JLabel("Hangman - Einstellungen", SwingConstants.CENTER);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 22));
        pnl.add(lblHeader, BorderLayout.NORTH);

        poolListModel = new DefaultListModel<>();
        poolList = new JList<>(poolListModel);
        JScrollPane scroll = new JScrollPane(poolList);
        scroll.setBorder(BorderFactory.createTitledBorder("Begriffsquelle wählen"));
        pnl.add(scroll, BorderLayout.CENTER);

        JPanel east = new JPanel(new BorderLayout());
        east.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        JPanel configGrid = new JPanel(new GridLayout(10, 1, 5, 5));
        configGrid.add(new JLabel("Max. Fehlversuche:"));
        configGrid.add(spinMaxErrors);
        configGrid.add(Box.createVerticalStrut(20));

        Dimension btnDim = new Dimension(180, 45);
        setupButton(btnStartGame, btnDim);
        configGrid.add(btnStartGame);

        east.add(configGrid, BorderLayout.NORTH);
        setupButton(btnBackToMain, btnDim);
        east.add(btnBackToMain, BorderLayout.SOUTH);

        pnl.add(east, BorderLayout.EAST);
        cardPanel.add(pnl, "SETUP");
    }

    private void initGamePanel() {
        JPanel pnl = new JPanel(new BorderLayout(20, 20));
        pnl.setBorder(new EmptyBorder(20, 25, 20, 25));

        // North
        JPanel northPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        lblWordDisplay.setFont(new Font("Monospaced", Font.BOLD, 40));
        northPanel.add(lblWordDisplay);

        lblWrongChars.setFont(new Font("Arial", Font.ITALIC, 14));
        lblWrongChars.setForeground(Color.RED);
        northPanel.add(lblWrongChars);
        pnl.add(northPanel, BorderLayout.NORTH);

        // Center
        txtHangmanVisual.setFont(new Font("Monospaced", Font.PLAIN, 14));
        txtHangmanVisual.setEditable(false);
        txtHangmanVisual.setBackground(new Color(245, 245, 245));
        txtHangmanVisual.setEnabled(false);
        pnl.add(new JScrollPane(txtHangmanVisual, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,ScrollPaneConstants. HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);

        // South
        JPanel south = new JPanel(new BorderLayout(10, 10));
        south.setBorder(new EmptyBorder(10, 0, 0, 0));

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        txtInput.setPreferredSize(new Dimension(200, 35));
        txtInput.setFont(new Font("Arial", Font.BOLD, 18));

        lblAttempts.setFont(new Font("Arial", Font.BOLD, 16));

        inputPanel.add(new JLabel("Eingabe:"));
        inputPanel.add(txtInput);
        setupButton(btnGuess, new Dimension(120, 35));
        inputPanel.add(btnGuess);
        inputPanel.add(lblAttempts);

        south.add(inputPanel, BorderLayout.CENTER);
        pnl.add(south, BorderLayout.SOUTH);

        // East
        JPanel east = new JPanel(new BorderLayout());
        east.setBorder(new EmptyBorder(0, 10, 0, 0));
        JPanel btnGrid = new JPanel(new GridLayout(6, 1, 10, 10));

        Dimension d = new Dimension(180, 40);
        setupButton(btnRestart, d);
        setupButton(btnGiveUp, d);
        setupButton(btnBackToSetup, d);

        btnGrid.add(btnRestart);
        btnGrid.add(btnGiveUp);
        east.add(btnGrid, BorderLayout.NORTH);
        east.add(btnBackToSetup, BorderLayout.SOUTH);

        pnl.add(east, BorderLayout.EAST);
        cardPanel.add(pnl, "GAME");
    }

    private void setupButton(JButton btn, Dimension d) {
        btn.setPreferredSize(d);
        btn.setMaximumSize(d);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    public void updateDisplay(String revealedWord, int remaining, String wrongGuesses, String visual) {
        lblWordDisplay.setText(revealedWord);
        lblAttempts.setText("Verbleibende Versuche: " + remaining);
        lblWrongChars.setText("Falsche Tipps: " + wrongGuesses);
        txtHangmanVisual.setText(visual);
        txtInput.setText("");
        txtInput.requestFocus();
    }

    public void addPoolName(String name) {
        if (!poolListModel.contains(name)) poolListModel.addElement(name);
    }

    public String getSelectedPool() { return poolList.getSelectedValue(); }
    public int getMaxErrors() { return (int) spinMaxErrors.getValue(); }
    public String getInput() { return txtInput.getText(); }
    public JTextArea getTxtHangmanVisual() { return txtHangmanVisual; }

    public void clearInput() { txtInput.setText(""); txtInput.requestFocus(); }
    public void showSetup() { cardLayout.show(cardPanel, "SETUP"); }
    public void showGame() { cardLayout.show(cardPanel, "GAME"); }

    public void setStartGameListener(ActionListener l) { btnStartGame.addActionListener(l); }
    public void setBackToMainListener(ActionListener l) { btnBackToMain.addActionListener(l); }
    public void setGuessListener(ActionListener l) { btnGuess.addActionListener(l); }
    public void setRestartListener(ActionListener l) { btnRestart.addActionListener(l); }
    public void setGiveUpListener(ActionListener l) { btnGiveUp.addActionListener(l); }
    public void setBackToSetupListener(ActionListener l) { btnBackToSetup.addActionListener(l); }
}