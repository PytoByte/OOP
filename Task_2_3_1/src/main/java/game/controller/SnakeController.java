package game.controller;

import game.model.Direction;
import game.model.Snake;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class SnakeController implements Controller {
    private final Snake snake;
    private Direction directionBuffer;

    public SnakeController(Snake snake) {
        this.snake = snake;
        this.directionBuffer = snake.getDirection();
    }

    @Override
    public void update() {
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

    @Override
    public void restart() {
        directionBuffer = snake.getDirection();
    }
}