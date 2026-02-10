package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

public class AdminDialog extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel cardPanel = new JPanel(cardLayout);

    // Pool-Verwaltung
    private JList<String> poolList;
    private DefaultListModel<String> poolListModel;
    private JButton btnCreatePool = new JButton("Pool erstellen");
    private JButton btnRenamePool = new JButton("Pool umbenennen");
    private JButton btnOpenPool = new JButton("Pool öffnen");
    private JButton btnBackToMain = new JButton("Hauptmenü");

    // Fragen-Verwaltung
    private JTable questionTable;
    private DefaultTableModel tableModel;
    private JTextField txtSearch = new JTextField(20);

    private JButton btnAddQuest = new JButton("Neue Frage");
    private JButton btnEditQuest = new JButton("Frage bearbeiten");
    private JButton btnDelQuest = new JButton("Frage löschen");
    private JButton btnPreview = new JButton("Vorschau (Bild)");

    // Import/Export & Navigation
    private JButton btnImport = new JButton("Import (CSV)");
    private JButton btnExport = new JButton("Export (CSV)");
    private JButton btnValidate = new JButton("Validieren");

    private JButton btnBackToPools = new JButton("Zurück");

    public AdminDialog() {
        setTitle("ITP-Master - Verwaltung");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);

        initPoolPanel();
        initQuestionPanel();

        add(cardPanel);
    }

    private void initPoolPanel() {
        JPanel pnl = new JPanel(new BorderLayout(15, 15));
        JLabel lbl = new JLabel("Fragenpool Verwaltung", SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 20));
        lbl.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
        pnl.add(lbl, BorderLayout.NORTH);

        poolListModel = new DefaultListModel<>();
        poolList = new JList<>(poolListModel);
        pnl.add(new JScrollPane(poolList), BorderLayout.CENTER);

        JPanel east = new JPanel(new BorderLayout());
        JPanel btnGrid = new JPanel(new GridLayout(5, 1, 10, 10));
        btnGrid.add(btnCreatePool);
        btnGrid.add(btnRenamePool);
        btnGrid.add(btnOpenPool);

        east.add(btnGrid, BorderLayout.NORTH);
        east.add(btnBackToMain, BorderLayout.SOUTH);
        east.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        pnl.add(east, BorderLayout.EAST);
        cardPanel.add(pnl, "POOLS");
    }

    private void initQuestionPanel() {
        JPanel pnl = new JPanel(new BorderLayout(10, 10));

        // North
        JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        northPanel.setBorder(BorderFactory.createTitledBorder("Fragen filtern"));
        northPanel.add(new JLabel("Suche:"));
        northPanel.add(txtSearch);
        pnl.add(northPanel, BorderLayout.NORTH);

        // Center
        tableModel = new DefaultTableModel(new String[]{"Frage", "Antwort", "Bild-URL"}, 0);
        questionTable = new JTable(tableModel);
        pnl.add(new JScrollPane(questionTable), BorderLayout.CENTER);

        // East
        JPanel east = new JPanel(new BorderLayout());
        east.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        JPanel actionContainer = new JPanel();
        actionContainer.setLayout(new BoxLayout(actionContainer, BoxLayout.Y_AXIS));

        JLabel lblAktionen = new JLabel("Aktionen:");
        lblAktionen.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblAktionen.setFont(new Font("Arial", Font.PLAIN, 16));

        Dimension btnDim = new Dimension(180, 50);
        setupButton(btnAddQuest, btnDim);
        setupButton(btnEditQuest, btnDim);
        setupButton(btnDelQuest, btnDim);
        setupButton(btnPreview, btnDim);
        setupButton(btnImport, btnDim);
        setupButton(btnExport, btnDim);
        setupButton(btnValidate, btnDim);

        actionContainer.add(lblAktionen);
        actionContainer.add(Box.createVerticalStrut(15));
        actionContainer.add(btnAddQuest);
        actionContainer.add(Box.createVerticalStrut(10));
        actionContainer.add(btnEditQuest);
        actionContainer.add(Box.createVerticalStrut(10));
        actionContainer.add(btnDelQuest);
        actionContainer.add(Box.createVerticalStrut(10));
        actionContainer.add(btnPreview);
        actionContainer.add(Box.createVerticalStrut(20));
        actionContainer.add(new JSeparator());
        actionContainer.add(Box.createVerticalStrut(10));
        actionContainer.add(btnImport);
        actionContainer.add(Box.createVerticalStrut(10));
        actionContainer.add(btnExport);
        actionContainer.add(Box.createVerticalStrut(10));
        actionContainer.add(btnValidate);

        east.add(actionContainer, BorderLayout.NORTH);
        east.add(btnBackToPools, BorderLayout.SOUTH);

        pnl.add(east, BorderLayout.EAST);
        cardPanel.add(pnl, "QUESTIONS");
    }

    private void setupButton(JButton btn, Dimension d) {
        btn.setMaximumSize(d);
        btn.setPreferredSize(d);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
    public void clearTable() {
        tableModel.setRowCount(0);
    }

    public void addRowToTable(Object[] rowData) {
        tableModel.addRow(rowData);
    }


    //Navigation & Listener (Interface für Controller)
    public void showPools() { cardLayout.show(cardPanel, "POOLS"); }
    public void showQuestions() { cardLayout.show(cardPanel, "QUESTIONS"); }

    public void setCreatePoolListener(ActionListener l) { btnCreatePool.addActionListener(l); }
    public void setRenamePoolListener(ActionListener l) { btnRenamePool.addActionListener(l); }
    public void setOpenPoolListener(ActionListener l) { btnOpenPool.addActionListener(l); }
    public void setBackToMainListener(ActionListener l) { btnBackToMain.addActionListener(l); }
    public void setBackToPoolsListener(ActionListener l) { btnBackToPools.addActionListener(l); }

    public void setAddQuestListener(ActionListener l) { btnAddQuest.addActionListener(l); }
    public void setEditQuestListener(ActionListener l) { btnEditQuest.addActionListener(l); }
    public void setDelQuestListener(ActionListener l) { btnDelQuest.addActionListener(l); }
    public void setPreviewListener(ActionListener l) { btnPreview.addActionListener(l); }
    public void setImportListener(ActionListener l) { btnImport.addActionListener(l); }
    public void setExportListener(ActionListener l) { btnExport.addActionListener(l); }
    public void setValidateListener(ActionListener l) { btnValidate.addActionListener(l); }

    public void setSearchListener(Runnable r) {
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { r.run(); }
            public void removeUpdate(DocumentEvent e) { r.run(); }
            public void changedUpdate(DocumentEvent e) { r.run(); }
        });
    }

    public String getSelectedPool() { return poolList.getSelectedValue(); }
    public int getSelectedQuestionRow() { return questionTable.getSelectedRow();}
    public String getSearchText() { return txtSearch.getText(); }
    public void addPoolToList(String name) { poolListModel.addElement(name); }
    public String showInputDialog(String title, String msg, String init) {
        return JOptionPane.showInputDialog(this, msg, title, JOptionPane.QUESTION_MESSAGE);
    }

    public void refreshPoolList(java.util.Set<String> names) {
        poolListModel.clear();
        names.forEach(poolListModel::addElement);
    }
}