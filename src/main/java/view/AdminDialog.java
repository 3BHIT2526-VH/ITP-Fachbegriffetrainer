package view;

import javax.swing.*;
import java.awt.*;

public class AdminDialog extends JDialog {
    public AdminDialog(Frame owner, boolean modal) {
        super(owner, "Admin - Question Management", modal);
        buildUI();
    }

    public void showDialog() {
        this.pack();
        this.setLocationRelativeTo(getOwner());
        this.setVisible(true);
    }

    public void buildUI() {
        setLayout(new BorderLayout());

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> questionList = new JList<>(listModel);
        add(new JScrollPane(questionList), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(new JButton("Load Pool"));
        buttonPanel.add(new JButton("Save Pool"));
        buttonPanel.add(new JButton("Add Question"));
        buttonPanel.add(new JButton("Delete Question"));

        add(buttonPanel, BorderLayout.SOUTH);
        setSize(500, 400);
    }
}