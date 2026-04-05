package game.controller;

import game.model.Direction;
import game.model.Snake;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

/**
 * Контроллер змейки.
 */
public class SnakeController implements Controller {
    private final Snake snake;
    private Direction directionBuffer;

    /**
     * Базовый конструктор класса.
     *
     * @param snake модель змейки
     */
    public SnakeController(Snake snake) {
        this.snake = snake;
        this.directionBuffer = snake.getDirection();
    }

    @Override
    public void tick() {
        snake.setDirection(directionBuffer);
    }

    @Override
    public void setupEvents(Scene scene) {
        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();

            switch (code) {
                case UP -> {
                    directionBuffer = Direction.UP;
                }
                case DOWN -> {
                    directionBuffer = Direction.DOWN;
                }
                case LEFT -> {
                    directionBuffer = Direction.LEFT;
                }
                case RIGHT -> {
                    directionBuffer = Direction.RIGHT;
                }
                default -> {
                    System.err.println("Skip unused button");
                }
            }
        });
    }
}