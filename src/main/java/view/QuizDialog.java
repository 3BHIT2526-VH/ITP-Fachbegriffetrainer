package view;

import model.ImageQuestion;
import model.Question;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.List;

public class QuizDialog extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel cardPanel = new JPanel(cardLayout);

    // SETUP
    private JList<String> poolList;
    private DefaultListModel<String> poolListModel;
    private JCheckBox cbRandom = new JCheckBox("Zufällige Auswahl");
    private JCheckBox cbImmediate = new JCheckBox("Antworten sofort prüfen");
    private JSpinner spinLimit = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
    private JSpinner spinTime = new JSpinner(new SpinnerNumberModel(0, 0, 3600, 10));
    private JButton btnStartQuiz = new JButton("Quiz starten");
    private JButton btnGoToContinue = new JButton("Quiz fortsetzen");
    private JButton btnStats = new JButton("Statistiken");
    private JButton btnBackToMain = new JButton("Hauptmenü");

    // PLAY MODE
    private JLabel lblProgress = new JLabel("Frage X von Y | Zeit: 00:00");
    private JLabel lblQuestionText = new JLabel("Frage?");
    private JTextField txtAnswer = new JTextField();
    private JLabel lblImageContainer = new JLabel("Bild-Vorschau", SwingConstants.CENTER);
    private JButton btnCheck = new JButton("Prüfen");
    private JButton btnNext = new JButton("Nächste");
    private JButton btnPrev = new JButton("Vorherige");
    private JButton btnPause = new JButton("Pause/Speichern");
    private JButton btnAbort = new JButton("Abbrechen");

    // RESULT
    private JLabel lblScore = new JLabel("Ergebnis: 0 / 0 (0%)", SwingConstants.CENTER);
    private JButton btnRestart = new JButton("Neues Quiz");
    private DefaultTableModel resultTableModel;
    private JTable resultTable;

    // STATISTICS
    private JTable statsTable;
    private DefaultTableModel statsTableModel;
    private JButton btnImportStats = new JButton("Import (CSV)");
    private JButton btnExportStats = new JButton("Export (CSV)");
    private JButton btnResetStats = new JButton("Alles Löschen");
    private JButton btnBackFromStats = new JButton("Zurück");

    // SESSIONS
    private JTable sessionsTable;
    private DefaultTableModel sessionsTableModel;
    private JButton btnLoadSession = new JButton("Ausgewählte Session starten");
    private JButton btnBackFromSessions = new JButton("Zurück");

    public QuizDialog() {
        setTitle("ITP-Master - Quiz Modus");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(950, 650);
        setLocationRelativeTo(null);

        initSelectionPanel();
        initPlayPanel();
        initResultPanel();
        initStatisticsPanel();
        initSessionsPanel();

        add(cardPanel);
    }

    private void initSelectionPanel() {
        JPanel pnl = new JPanel(new BorderLayout(15, 15));
        JLabel lbl = new JLabel("Quiz-Konfiguration", SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 22));
        pnl.add(lbl, BorderLayout.NORTH);

        poolListModel = new DefaultListModel<>();
        poolList = new JList<>(poolListModel);
        pnl.add(new JScrollPane(poolList), BorderLayout.CENTER);

        JPanel east = new JPanel(new BorderLayout());
        east.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        JPanel configGrid = new JPanel(new GridLayout(12, 1, 5, 2));

        Dimension btnDim = new Dimension(180, 45);
        configGrid.add(new JLabel("Optionen:"));
        configGrid.add(cbRandom);
        configGrid.add(cbImmediate);
        configGrid.add(new JLabel("Anzahl Fragen:"));
        configGrid.add(spinLimit);
        configGrid.add(new JLabel("Zeitlimit (Sek):"));
        configGrid.add(spinTime);
        configGrid.add(Box.createVerticalStrut(10));

        setupButton(btnStartQuiz, btnDim);
        setupButton(btnGoToContinue, btnDim);
        setupButton(btnStats, btnDim);

        configGrid.add(btnStartQuiz);
        configGrid.add(btnGoToContinue);
        configGrid.add(btnStats);

        east.add(configGrid, BorderLayout.NORTH);
        east.add(btnBackToMain, BorderLayout.SOUTH);
        pnl.add(east, BorderLayout.EAST);
        cardPanel.add(pnl, "SELECTION");
    }

    private void initPlayPanel() {
        JPanel pnl = new JPanel(new BorderLayout(20, 20));
        pnl.setBorder(new EmptyBorder(20, 25, 20, 25)); // Padding für das gesamte Fenster

        lblProgress.setFont(new Font("Arial", Font.ITALIC, 14));
        pnl.add(lblProgress, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(15, 15));
        lblQuestionText.setFont(new Font("Arial", Font.BOLD, 18));
        center.add(lblQuestionText, BorderLayout.NORTH);

        lblImageContainer.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lblImageContainer.setPreferredSize(new Dimension(300, 250));
        center.add(lblImageContainer, BorderLayout.CENTER);

        // Antwortbox mit extra Padding nach links/unten
        JPanel answerBox = new JPanel(new BorderLayout(5, 5));
        answerBox.setBorder(new EmptyBorder(10, 0, 10, 0));
        answerBox.add(new JLabel("Deine Antwort:"), BorderLayout.NORTH);
        txtAnswer.setFont(new Font("Arial", Font.PLAIN, 16));
        answerBox.add(txtAnswer, BorderLayout.CENTER);
        center.add(answerBox, BorderLayout.SOUTH);

        pnl.add(center, BorderLayout.CENTER);

        JPanel east = new JPanel(new BorderLayout());
        east.setBorder(new EmptyBorder(0, 10, 0, 0)); // Abstand zwischen Center und Buttons
        JPanel btnGrid = new JPanel(new GridLayout(6, 1, 10, 10));
        Dimension d = new Dimension(180, 40);
        setupButton(btnCheck, d); setupButton(btnNext, d); setupButton(btnPrev, d); setupButton(btnPause, d);
        btnGrid.add(btnCheck); btnGrid.add(btnNext); btnGrid.add(btnPrev);
        btnGrid.add(new JSeparator()); btnGrid.add(btnPause);
        east.add(btnGrid, BorderLayout.NORTH);

        setupButton(btnAbort, d);
        east.add(btnAbort, BorderLayout.SOUTH);
        pnl.add(east, BorderLayout.EAST);

        cardPanel.add(pnl, "PLAY");
    }

    private void initSessionsPanel() {
        JPanel pnl = new JPanel(new BorderLayout(15, 15));
        JLabel lbl = new JLabel("Pausierte Quiz-Sitzungen", SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 22));
        pnl.add(lbl, BorderLayout.NORTH);

        sessionsTableModel = new DefaultTableModel(new String[]{"Datum", "Pool", "Fortschritt", "ID"}, 0);
        sessionsTable = new JTable(sessionsTableModel);
        pnl.add(new JScrollPane(sessionsTable), BorderLayout.CENTER);

        JPanel east = new JPanel(new BorderLayout());
        east.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        setupButton(btnLoadSession, new Dimension(200, 50));
        east.add(btnLoadSession, BorderLayout.NORTH);
        east.add(btnBackFromSessions, BorderLayout.SOUTH);

        pnl.add(east, BorderLayout.EAST);
        cardPanel.add(pnl, "SESSIONS");
    }

    private void initStatisticsPanel() {
        JPanel pnl = new JPanel(new BorderLayout(15, 15));
        JLabel lbl = new JLabel("Lernfortschritt", SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 22));
        pnl.add(lbl, BorderLayout.NORTH);

        statsTableModel = new DefaultTableModel(new String[]{"Datum", "Pool", "Ergebnis", "Quote"}, 0);
        statsTable = new JTable(statsTableModel);
        pnl.add(new JScrollPane(statsTable), BorderLayout.CENTER);

        JPanel east = new JPanel(new BorderLayout());
        east.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        JPanel btnGrid = new JPanel(new GridLayout(6, 1, 10, 10));
        setupButton(btnImportStats, new Dimension(180, 45));
        setupButton(btnExportStats, new Dimension(180, 45));
        setupButton(btnResetStats, new Dimension(180, 45));

        btnGrid.add(btnImportStats);
        btnGrid.add(btnExportStats);
        btnGrid.add(btnResetStats);

        east.add(btnGrid, BorderLayout.NORTH);
        east.add(btnBackFromStats, BorderLayout.SOUTH);

        pnl.add(east, BorderLayout.EAST);
        cardPanel.add(pnl, "STATISTICS");
    }

    private void initResultPanel() {
        JPanel pnl = new JPanel(new BorderLayout(20, 10));
        pnl.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Oben: Score Label
        lblScore.setFont(new Font("Arial", Font.BOLD, 22));
        pnl.add(lblScore, BorderLayout.NORTH);

        // Mitte: Tabelle mit den Ergebnissen
        String[] columns = {"Frage", "Deine Antwort", "Richtig", "Status"};
        resultTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        resultTable = new JTable(resultTableModel);

        // Spaltenbreiten etwas anpassen
        resultTable.getColumnModel().getColumn(3).setMaxWidth(80);

        JScrollPane scrollPane = new JScrollPane(resultTable);
        pnl.add(scrollPane, BorderLayout.CENTER);

        // Unten: Buttons
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        setupButton(btnRestart, new Dimension(180, 45));
        south.add(btnRestart);
        pnl.add(south, BorderLayout.SOUTH);

        cardPanel.add(pnl, "RESULT");
    }

    private void setupButton(JButton btn, Dimension d) {
        btn.setMaximumSize(d); btn.setPreferredSize(d); btn.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    // --- CONTROLLER API ---
    public void displayQuestion(Question q) {
        lblQuestionText.setText(q.getPrompt());
        txtAnswer.setText("");
        txtAnswer.setBackground(Color.WHITE);
        txtAnswer.setEditable(true);
        btnCheck.setEnabled(true);

        if (q instanceof ImageQuestion) {
            URL url = ((ImageQuestion) q).getImageUrl();
            if (url != null) {
                ImageIcon icon = new ImageIcon(url);
                Image img = icon.getImage().getScaledInstance(400, 300, Image.SCALE_SMOOTH);
                lblImageContainer.setIcon(new ImageIcon(img));
                lblImageContainer.setText("");
            } else {
                lblImageContainer.setIcon(null);
                lblImageContainer.setText("Bild konnte nicht geladen werden.");
            }
        } else {
            lblImageContainer.setIcon(null);
            lblImageContainer.setText("Kein Bild vorhanden");
        }
    }

    public void setImmediateFeedback(boolean correct, String rightAnswer) {
        if (correct) {
            txtAnswer.setBackground(new Color(200, 255, 200));
        } else {
            txtAnswer.setBackground(new Color(255, 200, 200));
            txtAnswer.setText(txtAnswer.getText() + " (Richtig: " + rightAnswer + ")");
        }
        txtAnswer.setEditable(false);
        btnCheck.setEnabled(false);
    }

    public void updateLiveProgress(int current, int total, Integer percent, String time) {
        String progressText = String.format("Frage %d von %d | Zeit: %s", current, total, time);
        if (percent != null) {
            progressText += String.format(" | Quote: %d%%", percent);
        }
        lblProgress.setText(progressText);
    }

    public void addPoolName(String name) {
        if (!poolListModel.contains(name)) poolListModel.addElement(name);
    }

    public void lockAnswerField(boolean correct, String rightAnswer) {
        if (correct) {
            txtAnswer.setBackground(new Color(200, 255, 200));
        } else {
            txtAnswer.setBackground(new Color(255, 200, 200));
            txtAnswer.setText(txtAnswer.getText() + " (Richtig: " + rightAnswer + ")");
        }
        txtAnswer.setEditable(false);
        btnCheck.setEnabled(false);
    }

    public void setPrevEnabled(boolean enabled) {
        btnPrev.setEnabled(enabled);
    }

    public void setContinueEnabled(boolean enabled) {
        btnGoToContinue.setEnabled(enabled);
    }
    public DefaultTableModel getSessionsTableModel() {
        return sessionsTableModel;
    }

    public int getSelectedSessionId() {
        int row = sessionsTable.getSelectedRow();
        if (row != -1) {
            return (int) sessionsTableModel.getValueAt(row, 3); // Spalte "ID"
        }
        return -1;
    }

    public void displayResults(String scoreText, List<Object[]> rows) {
        lblScore.setText(scoreText);
        resultTableModel.setRowCount(0); // Tabelle leeren
        for (Object[] row : rows) {
            resultTableModel.addRow(row);
        }
    }

    public void addStatsRow(Object[] row) {
        statsTableModel.addRow(row);
    }

    public String getSelectedPool() { return poolList.getSelectedValue(); }
    public boolean isShuffleSelected() { return cbRandom.isSelected(); }
    public boolean isImmediateCheckSelected() { return cbImmediate.isSelected(); }
    public int getQuestionCount() { return (int) spinLimit.getValue(); }
    public int getTimeLimit() { return (int) spinTime.getValue(); }
    public String getAnswerField() { return txtAnswer.getText().trim(); }
    public void setAnswerField(String t) { txtAnswer.setText(t); }
    public void setQuestionText(String t) { lblQuestionText.setText(t); }
    public int getSelectedSessionRow() { return sessionsTable.getSelectedRow(); }
    public DefaultTableModel getStatsTableModel() { return statsTableModel; }

    public void showSelection() { cardLayout.show(cardPanel, "SELECTION"); }
    public void showPlay() { cardLayout.show(cardPanel, "PLAY"); }
    public void showSessions() { cardLayout.show(cardPanel, "SESSIONS"); }
    public void showStatistics() { cardLayout.show(cardPanel, "STATISTICS"); }
    public void showResult() { cardLayout.show(cardPanel, "RESULT"); }

    public void setStartQuizListener(ActionListener l) { btnStartQuiz.addActionListener(l); }
    public void setGoToContinueListener(ActionListener l) { btnGoToContinue.addActionListener(l); }
    public void setStatsListener(ActionListener l) { btnStats.addActionListener(l); }
    public void setCheckListener(ActionListener l) { btnCheck.addActionListener(l); }
    public void setNextListener(ActionListener l) { btnNext.addActionListener(l); }
    public void setPrevListener(ActionListener l) { btnPrev.addActionListener(l); }
    public void setPauseListener(ActionListener l) { btnPause.addActionListener(l); }
    public void setAbortListener(ActionListener l) { btnAbort.addActionListener(l); }
    public void setRestartListener(ActionListener l) { btnRestart.addActionListener(l); }
    public void setBackFromStatsListener(ActionListener l) { btnBackFromStats.addActionListener(l); }
    public void setBackFromSessionsListener(ActionListener l) { btnBackFromSessions.addActionListener(l); }
    public void setLoadSessionListener(ActionListener l) { btnLoadSession.addActionListener(l); }
    public void setExportStatsListener(ActionListener l) { btnExportStats.addActionListener(l); }
    public void setImportStatsListener(ActionListener l) { btnImportStats.addActionListener(l); }
    public void setResetStatsListener(ActionListener l) { btnResetStats.addActionListener(l); }
    public void setBackToMainListener(ActionListener l) { btnBackToMain.addActionListener(l); }
}