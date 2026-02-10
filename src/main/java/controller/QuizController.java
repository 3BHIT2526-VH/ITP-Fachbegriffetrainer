package controller;

import model.*;
import util.CSVStatisticsRepo;
import util.CsvFormatException;
import util.StatisticsRepo;
import util.StorageException;
import view.QuizDialog;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class QuizController {
    private QuizDialog view;
    private ITPAPP mainController;
    private QuizSession currentSession;
    private QuizSettings currentSettings;

    private List<QuizSession> pausedSessions = new ArrayList<>();

    private Timer quizTimer;
    private int secondsElapsed;

    public QuizController(ITPAPP mainController) {
        this.mainController = mainController;
        this.view = new QuizDialog();

        if (mainController.getPools() != null) {
            mainController.getPools().keySet().forEach(view::addPoolName);
        }

        view.setStartQuizListener(e -> {
            String selected = view.getSelectedPool();
            if (selected != null) startNewQuiz(selected);
            else JOptionPane.showMessageDialog(view, "Bitte Pool wählen!");
        });

        updateContinueButtonState();
        view.setGoToContinueListener(e -> {
            refreshSessionsTable();
            view.showSessions();
        });

        view.setLoadSessionListener(e -> {
            int row = view.getSelectedSessionRow();
            if (row != -1) {
                resumeSession(row);
            } else {
                JOptionPane.showMessageDialog(view, "Bitte eine Session auswählen!");
            }
        });

        view.setBackFromSessionsListener(e -> view.showSelection());

        view.setStatsListener(e -> view.showStatistics());
        view.setBackFromStatsListener(e -> view.showSelection());
        view.setBackToMainListener(e -> { stopTimer(); view.dispose(); mainController.showMainMenu(); });

        view.setCheckListener(e -> handleCheck());

        view.setNextListener(e -> {
            if (currentSession == null) return;

            // Falls Sofort-Prüfen an ist UND noch nicht auf "Prüfen" geklickt wurde:
            if (currentSettings.isImmediateCheck() && !currentSession.isAnswered(currentSession.getCurrentQuestion().get().getId())) {
                handleCheck();

                // Optional: Kleiner Delay oder Hinweis
            } else {
                saveCurrentAnswerState();
            }

            if (currentSession.next()) {
                updateQuestionUI();
            } else {
                finishQuiz();
            }
        });

        view.setPrevListener(e -> {
            saveCurrentAnswerState();
            if (currentSession != null && currentSession.previous()) updateQuestionUI();
        });

        view.setPauseListener(e -> {
            if (currentSession != null) {
                stopTimer();
                currentSession.setTimestamp(java.time.LocalDateTime.now());
                pausedSessions.add(currentSession);

                updateContinueButtonState();
                JOptionPane.showMessageDialog(view, "Quiz pausiert und gespeichert.");
                view.showSelection();
            }
        });

        view.setAbortListener(e -> {
            if (JOptionPane.showConfirmDialog(view, "Abbrechen?", "Stop", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                stopTimer();
                view.showSelection();
            }
        });

        view.setRestartListener(e -> view.showSelection());

        view.setStatsListener(e -> {
            view.showStatistics();
        });

        view.setImportStatsListener(e -> handleImportStats());
        view.setExportStatsListener(e -> handleExportStats());

        view.setResetStatsListener(e -> {
            if (JOptionPane.showConfirmDialog(view, "Alle Einträge aus der Anzeige löschen?",
                    "Löschen", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                view.getStatsTableModel().setRowCount(0);
            }
        });

        view.setVisible(true);
    }

    private void startNewQuiz(String poolName) {
        QuestionPool pool = mainController.getPools().get(poolName);
        currentSettings = new QuizSettings(
                view.getQuestionCount(), view.getTimeLimit(),
                view.isShuffleSelected(), view.isImmediateCheckSelected()
        );

        this.currentSession = new QuizSession(pool, currentSettings);
        secondsElapsed = 0;
        startTimer();
        view.showPlay();
        updateQuestionUI();
    }

    private void startTimer() {
        if (quizTimer != null) quizTimer.stop();
        quizTimer = new Timer(1000, e -> {
            secondsElapsed++;
            updateTimerDisplay();

            // Zeitlimit
            if (currentSettings.getTimeLimit() > 0 && secondsElapsed >= currentSettings.getTimeLimit()) {
                stopTimer();
                JOptionPane.showMessageDialog(view, "Zeit abgelaufen!");
                finishQuiz();
            }
        });
        quizTimer.start();
    }

    private void stopTimer() {
        if (quizTimer != null) quizTimer.stop();
    }

    private void updateTimerDisplay() {
        int mins = secondsElapsed / 60;
        int secs = secondsElapsed % 60;
        String timeStr = String.format("%02d:%02d", mins, secs);

        Integer quote = null;
        if (currentSettings.isImmediateCheck()) {
            quote = currentSession.getStatistics().getSuccessRate();
        }

        view.updateLiveProgress(
                currentSession.getCurrentIndex() + 1,
                currentSession.getTotalQuestions(),
                quote,
                timeStr
        );
    }

    private void updateQuestionUI() {
        currentSession.getCurrentQuestion().ifPresent(q -> {
            view.displayQuestion(q);

            String savedAnswer = currentSession.getAnswerFor(q.getId());
            view.setAnswerField(savedAnswer != null ? savedAnswer : "");

            if (currentSettings.isImmediateCheck()) {
                view.setPrevEnabled(false);

                if (currentSession.isAnswered(q.getId())) {
                    boolean wasCorrect = currentSession.wasCorrect(q.getId());
                    view.lockAnswerField(wasCorrect, q.getAnswer());
                }
            } else {
                view.setPrevEnabled(currentSession.getCurrentIndex() > 0);
            }

            updateTimerDisplay();
        });
    }

    private void saveCurrentAnswerState() {
        currentSession.getCurrentQuestion().ifPresent(q -> {
            String input = view.getAnswerField();

            currentSession.setTemporaryAnswer(q.getId(), input);
        });
    }

    private void handleCheck() {
        currentSession.getCurrentQuestion().ifPresent(q -> {
            String answer = view.getAnswerField();

            boolean correct = currentSession.answer(q.getId(), answer);

            if (currentSettings.isImmediateCheck()) {
                view.lockAnswerField(correct, q.getAnswer());
                updateTimerDisplay();
            } else {
                JOptionPane.showMessageDialog(view, "Antwort wurde für die Auswertung übernommen.");
            }
        });
    }

    private void finishQuiz() {
        stopTimer();
        saveCurrentAnswerState();

        List<Object[]> resultRows = new ArrayList<>();

        for (int i = 0; i < currentSession.getTotalQuestions(); i++) {
            final int index = i;
            currentSession.getQuestionAtIndex(index).ifPresent(q -> {
                String userAnswer = currentSession.getAnswerFor(q.getId());
                if (userAnswer == null) userAnswer = "";

                boolean correct;
                if (currentSession.isAnswered(q.getId())) {
                    correct = currentSession.wasCorrect(q.getId());
                } else {
                    correct = currentSession.answer(q.getId(), userAnswer);
                }

                resultRows.add(new Object[]{
                        q.getPrompt(),
                        userAnswer,
                        q.getAnswer(),
                        correct ? "✅" : "❌"
                });
            });
        }

        QuizStatistics stats = currentSession.finish();

        String scoreText = String.format("Ergebnis: %d / %d (%d%%)",
                stats.getCorrectCount(), stats.getTotalCount(), stats.getSuccessRate());

        view.displayResults(scoreText, resultRows);
        view.showResult();

        String date = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        view.addStatsRow(new Object[]{
                date,
                currentSession.getPoolName(),
                stats.getCorrectCount() + " / " + stats.getTotalCount(),
                stats.getSuccessRate() + "%"
        });

        int choice = JOptionPane.showConfirmDialog(view, "Quiz beendet!\n" + "Richtig: " + stats.getCorrectCount() + " von " +
                stats.getTotalCount() + "\n" + "Quote: " + stats.getSuccessRate() + "%" +
                "\nMöchten Sie dieses Ergebnis in eine CSV-Datei speichern?", "Speichern", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            saveStatsToCSV(stats);
        }
    }

    private void updateContinueButtonState() {
        view.setContinueEnabled(!pausedSessions.isEmpty());
    }

    private void refreshSessionsTable() {
        DefaultTableModel model = view.getSessionsTableModel();
        model.setRowCount(0);

        for (int i = 0; i < pausedSessions.size(); i++) {
            QuizSession s = pausedSessions.get(i);
            model.addRow(new Object[]{
                    "Sitzung " + (i + 1),
                    s.getPoolName(),
                    (s.getCurrentIndex() + 1) + " / " + s.getTotalQuestions(),
                    i
            });
        }
    }

    private void resumeSession(int index) {
        this.currentSession = pausedSessions.get(index);
        this.currentSettings = currentSession.getSettings();

        pausedSessions.remove(index);
        updateContinueButtonState();

        // TODO/Optional: Timer müsste man ggf. auch in der Session speichern
        // secondsElapsed = currentSession.getSavedSeconds();
        startTimer();
        view.showPlay();
        updateQuestionUI();
    }

    private void saveStatsToCSV(QuizStatistics stats) {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (!file.getName().endsWith(".csv")) {
                file = new File(file.getAbsolutePath() + ".csv");
            }

            try {
                CSVStatisticsRepo repo = new CSVStatisticsRepo();
                repo.save(stats, currentSession.getPoolName(), file);
                JOptionPane.showMessageDialog(view, "Statistik wurde erfolgreich gespeichert.");
            } catch (StorageException e) {
                JOptionPane.showMessageDialog(view, "Fehler beim Speichern: " + e.getMessage(),
                        "Dateifehler", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleImportStats() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();

            try {
                StatisticsRepo repo = new CSVStatisticsRepo();
                List<String[]> newData = repo.load(file);

                DefaultTableModel model = view.getStatsTableModel();
                for (String[] row : newData) {
                    model.addRow(row);
                }

                JOptionPane.showMessageDialog(view, newData.size() + " Einträge erfolgreich hinzugefügt.");

            } catch (CsvFormatException e) {
                JOptionPane.showMessageDialog(view, "CSV-Formatfehler: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            } catch (StorageException e) {
                JOptionPane.showMessageDialog(view, "Dateifehler: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleExportStats() {
        DefaultTableModel model = view.getStatsTableModel();
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(view, "Keine Daten zum Exportieren vorhanden!");
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Statistik exportieren");
        if (chooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".csv")) {
                file = new File(file.getAbsolutePath() + ".csv");
            }

            try {
                List<String[]> exportData = new ArrayList<>();
                for (int i = 0; i < model.getRowCount(); i++) {
                    String[] row = {
                            model.getValueAt(i, 0).toString(),
                            model.getValueAt(i, 1).toString(),
                            model.getValueAt(i, 2).toString(),
                            model.getValueAt(i, 3).toString()
                    };
                    exportData.add(row);
                }

                StatisticsRepo repo = new CSVStatisticsRepo();
                repo.saveAll(exportData, file);

                JOptionPane.showMessageDialog(view, "Export erfolgreich!");

            } catch (StorageException ex) {
                JOptionPane.showMessageDialog(view, "Fehler beim Export: " + ex.getMessage(),
                        "Export-Fehler", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}