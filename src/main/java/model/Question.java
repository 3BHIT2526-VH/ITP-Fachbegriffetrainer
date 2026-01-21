package model;

import java.util.UUID;

public abstract class Question {
    protected UUID id;
    protected String prompt;
    protected String answer;

    protected Question(UUID id, String prompt, String answer) {
        this.id = id;
        this.prompt = prompt;
        this.answer = answer;
    }

    public String getPrompt() { return prompt; }
    public String getAnswer() { return answer; }
    public UUID getId() { return id; }

    public abstract void validate();
}