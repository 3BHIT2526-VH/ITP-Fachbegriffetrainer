package control;

import model.*;
import util.StatisticsRepo;
import java.io.File;
import java.util.UUID;

public class QuizController {
    private StatisticsRepo statsRepo;

    public QuizController(StatisticsRepo statisticsRepository) {
        this.statsRepo = statisticsRepository;
    }

    public void createSession(QuestionPool pool, QuizSettings settings) {
        QuizSession session = new QuizSession(pool);
        // Logic to link session to QuizDialog would go here
    }

    public boolean submitAnswers(QuizSession session, UUID id, String answer) {
        return session.answer(id, answer);
    }

    public void exportStats(QuizStatistics stats, File file) {
        // Implementation for saving stats via repo
        statsRepo.save(null, file); // Simplified for UML matching
    }
}