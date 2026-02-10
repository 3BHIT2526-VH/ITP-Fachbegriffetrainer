package util;

import model.*;
import java.io.*;
import java.net.URL;
import java.util.UUID;

public class CSVQuestionPoolRepo implements QuestionPoolRepo {

    private static final String CSV_SEPARATOR = ";";
    private static final String EXPECTED_HEADER = "Typ;ID;Frage;Antwort;Option";

    @Override
    public QuestionPool load(File file) throws StorageException, CsvFormatException {
        String poolName = file.getName().replaceFirst("[.][^.]+$", "");
        QuestionPool pool = new QuestionPool(poolName);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String header = reader.readLine();
            if (header == null) throw new CsvFormatException("Datei ist leer.");
            if (!header.trim().equalsIgnoreCase(EXPECTED_HEADER)) {
                throw new CsvFormatException("Ungültiger Header. Erwartet: " + EXPECTED_HEADER);
            }

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(CSV_SEPARATOR);
                if (parts.length < 4) throw new CsvFormatException("Zeile unvollständig: " + line);

                try {
                    String type = parts[0];
                    UUID id = UUID.fromString(parts[1]);
                    String prompt = parts[2];
                    String answer = parts[3];

                    if (type.equalsIgnoreCase("TEXT")) {
                        pool.add(new TextQuestion(id, prompt, answer));
                    } else if (type.equalsIgnoreCase("IMAGE")) {
                        if (parts.length < 5) throw new CsvFormatException("Bild-URL fehlt: " + line);
                        pool.add(new ImageQuestion(id, prompt, answer, new URL(parts[4])));
                    }
                } catch (Exception e) {
                    throw new CsvFormatException("Fehler beim Parsen der Zeile: " + line);
                }
            }
        } catch (IOException e) {
            throw new StorageException("Dateizugriffsfehler beim Laden: " + e.getMessage());
        }
        return pool;
    }

    @Override
    public void save(QuestionPool pool, File file) throws StorageException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(EXPECTED_HEADER);
            writer.newLine();

            for (Question q : pool.getQuestions()) {
                String[] row = new String[5];
                row[1] = q.getId().toString();
                row[2] = q.getPrompt();
                row[3] = q.getAnswer();

                if (q instanceof ImageQuestion) {
                    row[0] = "IMAGE";
                    row[4] = ((ImageQuestion) q).getImageUrl().toString();
                } else {
                    row[0] = "TEXT";
                    row[4] = "";
                }
                writer.write(String.join(CSV_SEPARATOR, row));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new StorageException("Fehler beim Speichern des Pools: " + e.getMessage());
        }
    }
}