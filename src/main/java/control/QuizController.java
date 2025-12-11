package control;

import Model.QuestionPool;
import Model.QuizSession;
import Model.QuizSettings;
import Model.QuizStatistics;
import util.StatisticsRepo;

import java.io.File;
import java.util.UUID;

public class QuizController {

    private final StatisticsRepo statsRepo;

    public QuizController(StatisticsRepo statisticsRepository) {
        this.statsRepo = statisticsRepository;
    }

    public QuizSession createSession(QuestionPool pool, QuizSettings settings) {
        // UML shows: QuizSession ← created here
        return new QuizSession(pool, settings);
    }

    public boolean submitAnswers(QuizSession session, UUID questionId, String answer) {
        return session.answer(questionId, answer);
    }

    public void exportStats(QuizStatistics stats, File file) {
        statsRepo.save(stats, file);
    }
}