package view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

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

    public AdminDialog(Frame owner) {
        super(owner, "Verwaltung - Fragenpools", true); // Modal
        setSize(1000, 700);
        setLocationRelativeTo(owner);
        buildUI();
    }

    public void showDialog() {
        this.setVisible(true);
    }

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

        poolList = new JList<>(); // Controller muss Model setzen
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

        // --- Rechte Seite: Fragen & Details ---
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
    }

    // Getter
    public JList<String> getPoolList() { return poolList; }
    public JTable getQuestionTable() { return questionTable; }
    public DefaultTableModel getTableModel() { return tableModel; }
    public JButton getBtnCreatePool() { return btnCreatePool; }
    public JButton getBtnDeletePool() { return btnDeletePool; }
    public JButton getBtnImport() { return btnImport; }
    public JButton getBtnExport() { return btnExport; }
    public JLabel getPreviewLabel() { return previewLabel; }
    // ... weitere Getter für alle Buttons/Felder
}