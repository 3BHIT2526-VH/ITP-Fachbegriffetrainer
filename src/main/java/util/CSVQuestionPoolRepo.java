package util;

import Model.ImageQuestion;
import Model.Question;
import Model.QuestionPool;
import Model.TextQuestion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CSVQuestionPoolRepo implements QuestionPoolRepo {

    private final Charset charset;

    public CSVQuestionPoolRepo() {
        this(StandardCharsets.UTF_8);
    }

    public CSVQuestionPoolRepo(Charset charset) {
        this.charset = charset;
    }

    @Override
    public QuestionPool load(File file) throws StorageException, CsvFormatException {
        List<Question> questions = new ArrayList<>();

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), charset))) {

            String line;
            boolean first = true;

            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                // optional Header überspringen
                if (first && line.startsWith("id;")) {
                    first = false;
                    continue;
                }
                first = false;

                String[] parts = line.split(";", -1);
                if (parts.length < 4) {
                    throw new CsvFormatException("Zu wenige Spalten in Zeile: " + line);
                }

                UUID id;
                try {
                    id = UUID.fromString(parts[0]);
                } catch (IllegalArgumentException e) {
                    throw new CsvFormatException("Ungültige UUID: " + parts[0], e);
                }

                String type = parts[1];
                String prompt = unescape(parts[2]);
                String answer = unescape(parts[3]);

                Question q;
                if ("IMAGE".equalsIgnoreCase(type)) {
                    if (parts.length < 5 || parts[4].isEmpty()) {
                        throw new CsvFormatException("Image-Frage ohne URL: " + line);
                    }
                    try {
                        URL url = new URL(unescape(parts[4]));
                        q = new ImageQuestion(id, prompt, answer, url);
                    } catch (MalformedURLException e) {
                        throw new CsvFormatException("Ungültige Bild-URL: " + parts[4], e);
                    }
                } else { // TEXT oder unbekannt
                    q = new TextQuestion(id, prompt, answer);
                }

                questions.add(q);
            }

        } catch (IOException e) {
            throw new StorageException("Fragenpool konnte nicht gelesen werden: " + file, e);
        }

        String poolName = stripExtension(file.getName());
        return new QuestionPool(poolName, questions);
    }

    @Override
    public void save(QuestionPool pool, File file) throws StorageException {
        try (BufferedWriter out = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), charset))) {

            // Header
            out.write("id;type;prompt;answer;imageUrl");
            out.newLine();

            for (Question q : pool.getQuestions()) {
                String type;
                String imageUrl = "";

                if (q instanceof ImageQuestion imgQ) {
                    type = "IMAGE";
                    URL url = imgQ.getImageURL();
                    imageUrl = (url != null) ? url.toExternalForm() : "";
                } else {
                    type = "TEXT";
                }

                StringBuilder sb = new StringBuilder();
                sb.append(q.getId()).append(';')
                        .append(type).append(';')
                        .append(escape(q.getPrompt())).append(';')
                        .append(escape(q.getAnswer())).append(';')
                        .append(escape(imageUrl));

                out.write(sb.toString());
                out.newLine();
            }

        } catch (IOException e) {
            throw new StorageException("Fragenpool konnte nicht gespeichert werden: " + file, e);
        }
    }

    private static String stripExtension(String name) {
        int idx = name.lastIndexOf('.');
        return (idx > 0) ? name.substring(0, idx) : name;
    }

    private static String escape(String value) {
        if (value == null) return "";
        // sehr einfache Escapes, reicht für Schulprojekt
        return value.replace("\\", "\\\\")
                .replace(";", "\\;")
                .replace("\n", "\\n")
                .replace("\r", "");
    }

    private static String unescape(String value) {
        StringBuilder sb = new StringBuilder();
        boolean esc = false;
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (esc) {
                switch (c) {
                    case 'n' -> sb.append('\n');
                    case ';' -> sb.append(';');
                    case '\\' -> sb.append('\\');
                    default -> sb.append(c);
                }
                esc = false;
            } else if (c == '\\') {
                esc = true;
            } else {
                sb.append(c);
            }
        }
        if (esc) sb.append('\\');
        return sb.toString();
    }
}