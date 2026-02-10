package model;
import util.ValidationException;

import java.util.UUID;

public class TextQuestion extends Question {
    public TextQuestion(UUID id, String prompt, String answer) {
        super(id, prompt, answer);
    }

    @Override
    public void validate() {
        if (prompt == null || prompt.isBlank() || answer == null || answer.isBlank()) {
            throw new ValidationException("Invalid TextQuestion");
        }
    }
}