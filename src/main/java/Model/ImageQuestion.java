package Model;

import java.net.URL;
import java.util.UUID;

public class ImageQuestion extends Question {

    private final URL imageURL;

    public ImageQuestion(UUID id, String prompt, String answer, URL imageURL) {
        super(id, prompt, answer);
        this.imageURL = imageURL;
    }

    public URL getImageURL() {
        return imageURL;
    }

    @Override
    public void validate() {
        // No additional UML-specified logic
    }
}