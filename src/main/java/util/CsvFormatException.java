package util;

public class CsvFormatException extends StorageException {

    public CsvFormatException() {
        super();
    }

    public CsvFormatException(String message) {
        super(message);
    }

    public CsvFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public CsvFormatException(Throwable cause) {
        super(cause);
    }
}