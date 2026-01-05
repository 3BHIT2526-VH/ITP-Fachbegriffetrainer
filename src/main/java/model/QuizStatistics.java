package model;

import java.util.List;

public class QuizStatistics {
    private int correctCount;
    private int wrongCount;
    private int accuracy; // In percentage

    public QuizStatistics(int correctCount, int wrongCount, double accuracy) {
        this.correctCount = correctCount;
        this.wrongCount = wrongCount;
        this.accuracy = (int) accuracy;
    }

    public static QuizStatistics from(List<ResultItem> results) {
        int correct = (int) results.stream().filter(ResultItem::isCorrect).count();
        int wrong = results.size() - correct;
        double acc = results.isEmpty() ? 0 : (double) correct / results.size() * 100;
        return new QuizStatistics(correct, wrong, acc);
    }
}