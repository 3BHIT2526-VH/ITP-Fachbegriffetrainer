package Model;

import java.util.*;

public class QuestionPool {

    private final String name;
    private final List<Question> questions;

    public QuestionPool(String name) {
        this.name = name;
        this.questions = new ArrayList<>();
    }

    public QuestionPool(String name, List<Question> questions) {
        this.name = name;
        this.questions = questions;
    }

    public void add(Question q) {
        questions.add(q);
    }

    public void remove(UUID id) {
        questions.removeIf(q -> id.equals(q.id));
    }

    public Optional<Question> findById(UUID id) {
        return questions.stream().filter(q -> id.equals(q.id)).findFirst();
    }

    public int size() {
        return questions.size();
    }

    public List<Question> getQuestions() {
        return Collections.unmodifiableList(questions);
    }

    public String getName() {
        return name;
    }
}