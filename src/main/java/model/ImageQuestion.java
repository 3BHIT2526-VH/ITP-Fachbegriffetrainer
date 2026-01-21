package model;

import java.net.URL;
import java.util.UUID;

public class ImageQuestion extends Question {
    private URL imageURL;

    public ImageQuestion(UUID id, String prompt, String answer, URL imageURL) {
        super(id, prompt, answer);
        this.imageURL = imageURL;
    }

    public URL getImageUrl() { return imageURL; }

    @Override
    public void validate() {
        if (imageURL == null) {
            throw new RuntimeException("Image URL is required for ImageQuestion");
        }
    }
}