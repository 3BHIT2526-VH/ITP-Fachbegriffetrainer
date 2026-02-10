package model;

public class QuizSettings {
    private int questionCount;
    private int timeLimitSeconds;
    private boolean shuffle;
    private boolean immediateCheck;

    public QuizSettings(int questionCount, int timeLimitSeconds, boolean shuffle, boolean immediateCheck) {
        this.questionCount = questionCount;
        this.timeLimitSeconds = timeLimitSeconds;
        this.shuffle = shuffle;
        this.immediateCheck = immediateCheck;
    }

    public static QuizSettings defaults() {
        return new QuizSettings(10, 60, false, false);
    }

    public int getQuestionCount() { return questionCount; }
    public int getTimeLimit() { return timeLimitSeconds; }
    public boolean isShuffle() { return shuffle; }
    public boolean isImmediateCheck() { return immediateCheck; }
}