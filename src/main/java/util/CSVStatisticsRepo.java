package util;

import model.QuizStatistics;
import java.io.*;

public class CSVStatisticsRepo implements StatisticsRepo {
    @Override
    public void save(QuizStatistics stats, File file) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            if (stats != null) {
                pw.println("Quiz Statistik Report");
                // Hier könnten weitere Details aus QuizStatistics geschrieben werden
            }
        } catch (IOException e) {
            throw new StorageException("Export der Statistik fehlgeschlagen: " + e.getMessage());
        }
    }
}