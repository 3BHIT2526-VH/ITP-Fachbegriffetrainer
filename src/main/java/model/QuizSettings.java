package model;

public class QuizSettings {
    private int questionCount;
    private int timeLimitSeconds;

    public QuizSettings(int questionCount, int timeLimitSeconds) {
        this.questionCount = questionCount;
        this.timeLimitSeconds = timeLimitSeconds;
    }

    public static QuizSettings defaults() {
        return new QuizSettings(10, 60);
    }

    public int getQuestionCount() { return questionCount; }
    public int getTimeLimitSeconds() { return timeLimitSeconds; }
}