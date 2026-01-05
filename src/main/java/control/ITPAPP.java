package control;

import view.*;
import util.*;
import model.QuestionPool;

public class ITPAPP {
    public static void main(String[] args) {
        // Initialize Repositories
        QuestionPoolRepo poolRepo = new CSVQuestionPoolRepo();
        StatisticsRepo statsRepo = new CSVStatisticsRepo();

        // Initialize Controllers
        AdminController adminCtrl = new AdminController(poolRepo);
        QuizController quizCtrl = new QuizController(statsRepo);
        GameController gameCtrl = new GameController();

        // In a real Swing app, you would launch a MainFrame here.
        // For this task, we initialize the Dialogs as per UML:
        AdminDialog adminDlg = new AdminDialog(null, true);
        QuizDialog quizDlg = new QuizDialog(null, true);
        GameDialog gameDlg = new GameDialog(null, true);

        adminDlg.showDialog();
    }
}