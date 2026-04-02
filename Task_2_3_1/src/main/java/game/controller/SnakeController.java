package game.controller;

import game.GameModel;
import game.model.Direction;
import game.model.Food;
import game.model.Point;
import game.model.Snake;
import game.model.Walls;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

import java.util.List;

public class SnakeController implements Controller, ColliderControl {
    Snake snake;
    GameModel gameModel;
    Direction directionBuffer;

    public SnakeController(GameModel gameModel, Snake snake) {
        this.snake = snake;
        this.gameModel = gameModel;
        this.directionBuffer = snake.getDirection();
    }

    public void update() {
        snake.setDirection(directionBuffer);

        List<Point> points = snake.getPoints();
        Point head = points.get(0);
        Point nextPos = new Point(head.getX(), head.getY());
        switch (snake.getDirection()) {
            case UP -> nextPos.setY((head.getY() - 1 + gameModel.getHeight()) % gameModel.getHeight());
            case DOWN -> nextPos.setY((head.getY() + 1) % gameModel.getHeight());
            case LEFT -> nextPos.setX((head.getX() - 1 + gameModel.getWidth()) % gameModel.getWidth());
            case RIGHT -> nextPos.setX((head.getX() + 1) % gameModel.getWidth());
        }

        for (Point body : points) {
            int oldX = body.getX();
            body.setX(nextPos.getX());

            int oldY = body.getY();
            body.setY((nextPos.getY()));

            nextPos.setX(oldX);
            nextPos.setY(oldY);

            if (body != head && body.equals(head)) {
                gameModel.setGameOver(true);
            }
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
    public void collide(Object model, Point p) {
        System.out.println("COLLISION SNAKE");
        if (model instanceof Food) {
            System.out.println("COLLISION SNAKE-FOOD");
            List<Point> points = snake.getPoints();
            Point head = points.get(0);
            points.add(new Point(-1, -1));
        } else if (model instanceof Walls) {

        }
    }
}
