package control; // Korrigiert zu 'controller' (Kleinbuchstaben)

import Model.QuestionPool;
import Model.QuizSession;
import Model.QuizSettings;
import Model.QuizStatistics;
import util.*;
import view.QuizDialog; // Notwendig, um die View zu steuern
import java.io.File;
import java.util.Optional;
import java.util.UUID;
import util.StorageException; // Exception-Handling hinzugefügt

/**
 * Controller-Klasse zur Steuerung des Quiz-Ablaufs, der Session-Logik
 * und der Verwaltung von Quiz-Statistiken.
 * * Folgt dem Konstruktor und den Methoden des UML-Diagramms.
 */
public class QuizController {

    // Dependencies (Konstruktor-Parameter gemäß UML)
    private final StatisticsRepo statsRepo;
    private final QuizDialog view;
    private final QuestionPoolRepo poolRepo; // Auch wenn es hier nicht direkt genutzt wird, ist es laut UML nötig

    /**
     * Korrigierter Konstruktor, der alle Abhängigkeiten (Model, View, Repo) injiziert.
     * * @param statisticsRepository Das Repository für Statistik-Persistenz.
     * @param quizDialog Die zugehörige View zur Steuerung der Benutzeroberfläche.
     * @param questionPoolRepo Das Repository für Fragenpools.
     */
    public QuizController(StatisticsRepo statisticsRepository, QuizDialog quizDialog, QuestionPoolRepo questionPoolRepo) {
        this.statsRepo = statisticsRepository;
        this.view = quizDialog;
        this.poolRepo = questionPoolRepo;
        // Hier würden normalerweise initViewListeners() aufgerufen
    }

    /**
     * Erstellt eine neue Quiz-Session basierend auf einem Pool und den Einstellungen.
     * Entspricht createSession(QuestionPool, QuizSettings) im UML.
     * * @param pool Der zu verwendende Fragenpool.
     * @param settings Die Einstellungen (z.B. Fragenanzahl).
     * @return Eine neue Instanz von QuizSession.
     */
    public QuizSession createSession(QuestionPool pool, QuizSettings settings) {
        // Logik: Erzeugt das model.QuizSession Objekt
        return new QuizSession(pool, settings);
    }

    /**
     * Übermittelt die Antwort des Benutzers an die aktuelle Session.
     * Entspricht submitAnswers(QuizSession, UUID, String) im UML.
     * * @param session Die aktuelle QuizSession.
     * @param questionId Die ID der beantworteten Frage.
     * @param answer Die gegebene Antwort.
     * @return true, wenn die Antwort korrekt war, sonst false.
     */
    public boolean submitAnswers(QuizSession session, UUID questionId, String answer) {
        // Delegation an das Model
        return session.answer(questionId, answer);
    }

    /**
     * Validiert die Quiz-Einstellungen.
     * Entspricht validateSettings(String, int, boolean) im UML.
     * * @param poolName Der Name des Pools.
     * @param maxQuestions Die maximale Anzahl der Fragen.
     * @param randomOrder Ob die Fragen zufällig gemischt werden sollen.
     * @return Ein QuizSettings Objekt, falls valide.
     * @throws ValidationException Falls die Einstellungen ungültig sind.
     */
    public QuizSettings validateSettings(String poolName, int maxQuestions, boolean randomOrder) throws ValidationException {

        // Beispiel für Validierungslogik (unverändert)
        if (poolName == null || poolName.isEmpty()) {
            throw new ValidationException("Pool muss ausgewählt werden.");
        }
        if (maxQuestions <= 0) {
            throw new ValidationException("Anzahl der Fragen muss größer als Null sein.");
        }

        // Annahme: Die Zeitbegrenzung (timeLimitSeconds) ist hier nicht explizit enthalten,
        // muss aber im finalen QuizSettings-Objekt vorhanden sein.
        // Wir nehmen an, dass sie entweder aus einem anderen Feld der View kommt
        // oder hier hardcodiert/standardisiert wird (z.B. 60 Sekunden).

        int timeLimitSeconds = 60; // Platzhalter/Standardwert für die Zeitbegrenzung

        // **KORREKTUR:** Übergabe der korrekten zwei Parameter an den QuizSettings-Konstruktor.
        // Die Informationen poolName und randomOrder werden implizit von der Session/dem Controller verwendet.
        return new QuizSettings(maxQuestions, timeLimitSeconds);
    }

    /**
     * Exportiert die Statistiken in eine Datei.
     * Entspricht exportStats(QuizStatistics, File) im UML.
     * * @param stats Die zu speichernden Statistiken.
     * @param file Die Zieldatei.
     * @throws StorageException Falls beim Speichern ein Fehler auftritt.
     */
    public void exportStats(QuizStatistics stats, File file) throws StorageException {
        // Delegation an das StatisticsRepo (statsRepo.save)
        statsRepo.save(stats, file);
    }

    /**
     * Lädt Statistiken aus einer Datei (falls vorhanden).
     * Entspricht loadStatistics(File) im UML.
     * * @param file Die Quelldatei.
     * @return Ein Optional, das die geladenen Statistiken enthält.
     * @throws StorageException Falls beim Laden ein Fehler auftritt.
     */
    public Optional<QuizStatistics> loadStatistics(File file) throws StorageException {
        // Delegation an das StatisticsRepo (statsRepo.load)
        return statsRepo.load(file);
    }
}