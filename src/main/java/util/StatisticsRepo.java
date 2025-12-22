package util;

import Model.QuizStatistics; // Angenommen, diese Klasse existiert im 'model' Package
import java.io.File;
import java.util.Optional;

/**
 * Schnittstelle (Interface) für die Persistenz von Quiz-Statistiken.
 * Definiert Methoden zum Laden und Speichern von Statistik-Objekten.
 * * Entspricht dem Klassendiagramm im 'util' Package.
 */
public interface StatisticsRepo {

    /**
     * Lädt die gespeicherten Quiz-Statistiken aus der angegebenen Datei.
     * * @param file Die Datei, aus der die Statistiken geladen werden sollen.
     * @return Ein Optional, das das geladene QuizStatistics-Objekt enthält,
     * falls erfolgreich. Ein leeres Optional, falls die Datei leer ist oder nicht existiert.
     * @throws StorageException Falls ein kritischer Fehler beim Lesen der Datei auftritt.
     */
    Optional<QuizStatistics> load(File file) throws StorageException;

    /**
     * Speichert das gegebene QuizStatistics-Objekt in der angegebenen Datei.
     * * @param statistics Das QuizStatistics-Objekt, das gespeichert werden soll.
     * @param file Die Zieldatei, in der die Statistiken gespeichert werden sollen.
     * @throws StorageException Falls ein kritischer Fehler beim Schreiben in die Datei auftritt.
     */
    void save(QuizStatistics statistics, File file) throws StorageException;
}