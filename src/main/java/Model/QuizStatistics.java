package Model;

import java.util.List;

public class QuizStatistics {

    private final int correctCount;
    private final int wrongCount;
    private final int accuracy;

    public QuizStatistics(int correctCount, int wrongCount, double accuracy) {
        this.correctCount = correctCount;
        this.wrongCount = wrongCount;
        this.accuracy = (int) accuracy;
    }

    public static QuizStatistics from(List<ResultItem> results) {
        int correct = (int) results.stream().filter(ResultItem::isCorrect).count();
        int wrong = results.size() - correct;
        double accuracy = results.isEmpty() ? 0 : (correct * 100.0 / results.size());
        return new QuizStatistics(correct, wrong, accuracy);
    }

    public int getCorrectCount() {
        return correctCount;
    }

    public int getWrongCount() {
        return wrongCount;
    }

    public int getAccuracy() {
        return accuracy;
    }
}