package Model;

import java.util.UUID;

public class TextQuestion extends Question {

    public TextQuestion(UUID id, String prompt, String answer) {
        super(id, prompt, answer);
    }

    @Override
    public void validate() {
        // According to UML: override validate()
        // Implementation intentionally empty.
    }
}