package Model;

import util.*;
import java.util.UUID;

public abstract class Question {
    private final UUID id;
    private String prompt;
    private String answer;

    public Question(UUID id, String prompt, String answer) {
        this.id = id;
        this.prompt = prompt;
        this.answer = answer;
    }

    public UUID getId() { return id; }
    public String getPrompt() { return prompt; }
    public String getAnswer() { return answer; }

    // Platzhalter für die Validierung
    public void validate() throws ValidationException {
        if (prompt == null || prompt.trim().isEmpty()) {
            throw new ValidationException("Frage darf nicht leer sein.");
        }
    }
}