package view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminDialog extends JDialog {

    // Pool Komponenten
    private JList<String> poolList;
    private JButton btnCreatePool, btnRenamePool, btnDuplicatePool, btnDeletePool;

    // Fragen Komponenten
    private JTable questionTable;
    private DefaultTableModel tableModel;
    private JTextField txtQuestionText;
    private JTextField txtImageUrl;
    private JButton btnAddQuestion, btnEditQuestion, btnDeleteQuestion;

    // Import/Export
    private JButton btnImport, btnExport;

    // Vorschau
    private JLabel previewLabel;

    // Statusanzeige (NEUE KOMPONENTE, die die View selbst besitzt und verwaltet)
    private JLabel statusMessageLabel;

    /**
     * Korrigierter Konstruktor gemäß UML-Anforderung (Owner-Parameter).
     * @param owner Der Frame, der dieser Dialog gehört.
     */
    public AdminDialog(Frame owner) {
        super(owner, "Verwaltung - Fragenpools", true); // Modal
        setSize(1000, 700);
        setLocationRelativeTo(owner);
        buildUI();
    }

    // UML: + show(): void
    public void show() {
        // 1. Packen, um sicherzustellen, dass die Komponenten die korrekte Größe haben
        this.pack();

        // 2. Erneut zentrieren (wichtig, falls der Owner seit dem Konstruktor bewegt wurde)
        this.setLocationRelativeTo(this.getOwner());

        // 3. Sichtbar machen (blockiert den EDT, da modal)
        this.setVisible(true);
    }

    // UML: + buildUI(): void
    public void buildUI() {
        setLayout(new BorderLayout());

        // --- Toolbar (Oben) ---
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        btnImport = new JButton("Import CSV");
        btnExport = new JButton("Export CSV");
        toolbar.add(btnImport);
        toolbar.add(btnExport);
        add(toolbar, BorderLayout.NORTH);

        // --- Linke Seite: Pool Liste ---
        JPanel poolPanel = new JPanel(new BorderLayout());
        poolPanel.setBorder(new TitledBorder("Fragenpools"));

        poolList = new JList<>();
        poolPanel.add(new JScrollPane(poolList), BorderLayout.CENTER);

        JPanel poolBtnPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        btnCreatePool = new JButton("Neu");
        btnRenamePool = new JButton("Umbenennen");
        btnDuplicatePool = new JButton("Duplizieren");
        btnDeletePool = new JButton("Löschen");
        poolBtnPanel.add(btnCreatePool);
        poolBtnPanel.add(btnRenamePool);
        poolBtnPanel.add(btnDuplicatePool);
        poolBtnPanel.add(btnDeletePool);
        poolPanel.add(poolBtnPanel, BorderLayout.SOUTH);

        // --- Rechte Seite: Fragen & Details (Zentrum) ---
        JPanel rightPanel = new JPanel(new BorderLayout());

        // Fragen Tabelle
        String[] columnNames = {"ID", "Frage", "Antwort", "Bild-URL"};
        tableModel = new DefaultTableModel(columnNames, 0);
        questionTable = new JTable(tableModel);
        rightPanel.add(new JScrollPane(questionTable), BorderLayout.CENTER);

        // Detail & Vorschau Bereich (Unten Rechts)
        JPanel detailPanel = new JPanel(new BorderLayout());
        detailPanel.setBorder(new TitledBorder("Fragen-Details & Vorschau"));
        detailPanel.setPreferredSize(new Dimension(0, 200));

        // Formular
        JPanel formPanel = createFormPanel(); // Auslagerung der Formularerstellung

        // Vorschau Panel
        previewLabel = new JLabel("Kein Bild", SwingConstants.CENTER);
        previewLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        previewLabel.setPreferredSize(new Dimension(150, 150));

        detailPanel.add(formPanel, BorderLayout.CENTER);
        detailPanel.add(previewLabel, BorderLayout.EAST);

        rightPanel.add(detailPanel, BorderLayout.SOUTH);

        // SplitPane zusammenfügen
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, poolPanel, rightPanel);
        splitPane.setDividerLocation(250);
        add(splitPane, BorderLayout.CENTER);

        // --- Statusanzeige (Unten) ---
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        statusMessageLabel = new JLabel("Bereit.");
        statusMessageLabel.setForeground(Color.BLACK);
        bottomPanel.add(statusMessageLabel, BorderLayout.CENTER);

        add(bottomPanel, BorderLayout.SOUTH); // Hinzufügen zum Hauptlayout (KORRIGIERT)
    }

    // Hilfsmethode zur Erstellung des Formulars (ausgelagert zur Übersichtlichkeit)
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Frage:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; txtQuestionText = new JTextField(); formPanel.add(txtQuestionText, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; formPanel.add(new JLabel("Bild-URL:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; txtImageUrl = new JTextField(); formPanel.add(txtImageUrl, gbc);

        // Buttons für Fragen
        JPanel qBtnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnAddQuestion = new JButton("Hinzufügen");
        btnEditQuestion = new JButton("Speichern");
        btnDeleteQuestion = new JButton("Löschen");
        qBtnPanel.add(btnAddQuestion);
        qBtnPanel.add(btnEditQuestion);
        qBtnPanel.add(btnDeleteQuestion);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; formPanel.add(qBtnPanel, gbc);
        return formPanel;
    }


    // --- Statusanzeige Methoden (vom Controller aufgerufen) ---

    /**
     * Zeigt eine positive Statusmeldung (z.B. nach erfolgreichem Speichern).
     * @param message Die anzuzeigende Nachricht.
     */
    public void showStatusMessage(String message) {
        statusMessageLabel.setText("Status: " + message);
        statusMessageLabel.setForeground(new Color(0, 150, 0)); // Dunkelgrün
    }

    /**
     * Zeigt eine Fehlermeldung (z.B. bei Speicher- oder Validierungsfehlern).
     * @param message Die anzuzeigende Fehlernachricht.
     */
    public void showErrorMessage(String message) {
        statusMessageLabel.setText("FEHLER: " + message);
        statusMessageLabel.setForeground(Color.RED);
    }

    // Statusanzeige (NEUE KOMPONENTE, die die View selbst besitzt und verwaltet)

    // --- Getter-Methoden (ERGÄNZT UM TEXTFELDER) ---
    public JList<String> getPoolList() { return poolList; }
    public JTable getQuestionTable() { return questionTable; }
    public DefaultTableModel getTableModel() { return tableModel; }

    // Pool-Buttons
    public JButton getBtnCreatePool() { return btnCreatePool; }
    public JButton getBtnRenamePool() { return btnRenamePool; }
    public JButton getBtnDuplicatePool() { return btnDuplicatePool; }
    public JButton getBtnDeletePool() { return btnDeletePool; }

    // Fragen-Buttons
    public JButton getBtnAddQuestion() { return btnAddQuestion; }
    public JButton getBtnEditQuestion() { return btnEditQuestion; }
    public JButton getBtnDeleteQuestion() { return btnDeleteQuestion; }

    // Import/Export Buttons
    public JButton getBtnImport() { return btnImport; }
    public JButton getBtnExport() { return btnExport; }

    public JLabel getPreviewLabel() { return previewLabel; }

    /**
     * Getter für das Textfeld der Frage.
     * Wird vom Controller benötigt,  Eingaben auszulesen.
     */
    public JTextField getTxtQuestionText() { return txtQuestionText; }

    /**
     * Getter für das Textfeld der Bild-URL.
     * Wird vom Controller benötigt, um Eingaben auszulesen.
     */
    public JTextField getTxtImageUrl() { return txtImageUrl; }
}