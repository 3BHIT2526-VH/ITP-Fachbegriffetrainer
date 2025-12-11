package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class QuizSession {

    private int currentIndex = 0;
    private int score = 0;

    private final QuestionPool pool;
    private final QuizSettings settings;
    private final List<ResultItem> results = new ArrayList<>();

    public QuizSession(QuestionPool pool, QuizSettings settings) {
        this.pool = pool;
        this.settings = settings;
    }

    public Optional<Question> next() {
        if (currentIndex < pool.size()) {
            return Optional.of(pool.getQuestions().get(currentIndex++));
        }
        return Optional.empty();
    }

    public boolean answer(UUID questionId, String userAnswer) {
        Optional<Question> q = pool.findById(questionId);

        if (q.isEmpty())
            return false;

        boolean correct = q.get().getAnswer().equalsIgnoreCase(userAnswer);
        if (correct) score++;

        results.add(new ResultItem(questionId, correct, userAnswer, q.get().getAnswer()));
        return correct;
    }

    public QuizStatistics finish() {
        return QuizStatistics.from(results);
    }
}