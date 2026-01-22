package util;

/** Ausnahme für Fehler beim Laden von Bildern. */
public class ImageLoadException extends Exception {
    public ImageLoadException(String message) { super(message); }
}