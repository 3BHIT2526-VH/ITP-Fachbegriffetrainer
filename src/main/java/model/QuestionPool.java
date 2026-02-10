package model;
import java.util.*;

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

    public void add(Question q) { questions.add(q); }
    public void remove(UUID id) { questions.removeIf(q -> q.getId().equals(id)); }

    public List<Question> getQuestions() { return questions; }
    public int size() { return questions.size(); }

    public void setName(String name) { this.name = name; }
    public String getName() { return name; }

    public Optional<Question> findById(UUID id) {
        return questions.stream().filter(q -> q.getId().equals(id)).findFirst();
    }
}