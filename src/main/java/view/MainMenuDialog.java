package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainMenuDialog extends JFrame {
    private JButton btnAdmin = new JButton("Begriffe verwalten");
    private JButton btnHangman = new JButton("Hangman Modus");
    private JButton btnQuiz = new JButton("Quiz Modus");
    private JButton btnExit = new JButton("Programm beenden");

    public MainMenuDialog() {
        setTitle("ITP Fachbegriffe Trainer");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(450, 350);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Header
        JLabel lblTitle = new JLabel("ITP Trainer - Hauptmenü", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(lblTitle, BorderLayout.NORTH);

        // Center
        JPanel centerPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 60, 10, 60));
        centerPanel.add(btnAdmin);
        centerPanel.add(btnHangman);
        centerPanel.add(btnQuiz);
        add(centerPanel, BorderLayout.CENTER);

        // South
        JPanel southPanel = new JPanel();
        btnExit.setBackground(new Color(255, 64, 64));
        southPanel.add(btnExit);
        add(southPanel, BorderLayout.SOUTH);
    }

    public void setAdminListener(ActionListener l) { btnAdmin.addActionListener(l); }
    public void setHangmanListener(ActionListener l) { btnHangman.addActionListener(l); }
    public void setQuizListener(ActionListener l) { btnQuiz.addActionListener(l); }
    public void setExitListener(ActionListener l) { btnExit.addActionListener(l); }

    public void showExitConfirmation() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Möchtest du wirklich beenden?\nUngespeicherte Änderungen gehen verloren!",
                "Beenden bestätigen",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}