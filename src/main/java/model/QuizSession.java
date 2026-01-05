package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class QuizSession {
    private int currentIndex = 0;
    private int score = 0;
    private QuestionPool pool;
    private List<ResultItem> results = new ArrayList<>();

    public QuizSession(QuestionPool pool) {
        this.pool = pool;
    }

    public Optional<Question> next() {
        if (currentIndex < pool.size()) {
            return Optional.of(pool.getQuestions().get(currentIndex++));
        }
        return Optional.empty();
    }

    public boolean answer(UUID id, String userAnswer) {
        return pool.findById(id).map(q -> {
            boolean isCorrect = q.getAnswer().equalsIgnoreCase(userAnswer);
            if (isCorrect) score++;
            results.add(new ResultItem(id, isCorrect, userAnswer, q.getAnswer()));
            return isCorrect;
        }).orElse(false);
    }

    public QuizStatistics finish() {
        return QuizStatistics.from(results);
    }
}