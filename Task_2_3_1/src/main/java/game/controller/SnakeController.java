package game.controller;

import game.model.Collider;
import game.model.GameModel;
import game.model.Direction;
import game.model.Food;
import game.model.Point;
import game.model.Snake;
import game.model.Walls;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class SnakeController implements Controller, ColliderControl {
    Snake snake;
    GameModel gameModel;
    Direction directionBuffer;

    public SnakeController(GameModel gameModel, Snake snake) {
        this.snake = snake;
        this.gameModel = gameModel;
        this.directionBuffer = snake.getDirection();
        restart();
    }

    public void update() {
        snake.setDirection(directionBuffer);

        Point head = snake.getHead();
        Point nextPos = new Point(head.getX(), head.getY());
        switch (snake.getDirection()) {
            case UP ->
                    nextPos.setY((head.getY() - 1 + gameModel.getHeight()) % gameModel.getHeight());
            case DOWN ->
                    nextPos.setY((head.getY() + 1) % gameModel.getHeight());
            case LEFT ->
                    nextPos.setX((head.getX() - 1 + gameModel.getWidth()) % gameModel.getWidth());
            case RIGHT ->
                    nextPos.setX((head.getX() + 1) % gameModel.getWidth());
        }

        for (Point body : snake.getPoints()) {
            int oldX = body.getX();
            body.setX(nextPos.getX());

            int oldY = body.getY();
            body.setY((nextPos.getY()));

            nextPos.setX(oldX);
            nextPos.setY(oldY);

            if (body != head && body.equals(head)) {
                System.out.println("Suicide");
                gameModel.setGameOver(true);
            }
        }
    }

    @Override
    public void restart() {
        snake.getPoints().clear();
        snake.increaseBody(new Point(snake.getStartX(), snake.getStartY()));
        for (int i = 0; i < snake.getStartSize() - 1; i++) {
            snake.increaseBody(new Point(-1, -1));
        }
    }

    @Override
    public Object getModel() {
        return snake;
    }

    @Override
    public void setupEvents(Scene scene) {
        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();

            switch (code) {
                case KeyCode.UP -> directionBuffer = Direction.UP;
                case KeyCode.DOWN -> directionBuffer = Direction.DOWN;
                case KeyCode.LEFT -> directionBuffer = Direction.LEFT;
                case KeyCode.RIGHT -> directionBuffer = Direction.RIGHT;
            }
        });
    }

    @Override
    public void collide(Collider model, Point p) {
        if (model instanceof Food) {
            Point head = snake.getHead();
            if (p.equals(head)) {
                snake.increaseBody(new Point(-1, -1));
            }
        } else if (model instanceof Walls) {
            gameModel.setGameOver(true);
        }
    }
}
