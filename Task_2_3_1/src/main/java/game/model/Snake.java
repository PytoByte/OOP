package game.model;

import java.util.LinkedList;
import java.util.List;

public class Snake implements GameObject, Collider {
    private final List<Point> points = new LinkedList<>();
    Direction direction;

    public Snake() {
        //snake.clear();
        //snake.add(new Point(WIDTH / 2, HEIGHT / 2));
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction dir) {
        // Запрет разворота на 180°
        if (dir == Direction.UP && direction != Direction.DOWN) {
            direction = dir;
        }

        if (dir == Direction.DOWN && direction != Direction.UP) {
            direction = dir;
        }

        if (dir == Direction.LEFT && direction != Direction.RIGHT) {
            direction = dir;
        }

        if (dir == Direction.RIGHT && direction != Direction.LEFT) {
            direction = dir;
        }
    }

    public List<Point> getPoints() {
        return points;
    }

    @Override
    public Iterable<Point> getCollider() {
        return points;
    }
}
