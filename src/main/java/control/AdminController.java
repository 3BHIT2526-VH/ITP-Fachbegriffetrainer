package control;

import Model.Question;
import Model.QuestionPool;
import util.QuestionPoolRepo;
import util.StorageException;
import util.ValidationException;
import view.AdminDialog;

import java.io.File;
import java.util.UUID;
import java.util.Optional;

// Hinweis: Die fehlerhafte Zeile 'import util.*;' wurde entfernt, da die benötigten Klassen
// (QuestionPoolRepo, StorageException, ValidationException) jetzt explizit importiert werden.

/**
 * Controller-Klasse zur Verwaltung der Fragenpools und Fragen.
 * Implementiert die Logik des Admin-Modus und delegiert die Persistenz an das QuestionPoolRepo.
 * (Entspricht dem AdminController im UML-Diagramm ).
 */
public class AdminController {

    private final QuestionPoolRepo repo;
    private final AdminDialog view;

    // Aktueller Pool, an dem gearbeitet wird (Zustand des Controllers)
    private QuestionPool currentPool;

    /**
     * Konstruktor gemäß UML-Diagramm.
     * @param repo Das Repository für die Persistenz der Fragenpools.
     * @param view Der zugehörige AdminDialog zur Steuerung der UI.
     */
    public AdminController(QuestionPoolRepo repo, AdminDialog view) {
        this.repo = repo;
        this.view = view;
        // Hier würde initViewListeners() aufgerufen, um UI-Events (Button-Klicks) zu behandeln.
    }

    /**
     * Speichert den aktuell geladenen Fragenpool über das Repository.
     * Entspricht saveQuestionPool() im UML.
     */
    public void saveQuestionPool() {
        if (currentPool == null) {
            view.showErrorMessage("Kein Pool zum Speichern ausgewählt.");
            return;
        }

        try {
            // Annahme: Der Speicherort basiert auf dem Namen des Pools
            File saveFile = new File("data/" + currentPool.getName() + ".csv");
            repo.save(currentPool, saveFile);
            view.showStatusMessage("Pool '" + currentPool.getName() + "' erfolgreich gespeichert.");

        } catch (StorageException e) {
            view.showErrorMessage("Speicherfehler: " + e.getMessage());
        }
    }

    /**
     * Lädt einen Fragenpool anhand des Namens über das Repository.
     * Entspricht loadQuestionPool(String) im UML.
     * @param poolName Der Name des zu ladenden Pools.
     */
    public void loadQuestionPool(String poolName) {
        try {
            // Annahme: Der Ladeort basiert auf dem Namen des Pools
            File loadFile = new File("data/" + poolName + ".csv");
            this.currentPool = repo.load(loadFile);

            view.showStatusMessage("Pool '" + poolName + "' erfolgreich geladen.");
            refreshPoolData(); // UI nach dem Laden aktualisieren

        } catch (StorageException e) {
            view.showErrorMessage("Ladefehler: " + e.getMessage());
            this.currentPool = null; // Aktuellen Pool zurücksetzen
        }
    }

    /**
     * Fügt eine neue Frage zum aktuell geladenen Pool hinzu.
     * Entspricht addQuestion(Question) im UML.
     * @param question Das Question-Objekt, das hinzugefügt werden soll.
     */
    public void addQuestion(Question question) {
        if (currentPool == null) {
            view.showErrorMessage("Bitte zuerst einen Pool laden oder erstellen.");
            return;
        }

        try {
            // 1. Validierung (gemäß UML ist die Methode validate() in der Question-Hierarchie definiert)
            question.validate();

            // 2. Zum Model hinzufügen
            currentPool.add(question); // Delegiert an model.QuestionPool

            // 3. UI und Persistenz aktualisieren
            refreshPoolData();
            saveQuestionPool();
            view.showStatusMessage("Frage erfolgreich hinzugefügt.");

        } catch (ValidationException e) {
            // Die ValidationException ist im util-Diagramm definiert
            view.showErrorMessage("Validierungsfehler: " + e.getMessage());
        } catch (Exception e) {
            view.showErrorMessage("Fehler beim Hinzufügen der Frage: " + e.getMessage());
        }
    }

    /**
     * Löscht eine Frage anhand ihrer UUID aus dem aktuell geladenen Pool.
     * Entspricht deleteQuestion(UUID) im UML.
     * @param questionId Die UUID der zu löschenden Frage.
     */
    public void deleteQuestion(UUID questionId) {
        if (currentPool == null) {
            view.showErrorMessage("Kein Pool geladen.");
            return;
        }

        Optional<Question> q = currentPool.findBy(questionId);
        if (q.isEmpty()) {
            view.showErrorMessage("Frage mit ID " + questionId + " nicht gefunden.");
            return;
        }

        currentPool.remove(questionId); // Delegiert an model.QuestionPool

        // UI und Persistenz aktualisieren
        refreshPoolData();
        saveQuestionPool();
        view.showStatusMessage("Frage erfolgreich gelöscht.");
    }

    /**
     * Aktualisiert die Anzeige des Pools in der View (Tabelle, Liste).
     * Entspricht refreshPoolData() im UML.
     */
    public void refreshPoolData() {
        if (currentPool != null) {

            // 1. Fragen-Tabelle aktualisieren: Nutzung von view.getTableModel()
            //    Der Controller manipuliert das Model der JTable direkt.

            // Erst alle alten Zeilen löschen
            view.getTableModel().setRowCount(0);

            // Neue Daten hinzufügen
            for (Question q : currentPool.getQuestions()) {
                // Annahme: Die Question-Klasse hat Getter für die Tabellenspalten
                // Da Question abstrakt ist, nutzen wir die Basis-Getter (aus model.png)
                Object[] rowData = new Object[]{
                        q.getId().toString(),
                        q.getPrompt(),
                        q.getAnswer(),
                        // Für die ImageURL muss ein Cast auf ImageQuestion erfolgen,
                        // oder wir nutzen einen Hilfswert für TextQuestion
                        q instanceof Model.ImageQuestion ? ((Model.ImageQuestion) q).getImageURL() : ""
                };
                view.getTableModel().addRow(rowData);
            }

            // 2. Pool-Liste aktualisieren (Optional, da der Controller meistens nur einen Pool lädt,
            //    aber wenn die PoolList die aktuell geladenen Pools anzeigen soll, ist dies nötig):

            // Dies ist komplexer, da der Controller die Liste aller Pools kennen müsste.
            // Hier konzentrieren wir uns auf die Anzeige des NAMENS und der FRAGEN.

            // 3. Pool-Namen anzeigen (Hier muss eine Komponente im AdminDialog existieren,
            //    die den Poolnamen anzeigt, z.B. ein JLabel, das aktuell fehlt.)
            // Da wir keine Getter dafür haben, setzen wir nur einen Status.
            view.showStatusMessage("Pool '" + currentPool.getName() + "' geladen (" + currentPool.size() + " Fragen).");

        } else {
            // Pool nicht geladen: Tabelle leeren
            view.getTableModel().setRowCount(0);
            view.showStatusMessage("Kein Pool geladen.");
        }
    }
}