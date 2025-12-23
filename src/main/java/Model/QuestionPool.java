package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class QuestionPool {

    private String name;
    private List<Question> questions;

    public QuestionPool(String name, List<Question> questions) {
        this.name = name;
        this.questions = new ArrayList<>();
    }

    // UML: add(Question)
    public void add(Question question) {
        this.questions.add(question);
    }

    // UML: remove(UUID)
    public void remove(UUID questionId) {
        this.questions.removeIf(q -> q.getId().equals(questionId));
    }

    // UML: findBy(UUID): Optional<Question>
    public Optional<Question> findBy(UUID questionId) {
        return questions.stream()
                .filter(q -> q.getId().equals(questionId))
                .findFirst();
    }

    // UML: size(): int
    public int size() {
        return questions.size();
    }

    // Getter
    public List<Question> getQuestions() { return questions; }
    public String getName() { return name; }
}