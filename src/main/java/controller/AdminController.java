package controller;

import model.*;
import util.*;
import view.AdminDialog;
import javax.swing.*;
import java.io.File;
import java.util.UUID;

public class AdminController {
    private AdminDialog view;
    private ITPAPP mainController;
    private QuestionPoolRepo repo = new CSVQuestionPoolRepo();
    private String currentPoolName;

    public AdminController(ITPAPP mainController) {
        this.mainController = mainController;
        this.view = new AdminDialog();

        mainController.getPools().keySet().forEach(view::addPoolToList);

        view.setCreatePoolListener(e -> {
            String name = view.showInputDialog("Neuer Pool", "Name des Pools:", "");
            if (name != null && !name.trim().isEmpty()) {
                if (!mainController.getPools().containsKey(name)) {
                    QuestionPool newPool = new QuestionPool(name);
                    mainController.getPools().put(name, newPool);
                    view.addPoolToList(name);
                } else {
                    JOptionPane.showMessageDialog(view, "Ein Pool mit diesem Namen existiert bereits.");
                }
            }
        });

        view.setRenamePoolListener(e -> {
            String oldName = view.getSelectedPool();
            if (oldName != null) {
                String newName = view.showInputDialog("Pool umbenennen", "Neuer Name:", oldName);

                if (newName != null && !newName.trim().isEmpty() && !newName.equals(oldName)) {
                    if (!mainController.getPools().containsKey(newName)) {
                        QuestionPool pool = mainController.getPools().remove(oldName);
                        pool.setName(newName);
                        mainController.getPools().put(newName, pool);
                        view.refreshPoolList(mainController.getPools().keySet());

                        JOptionPane.showMessageDialog(view, "Pool erfolgreich umbenannt.");
                    } else {
                        JOptionPane.showMessageDialog(view, "Ein Pool mit diesem Namen existiert bereits.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(view, "Bitte Pool auswählen.");
            }
        });

        view.setOpenPoolListener(e -> {
            String selectedName = view.getSelectedPool();
            if (selectedName != null) {
                this.currentPoolName = selectedName;
                updateQuestionTable();
                view.showQuestions();
            } else {
                JOptionPane.showMessageDialog(view, "Bitte Pool auswählen.");
            }
        });

        view.setAddQuestListener(e -> {
            if (currentPoolName == null) return;

            String[] types = {"Text-Frage", "Bild-Frage"};
            String type = (String) JOptionPane.showInputDialog(view, "Typ wählen:", "Neue Frage",
                    JOptionPane.QUESTION_MESSAGE, null, types, types[0]);

            if (type == null) return;

            QuestionPool pool = mainController.getPools().get(currentPoolName);
            String answer = view.showInputDialog("Neue Frage", "Gesuchter Begriff (Antwort):", "");
            if (answer == null || answer.trim().isEmpty()) return;

            if (type.equals("Bild-Frage")) {
                String urlStr = view.showInputDialog("Bild-Frage", "Bild-URL:", "http://");
                try {
                    if (urlStr != null) {
                        pool.add(new ImageQuestion(UUID.randomUUID(), "", answer, new java.net.URL(urlStr)));
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view, "Ungültige URL!");
                }
            } else {
                String prompt = view.showInputDialog("Text-Frage", "Fragetext:", "");
                if (prompt != null) {
                    pool.add(new TextQuestion(UUID.randomUUID(), prompt, answer));
                }
            }
            updateQuestionTable();
        });

        view.setEditQuestListener(e -> {
            int row = view.getSelectedQuestionRow();
            if (row == -1) return;

            QuestionPool pool = mainController.getPools().get(currentPoolName);
            Question q = pool.getQuestions().get(row);

            String newAnswer = view.showInputDialog("Bearbeiten", "Antwort:", q.getAnswer());
            if (newAnswer == null) return;

            if (q instanceof ImageQuestion) {
                String newUrl = view.showInputDialog("Bearbeiten", "Bild-URL:", ((ImageQuestion) q).getImageUrl().toString());
                try {
                    pool.getQuestions().set(row, new ImageQuestion(q.getId(), "", newAnswer, new java.net.URL(newUrl)));
                } catch (Exception ex) { JOptionPane.showMessageDialog(view, "Fehler!"); }
            } else {
                String newPrompt = view.showInputDialog("Bearbeiten", "Fragetext:", q.getPrompt());
                pool.getQuestions().set(row, new TextQuestion(q.getId(), newPrompt, newAnswer));
            }
            updateQuestionTable();
        });

        view.setDelQuestListener(e -> {
            int selectedRow = view.getSelectedQuestionRow();
            if (selectedRow != -1) {
                int confirm = JOptionPane.showConfirmDialog(view, "Frage wirklich löschen?", "Löschen", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    QuestionPool pool = mainController.getPools().get(currentPoolName);
                    UUID id = pool.getQuestions().get(selectedRow).getId();
                    pool.remove(id);
                    updateQuestionTable();
                }
            } else {
                JOptionPane.showMessageDialog(view, "Bitte erst eine Frage markieren.");
            }
        });

        view.setPreviewListener(e -> {
            int row = view.getSelectedQuestionRow();
            if (row != -1) {
                QuestionPool pool = mainController.getPools().get(currentPoolName);
                Question q = pool.getQuestions().get(row);
                if (q instanceof ImageQuestion) {
                    showImagePreview(((ImageQuestion) q).getImageUrl().toString());
                } else {
                    JOptionPane.showMessageDialog(view, "Dies ist keine Bild-Frage.");
                }
            }
        });

        view.setSearchListener(this::updateQuestionTable);

        view.setImportListener(e -> {
            if (currentPoolName == null) return;

            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Fragen in '" + currentPoolName + "' importieren");

            if (fc.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
                try {
                    File file = fc.getSelectedFile();
                    QuestionPool importedData = repo.load(file);

                    // Merge
                    QuestionPool targetPool = mainController.getPools().get(currentPoolName);
                    for (Question q : importedData.getQuestions()) {
                        targetPool.add(q);
                    }

                    updateQuestionTable();
                    JOptionPane.showMessageDialog(view, importedData.size() + " Fragen erfolgreich hinzugefügt.");

                } catch (StorageException | CsvFormatException ex) {
                    JOptionPane.showMessageDialog(view, "Fehler beim Import: " + ex.getMessage());
                }
            }
        });


        view.setExportListener(e -> {
            if (currentPoolName == null) return;

            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Pool '" + currentPoolName + "' exportieren");
            if (fc.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
                try {
                    QuestionPool currentPool = mainController.getPools().get(currentPoolName);
                    repo.save(currentPool, fc.getSelectedFile());
                    JOptionPane.showMessageDialog(view, "Export erfolgreich.");
                } catch (StorageException ex) {
                    JOptionPane.showMessageDialog(view, "Fehler beim Export: " + ex.getMessage());
                }
            }
        });

        view.setValidateListener(e -> {
            if (currentPoolName == null) return;
            QuestionPool pool = mainController.getPools().get(currentPoolName);
            int errors = 0;
            StringBuilder sb = new StringBuilder("Validierungsergebnisse:\n");

            for (Question q : pool.getQuestions()) {
                try {
                    q.validate();
                } catch (ValidationException ex) {
                    errors++;
                    sb.append("- ").append(q.getPrompt()).append(": ").append(ex.getMessage()).append("\n");
                }
            }

            if (errors == 0) {
                JOptionPane.showMessageDialog(view, "Alle Fragen im Pool sind valide!");
            } else {
                JOptionPane.showMessageDialog(view, sb.toString(), "Validierungsfehler", JOptionPane.WARNING_MESSAGE);
            }
        });

        view.setBackToPoolsListener(e -> {
            currentPoolName = null;
            view.showPools();
        });
        view.setBackToMainListener(e -> {
            view.dispose();
            mainController.showMainMenu();
        });

        view.setVisible(true);
    }

    private void updateQuestionTable() {
        view.clearTable();
        QuestionPool pool = mainController.getPools().get(currentPoolName);
        if (pool == null) return;

        String filter = view.getSearchText().toLowerCase();

        for (Question q : pool.getQuestions()) {
            if (q.getPrompt().toLowerCase().contains(filter) || q.getAnswer().toLowerCase().contains(filter)) {
                String typeInfo = (q instanceof ImageQuestion) ? ((ImageQuestion) q).getImageUrl().toString() : "Text-Frage";
                view.addRowToTable(new Object[]{q.getPrompt(), q.getAnswer(), typeInfo});
            }
        }
    }

    private void showImagePreview(String url) {
        JDialog dlg = new JDialog(view, "Bildvorschau", true);
        try {
            JLabel lbl = new JLabel(new ImageIcon(new java.net.URL(url)));
            dlg.add(new JScrollPane(lbl));
            dlg.setSize(600, 400);
            dlg.setLocationRelativeTo(view);
            dlg.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Bild konnte nicht geladen werden.");
        }
    }
    }