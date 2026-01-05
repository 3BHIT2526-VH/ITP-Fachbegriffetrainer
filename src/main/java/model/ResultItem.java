package model;

import java.util.UUID;

public class ResultItem {
    private UUID questionId;
    private boolean correct;
    private String userAnswer;
    private String expectedAnswer;

    public ResultItem(UUID questionId, boolean correct, String userAnswer, String expectedAnswer) {
        this.questionId = questionId;
        this.correct = correct;
        this.userAnswer = userAnswer;
        this.expectedAnswer = expectedAnswer;
    }

    public boolean isCorrect() { return correct; }
    public UUID getQuestionId() { return questionId; }
    public String getUserAnswer() { return userAnswer; }
    public String getExpectedAnswer() { return expectedAnswer; }
}