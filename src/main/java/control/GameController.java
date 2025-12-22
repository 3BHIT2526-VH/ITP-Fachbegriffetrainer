package control;

import Model.HangmanGame;
import Model.QuestionPool;

public class GameController {

    public GameController() {}

    public HangmanGame newGameFromPool(QuestionPool pool) {
        // UML: HangmanGame(int maxErrors, String targetWord)
        // The pool must contain questions whose text is used as words.
        // Implementation left abstract, UML does not define logic.

        String word = pool.getQuestions().get(0).getText(); // placeholder
        return new HangmanGame(8, word); // arbitrary example: maxErrors = 8
    }
}