package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class QuestionPool {
    private String name;
    private List<Question> questions;

    public QuestionPool(String name) {
        this.name = name;
        this.questions = new ArrayList<>();
    }

    public QuestionPool(String name, List<Question> questions) {
        this.name = name;
        this.questions = questions;
    }

    public void add(Question question) {
        this.questions.add(question);
    }

    public void remove(UUID id) {
        questions.removeIf(q -> q.getId().equals(id));
    }

    public Optional<Question> findById(UUID id) {
        return questions.stream()
                .filter(q -> q.getId().equals(id))
                .findFirst();
    }

    public int size() {
        return questions.size();
    }

    public List<Question> getQuestions() {
        return questions;
    }
}