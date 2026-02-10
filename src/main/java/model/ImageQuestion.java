package model;
import util.ValidationException;
import java.net.URL;
import java.util.UUID;

public class ImageQuestion extends Question {
    private URL imageURL;

    public ImageQuestion(UUID id, String prompt, String answer, URL imageURL) {
        super(id, (prompt == null || prompt.isBlank()) ? "Benenne das Bild:" : prompt, answer);
        this.imageURL = imageURL;
    }

    public URL getImageUrl() { return imageURL; }

    @Override
    public void validate() {
        if (imageURL == null) throw new ValidationException("Bild-URL fehlt.");
        if (answer == null || answer.isBlank()) throw new ValidationException("Antwort fehlt.");
    }
}