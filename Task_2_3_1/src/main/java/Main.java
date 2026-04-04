import game.SnakeGame;
import javafx.application.Application;

/**
 * Главный загрузочный класс приложения.
 * Служит точкой входа для виртуальной машины Java (JVM).
 */
public class Main {
    /**
     * Запускает жизненный цикл JavaFX приложения.
     * Передает управление классу {@link SnakeGame}, который отвечает за настройку сцены.
     *
     * @param args аргументы командной строки.
     */
    public static void main(String[] args) {
        Application.launch(SnakeGame.class, args);
    }
}