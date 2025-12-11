package Model;

public class QuizSettings {

    private final int questionCount;
    private final int timeLimitSeconds;

    public QuizSettings(int questionCount, int timeLimitSeconds) {
        this.questionCount = questionCount;
        this.timeLimitSeconds = timeLimitSeconds;
    }

    public static QuizSettings defaults() {
        return new QuizSettings(10, 60);
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public int getTimeLimitSeconds() {
        return timeLimitSeconds;
    }
}