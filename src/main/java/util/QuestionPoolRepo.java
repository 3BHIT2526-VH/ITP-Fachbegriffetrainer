package util;

import model.QuestionPool;
import java.io.File;

public interface QuestionPoolRepo {
    /**
     * @throws StorageException bei Dateizugriffsfehlern
     * @throws CsvFormatException wenn es Fehler bei der Formatierung gibt
     */
    QuestionPool load(File file) throws StorageException, CsvFormatException;

    /**
     * @throws StorageException bei Dateizugriffsfehlern
     */
    void save(QuestionPool pool, File file) throws StorageException;
}