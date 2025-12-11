package Model;

import java.util.UUID;

public class ResultItem {

    private final UUID questionId;
    private final boolean correct;
    private final String userAnswer;
    private final String expectedAnswer;

    public ResultItem(UUID questionId, boolean correct, String userAnswer, String expectedAnswer) {
        this.questionId = questionId;
        this.correct = correct;
        this.userAnswer = userAnswer;
        this.expectedAnswer = expectedAnswer;
    }

    public UUID getQuestionId() {
        return questionId;
    }

    public boolean isCorrect() {
        return correct;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public String getExpectedAnswer() {
        return expectedAnswer;
    }
}