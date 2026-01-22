package util;

import model.QuestionPool;
import java.io.File;

public interface QuestionPoolRepo {
    /** Lädt einen QuestionPool aus einer Datei. */
    QuestionPool load(File file);

    /** Speichert einen QuestionPool in einer Datei. */
    void save(QuestionPool pool, File file);
}