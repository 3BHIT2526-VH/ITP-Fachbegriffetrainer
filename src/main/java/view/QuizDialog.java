package view;

import javax.swing.*;
import java.awt.*;

public class QuizDialog extends JDialog {
    public QuizDialog(Frame owner, boolean modal) {
        super(owner, "Quiz Session", modal);
        buildUI();
    }

    public void start() {
        this.pack();
        this.setVisible(true);
    }

    public void buildUI() {
        setLayout(new BorderLayout());

        JLabel promptLabel = new JLabel("Question prompt will appear here.", SwingConstants.CENTER);
        promptLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(promptLabel, BorderLayout.NORTH);

        JTextField answerField = new JTextField();
        add(answerField, BorderLayout.CENTER);

        JButton nextBtn = new JButton("Next Question");
        nextBtn.addActionListener(e -> nextQuestion());
        add(nextBtn, BorderLayout.SOUTH);

        setSize(400, 300);
    }

    public void nextQuestion() {
    }
}