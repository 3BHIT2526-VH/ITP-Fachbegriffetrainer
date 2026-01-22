package util;

import model.QuizStatistics;
import java.io.File;

public interface StatisticsRepo {
    /** Speichert die Quiz-Statistiken. */
    void save(QuizStatistics stats, File file);
}