package util;

import model.QuizStatistics;
import java.io.File;
import java.util.List;

public interface StatisticsRepo {
    /**
     * @throws StorageException bei Dateizugriffsfehlern
     */
    void save(QuizStatistics stats, String poolName, File file) throws StorageException;

    /**
     * @throws StorageException bei Dateizugriffsfehlern
     */
    void saveAll(List<String[]> data, File file) throws StorageException;

    /**
     * @throws StorageException bei Dateizugriffsfehlern
     * @throws CsvFormatException wenn der Header fehlt oder falsch ist
     */
    List<String[]> load(File file) throws StorageException, CsvFormatException;
}