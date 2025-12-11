package control;

import Model.Question;
import Model.QuestionPool;
import util.QuestionPoolRepo;

import java.util.UUID;

public class AdminController {

    private final QuestionPoolRepo poolRepo;

    public AdminController(QuestionPoolRepo poolRepo) {
        this.poolRepo = poolRepo;
    }

    public QuestionPool loadFile(java.io.File file) {
        return poolRepo.load(file);
    }

    public void save(QuestionPool pool, java.io.File file) {
        poolRepo.save(pool, file);
    }

    public void addQuestion(Question question) {
        // poolRepo does not store a pool → pool must be managed externally
        // Diagram shows AdminController modifies a QuestionPool directly
        // (Pool is passed via methods)
    }

    public void deleteQuestion(UUID id) {
        // same as above: actual pool instance must come from outside
    }
}