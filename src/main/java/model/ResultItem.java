package model;
import java.util.UUID;

public class ResultItem {
    private UUID questionId;
    private boolean correct;
    private String userAnswer;
    private String expectedAnswer;

    public ResultItem(UUID id, boolean correct, String user, String expected) {
        this.questionId = id; this.correct = correct;
        this.userAnswer = user; this.expectedAnswer = expected;
    }
    public boolean isCorrect() { return correct; }
    public String getUserAnswer() { return userAnswer; }
    public String getExpectedAnswer() { return expectedAnswer; }
    public UUID getQuestionId() { return questionId; }
}