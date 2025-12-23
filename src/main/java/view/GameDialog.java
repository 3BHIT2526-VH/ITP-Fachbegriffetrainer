package view;

import javax.swing.*;
import java.awt.*;

public class GameDialog extends JDialog {
    public GameDialog(Frame owner, boolean modal) {
        super(owner, "Hangman Game", modal);
        buildUI();
    }

    public void start() {
        this.pack();
        this.setVisible(true);
    }

    public void buildUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel wordLabel = new JLabel("_ _ _ _ _");
        wordLabel.setFont(new Font("Monospaced", Font.BOLD, 24));
        gbc.gridx = 0; gbc.gridy = 0;
        add(wordLabel, gbc);

        JTextField inputField = new JTextField(5);
        gbc.gridy = 1;
        add(inputField, gbc);

        JButton guessBtn = new JButton("Guess");
        gbc.gridy = 2;
        add(guessBtn, gbc);

        setSize(300, 400);
    }

    public void refresh() {
        revalidate();
        repaint();
    }
}