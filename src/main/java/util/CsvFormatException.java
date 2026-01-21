package util;

/** Ausnahme für Fehler im CSV-Format. */
public class CsvFormatException extends Exception {
    public CsvFormatException(String message) { super(message); }
}