import game.SnakeGame;
import javafx.application.Application;

/**
 * Главный загрузочный класс приложения.
 */
public class Main {
    /**
     * Запускает игру SnakeGame.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        Application.launch(SnakeGame.class, args);
    }
}