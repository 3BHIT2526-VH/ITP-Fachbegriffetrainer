package util;

import model.QuizStatistics;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CSVStatisticsRepo implements StatisticsRepo {

    private static final String CSV_SEPARATOR = ";";
    private static final String EXPECTED_HEADER = "Datum;Pool;Ergebnis;Quote";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Override
    public List<String[]> load(File file) throws StorageException, CsvFormatException {
        List<String[]> data = new ArrayList<>();
        if (!file.exists()) return data;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String header = reader.readLine();
            if (header == null) throw new CsvFormatException("Datei ist leer.");
            if (!header.trim().equalsIgnoreCase(EXPECTED_HEADER)) {
                throw new CsvFormatException("Ungültiger Header. Erwartet: " + EXPECTED_HEADER);
            }

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                data.add(line.split(CSV_SEPARATOR));
            }
        } catch (IOException e) {
            throw new StorageException("Dateizugriffsfehler beim Laden: " + e.getMessage());
        }
        return data;
    }

    @Override
    public void save(QuizStatistics stats, String poolName, File file) throws StorageException {
        boolean isNew = !file.exists();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            if (isNew) {
                writer.write(EXPECTED_HEADER);
                writer.newLine();
            }

            String[] row = {
                    LocalDateTime.now().format(DATE_FORMAT),
                    poolName,
                    stats.getCorrectCount() + "/" + stats.getTotalCount(),
                    stats.getSuccessRate() + "%"
            };
            writer.write(String.join(CSV_SEPARATOR, row));
            writer.newLine();
        } catch (IOException e) {
            throw new StorageException("Fehler beim Speichern der Statistik: " + e.getMessage());
        }
    }

    @Override
    public void saveAll(List<String[]> data, File file) throws StorageException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(EXPECTED_HEADER);
            writer.newLine();

            for (String[] row : data) {
                writer.write(String.join(CSV_SEPARATOR, row));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new StorageException("Fehler beim Exportieren: " + e.getMessage());
        }
    }
}