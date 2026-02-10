package model;

public class QuizStatistics {
    private int correct;
    private int total;

    public QuizStatistics(int correct, int total) {
        this.correct = correct;
        this.total = total;
    }

    public int getCorrectCount() {
        return correct;
    }

    public int getTotalCount() {
        return total;
    }

    public int getSuccessRate() {
        if (total == 0) return 0;
        return (int) (((double) correct / total) * 100);
    }

    public String getSummary() {
        return "Score: " + correct + "/" + total + "\nErfolgsquote: " + getSuccessRate() + "%";
    }
}