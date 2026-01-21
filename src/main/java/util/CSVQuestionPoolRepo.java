package util;

import model.Question;
import model.QuestionPool;
import model.TextQuestion;
import java.io.*;
import java.util.UUID;

public class CSVQuestionPoolRepo implements QuestionPoolRepo {
    @Override
    public QuestionPool load(File file) {
        QuestionPool pool = new QuestionPool("Geladener Pool");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 2) {
                    // Beispiel-Logik: Erstellt eine TextQuestion aus CSV-Daten
                    pool.add(new TextQuestion(UUID.randomUUID(), parts[0], parts[1]));
                }
            }
        } catch (IOException e) {
            throw new StorageException("Laden des Pools fehlgeschlagen: " + e.getMessage());
        }
        return pool;
    }

    @Override
    public void save(QuestionPool pool, File file) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            for (Question q : pool.getQuestions()) {
                pw.println(q.getPrompt() + ";" + q.getAnswer());
            }
        } catch (IOException e) {
            throw new StorageException("Speichern des Pools fehlgeschlagen: " + e.getMessage());
        }
    }
}