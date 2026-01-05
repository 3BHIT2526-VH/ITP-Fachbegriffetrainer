package control;

import model.Question;
import model.QuestionPool;
import util.QuestionPoolRepo;
import java.io.File;
import java.util.UUID;

public class AdminController {
    private QuestionPoolRepo poolRepo;
    private QuestionPool currentPool;

    public AdminController(QuestionPoolRepo poolRepo) {
        this.poolRepo = poolRepo;
    }

    public QuestionPool load(File file) {
        this.currentPool = poolRepo.load(file);
        return currentPool;
    }

    public void save(QuestionPool pool) {
        // In a real implementation, you'd pass a File reference
        poolRepo.save(pool, new File("pool.csv"));
    }

    public void addQuestion(Question question) {
        if (currentPool != null) {
            currentPool.add(question);
        }
    }

    public void deleteQuestion(UUID id) {
        if (currentPool != null) {
            currentPool.remove(id);
        }
    }
}