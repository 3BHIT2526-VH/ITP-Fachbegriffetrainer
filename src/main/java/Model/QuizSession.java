package Model;

import java.util.*;

public class QuizSession {

    private final QuestionPool pool;
    private final QuizSettings settings;
    private final List<Question> sessionQuestions; // Die Fragen für diese Session
    private int currentIndex; // UML: currentIndex: int
    private int score;        // UML: score: int
    private List<ResultItem> results; // UML: results 0..*

    public QuizSession(QuestionPool pool, QuizSettings settings) {
        this.pool = pool;
        this.settings = settings;
        this.currentIndex = 0;
        this.score = 0;
        this.results = new ArrayList<>();

        // Fragen auswählen und mischen
        this.sessionQuestions = new ArrayList<>(pool.getQuestions());
        if (true /* Annahme: if settings.isRandomOrder() */) {
            Collections.shuffle(this.sessionQuestions);
        }
        // Auf die gewünschte Anzahl Fragen beschränken (UML: QuizSettings)
        if (sessionQuestions.size() > settings.getQuestionCount()) {
            sessionQuestions.subList(settings.getQuestionCount(), sessionQuestions.size()).clear();
        }
    }

    // UML: next(): Optional<Question>
    public Optional<Question> next() {
        if (currentIndex < sessionQuestions.size()) {
            return Optional.of(sessionQuestions.get(currentIndex));
        }
        return Optional.empty();
    }

    // UML: answer(UUID, String): boolean
    public boolean answer(UUID questionId, String userAnswer) {
        if (currentIndex >= sessionQuestions.size()) {
            return false; // Session beendet
        }

        Question currentQuestion = sessionQuestions.get(currentIndex);
        if (!currentQuestion.getId().equals(questionId)) {
            // Sollte nicht passieren, wenn der Controller korrekt arbeitet
            return false;
        }

        // Antwortprüfung (case-insensitive und trimmen)
        boolean correct = currentQuestion.getAnswer()
                .trim()
                .equalsIgnoreCase(userAnswer.trim());

        if (correct) {
            score++;
        }

        // Ergebnis speichern (UML: ResultItem)
        results.add(new ResultItem(questionId, correct, userAnswer, currentQuestion.getAnswer()));

        currentIndex++; // Fortschritt erhöhen
        return correct;
    }

    // UML: finish(): QuizStatistics
    public QuizStatistics finish() {
        long correctCount = results.stream().filter(ResultItem::isCorrect).count();
        int totalQuestions = sessionQuestions.size();

        int wrongCount = totalQuestions - (int) correctCount;
        double accuracy = (totalQuestions > 0) ? (double) correctCount / totalQuestions : 0.0;

        return new QuizStatistics((int) correctCount, wrongCount, accuracy);
    }

    // Hilfsmethode, um den Fortschritt im Controller zu prüfen
    public boolean isFinished() {
        return currentIndex >= sessionQuestions.size();
    }
}