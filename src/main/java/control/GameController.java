package control;

import model.HangmanGame;
import model.QuestionPool;
import model.Question;
import java.util.Collections;
import java.util.List;

public class GameController {
    public GameController() {}

    public HangmanGame newGameFromPool(QuestionPool pool) {
        List<Question> questions = pool.getQuestions();
        if (questions.isEmpty()) {
            throw new RuntimeException("Pool is empty");
        }

        Collections.shuffle(questions);
        String targetWord = questions.get(0).getAnswer();
        return new HangmanGame(10, targetWord);
    }
}