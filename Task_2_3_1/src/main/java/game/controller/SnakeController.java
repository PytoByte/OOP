package game.controller;

import game.model.Direction;
import game.model.Point;
import game.model.Snake;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

import java.util.List;

public class SnakeController implements Controller, ColliderControl {
    Snake snake;

    public SnakeController(Snake model) {
        this.snake = model;
    }

    public void init() {
        snake.clear();
        snake.add(new Point(WIDTH / 2, HEIGHT / 2));

        obstacles.clear();
        obstacles.addAll(List.of(
                new Point(5, 5), new Point(5, 6), new Point(14, 10)
        ));

        food.clear();
        for (int i = 0; i < T; i++) spawnFood();
    }

    public void update() {
        if (model.isGameOver() || model.isGameWin()) {
            return;
        }

        Point head = model.getSnake().getFirst();
        Point newHead = switch (direction) {
            case UP -> new Point(head.x, head.y - 1);
            case DOWN -> new Point(head.x, head.y + 1);
            case LEFT -> new Point(head.x - 1, head.y);
            case RIGHT -> new Point(head.x + 1, head.y);
        };

        // Столкновения
        if (newHead.x < 0 || newHead.x >= WIDTH || newHead.y < 0 || newHead.y >= HEIGHT ||
                obstacles.stream().anyMatch(o -> o.equals(newHead)) ||
                snake.stream().anyMatch(s -> s.equals(newHead))) {
            gameOver = true;
            return;
        }

        // Еда
        boolean ate = false;
        for (int i = 0; i < food.size(); i++) {
            if (food.get(i).equals(newHead)) {
                food.remove(i);
                spawnFood();
                ate = true;
                break;
            }
        }

        snake.addFirst(newHead);
        if (!ate) snake.removeLast();

        if (snake.size() >= L) gameWin = true;
    }

    @Override
    public Object getModel() {
        return snake;
    }

    @Override
    public void setupEvents(Scene scene) {
        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();

            if (code == KeyCode.UP) {
                snake.setDirection(Direction.UP);
            }

            if (code == KeyCode.DOWN) {
                snake.setDirection(Direction.DOWN);
            }

            if (code == KeyCode.LEFT) {
                snake.setDirection(Direction.LEFT);
            }

            if (code == KeyCode.RIGHT) {
                snake.setDirection(Direction.RIGHT);
            }
        });
    }

    @Override
    public void collide(Object model) {

    }
}
