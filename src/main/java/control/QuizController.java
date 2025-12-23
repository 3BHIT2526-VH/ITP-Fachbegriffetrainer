package control;

import Model.*;
import util.*;
import view.QuizDialog;
import java.util.Optional;
import java.io.File;

public class QuizController {

    private final StatisticsRepo statsRepo;
    private final QuizDialog view;
    private final QuestionPoolRepo poolRepo;
    private QuizSession currentSession; // Zustand der aktuellen Session

    public QuizController(StatisticsRepo statsRepo, QuizDialog view, QuestionPoolRepo poolRepo) {
        this.statsRepo = statsRepo;
        this.view = view;
        this.poolRepo = poolRepo;
        // initQuizListeners() würde hier Listener auf btnStartQuiz, btnCheck, etc. registrieren
    }

    // Logik zur Vorbereitung (Aufruf vom ITPAPP)
    public void prepareSession() {
        // Logik: Lade Poolnamen und fülle die JComboBox in der View
        // view.updatePoolSelector(poolRepo.findAllNames());
        view.showGameView(); // Wechselt von Konfig zu Quiz
    }

    // Logik für den Start-Button (registriert in initQuizListeners)
    public void startQuiz(String poolName, int maxQuestions, boolean randomOrder) {
        try {
            // 1. Einstellungen validieren (UML: validateSettings)
            QuizSettings settings = validateSettings(poolName, maxQuestions, randomOrder);

            // 2. Pool laden (Annahme: loadPoolByName() existiert im PoolRepo)
            QuestionPool selectedPool = poolRepo.load(new File(poolName + ".csv")); // Vereinfachter Ladevorgang

            // 3. Session erstellen (UML: createSession)
            currentSession = new QuizSession(selectedPool, settings);

            // 4. UI auf die erste Frage setzen
            displayNextQuestion();

        } catch (ValidationException | StorageException e) {
            // view.showError(e.getMessage());
        }
    }

    // Logik für den Prüfen-Button
    public void checkAnswer(String userAnswer) {
        if (currentSession != null && currentSession.next().isPresent()) {
            Question currentQuestion = currentSession.next().get();

            boolean correct = currentSession.answer(currentQuestion.getId(), userAnswer);

            // View aktualisieren (Feedback geben)
            // view.displayFeedback(correct);

            // Prüfung, ob Quiz fertig ist
            if (currentSession.isFinished()) {
                endQuiz();
            } else {
                // Button-Status anpassen: Prüfen -> Nächste Frage
            }
        }
    }

    // Logik für den Nächste-Frage-Button
    public void displayNextQuestion() {
        Optional<Question> nextQuestion = currentSession.next();
        if (nextQuestion.isPresent()) {
            // view.updateQuestionDisplay(nextQuestion.get());
            view.nextQuestion(); // Setzt das Eingabefeld zurück
        }
    }

    // Logik für das Ende des Quiz
    private void endQuiz() {
        QuizStatistics stats = currentSession.finish(); // UML: finish()
        // view.showResults(stats);

        // Optional: Statistiken automatisch speichern (UML: exportStats)
        try {
            // statsRepo.save(stats, new File("stats/session_" + System.currentTimeMillis() + ".dat"));
        } catch (Exception e) {
            // Fehler beim Speichern ignorieren oder protokollieren
        }
        currentSession = null;
    }

    // (Die Methode validateSettings wurde im vorherigen Schritt korrigiert)
    public QuizSettings validateSettings(String poolName, int maxQuestions, boolean randomOrder) throws ValidationException {
        // ... (Logik wie in der Korrektur beschrieben)
        int timeLimitSeconds = 60;
        return new QuizSettings(maxQuestions, timeLimitSeconds);
    }
}