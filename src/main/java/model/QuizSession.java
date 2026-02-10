package model;

import java.util.*;

public class QuizSession {
    private int currentIndex = 0;
    private final List<Question> activeQuestions;
    private final Map<UUID, ResultItem> results = new HashMap<>();

    private String poolName;
    private QuizSettings settings;
    private java.time.LocalDateTime timestamp;

    public QuizSession(QuestionPool pool, QuizSettings settings) {
        this.poolName = pool.getName();
        this.settings = settings;
        this.activeQuestions = prepareQuestions(pool);
    }

    private List<Question> prepareQuestions(QuestionPool pool) {
        List<Question> list = new ArrayList<>(pool.getQuestions());
        if (settings.isShuffle()) Collections.shuffle(list);

        int limit = Math.min(list.size(), settings.getQuestionCount());
        return new ArrayList<>(list.subList(0, limit));
    }

    public boolean isAnswered(UUID id) {
        return results.containsKey(id);
    }

    public boolean wasCorrect(UUID id) {
        return results.containsKey(id) && results.get(id).isCorrect();
    }

    public String getAnswerFor(UUID id) {
        if (results.containsKey(id)) {
            return results.get(id).getUserAnswer();
        }
        return null;
    }

    public void setTemporaryAnswer(UUID id, String text) {
        if (settings.isImmediateCheck() && isAnswered(id)) {
            return;
        }
        // Wir nutzen das ResultItem Objekt auch für temporäre Speicherungen
        // "isCorrect" setzen wir erst einmal auf false oder prüfen es direkt
        results.put(id, new ResultItem(id, false, text, ""));
    }

    public boolean next() {
        if (currentIndex < activeQuestions.size() - 1) {
            currentIndex++;
            return true;
        }
        return false;
    }

    public boolean previous() {
        if (currentIndex > 0) {
            currentIndex--;
            return true;
        }
        return false;
    }

    public Optional<Question> getCurrentQuestion() {
        if (currentIndex >= 0 && currentIndex < activeQuestions.size()) {
            return Optional.of(activeQuestions.get(currentIndex));
        }
        return Optional.empty();
    }

    public Optional<Question> getQuestionAtIndex(int index) {
        if (index >= 0 && index < activeQuestions.size()) {
            return Optional.of(activeQuestions.get(index));
        }
        return Optional.empty();
    }

    public boolean answer(UUID id, String userAnswer) {
        if (settings.isImmediateCheck() && isAnswered(id)) {
            return false;
        }

        return activeQuestions.stream()
                .filter(q -> q.getId().equals(id))
                .findFirst()
                .map(q -> {
                    boolean correct = q.getAnswer().equalsIgnoreCase(userAnswer.trim());
                    results.put(id, new ResultItem(id, correct, userAnswer, q.getAnswer()));
                    return correct;
                }).orElse(false);
    }


    public void setIndex(int index) { this.currentIndex = index; }
    public int getCurrentIndex() { return currentIndex; }
    public int getTotalQuestions() { return activeQuestions.size(); }
    public String getPoolName() { return poolName; }
    public QuizSettings getSettings() { return settings; }
    public void setTimestamp(java.time.LocalDateTime ts) { this.timestamp = ts; }


    public QuizStatistics finish() {
        int correctCount = (int) results.values().stream().filter(ResultItem::isCorrect).count();
        return new QuizStatistics(correctCount, activeQuestions.size());
    }

    public QuizStatistics getStatistics() {
        int correctCount = (int) results.values().stream()
                .filter(item -> isAnswered(item.getQuestionId()) && item.isCorrect())
                .count();
        return new QuizStatistics(correctCount, activeQuestions.size());
    }
}