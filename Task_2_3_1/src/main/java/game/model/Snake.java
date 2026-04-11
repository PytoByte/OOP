package game.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Модель змейки.
 */
public class Snake implements Renderable<SnakePart>, Collider, Updatable, Restartable {
    private final GameWorld gameWorld;
    private final List<Point> points = new LinkedList<>();
    private final int startX;
    private final int startY;
    private final int startSize;
    private final Direction startDirection;
    private Direction direction;
    private int size;

    /**
     * Базовый конструктор класса.
     *
     * @param startX начальный X
     * @param startY начальный Y
     * @param startSize начальный размер
     * @param startDirection начальное направление
     * @param gameWorld мир игры
     */
    public Snake(
            int startX,
            int startY,
            int startSize,
            Direction startDirection,
            GameWorld gameWorld
    ) {
        this.startDirection = startDirection;
        this.direction = startDirection;
        this.startSize = startSize;
        this.startX = startX;
        this.startY = startY;
        this.gameWorld = gameWorld;
        this.size = startSize;
        restart();
    }

    @Override
    public void update() {
        ConstPoint head = getHead();
        Point nextPos = new Point(head.getX(), head.getY());

        int fieldWidth = gameWorld.getWidth();
        int fieldHeight = gameWorld.getHeight();

        switch (direction) {
            case UP -> {
                nextPos.setY((head.getY() - 1 + fieldHeight) % fieldHeight);
            }
            case DOWN -> {
                nextPos.setY((head.getY() + 1) % fieldHeight);
            }
            case LEFT -> {
                nextPos.setX((head.getX() - 1 + fieldWidth) % fieldWidth);
            }
            case RIGHT -> {
                nextPos.setX((head.getX() + 1) % fieldWidth);
            }
            default -> {
                System.err.printf("Unexpected snake direction %s\n", direction);
            }
        }

        for (Point body : points) {
            int oldX = body.getX();
            body.setX(nextPos.getX());

            int oldY = body.getY();
            body.setY(nextPos.getY());

            nextPos.setX(oldX);
            nextPos.setY(oldY);

            if (body != head && body.equals(head)) {
                gameWorld.setGameOver(true);
            }
        }

        Point newBody = new Point(nextPos.getX(), nextPos.getY());
        if (points.size() < size) {
            points.add(newBody);
        }
        if (newBody.equals(head)) {
            gameWorld.setGameOver(true);
        }
    }

    public int getSize() {
        return size;
    }

    @Override
    public void restart() {
        points.clear();
        points.add(new Point(startX, startY));
        size = startSize;
        direction = startDirection;
    }

    @Override
    public void onCollision(Collider other, ConstPoint p) {
        if (other instanceof FoodManager) {
            foodCollision(p);
        }
    }

    /**
     * Обработчик коллизии с едой.
     *
     * @param p Точка в которой произошла коллизия
     */
    private void foodCollision(ConstPoint p) {
        if (getHead().equals(p)) {
            size++;
        }
    }

    public ConstPoint getHead() {
        return points.get(0);
    }

    /**
     * Устанавливает направление движения змейки.
     * Игнорирует изменение направления на противоположное.
     *
     * @param dir новое направление движения
     */
    public void setDirection(Direction dir) {
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

    @Override
    public List<Pair<ConstPoint, SnakePart>> getRenderData() {
        List<Pair<ConstPoint, SnakePart>> renderData = new ArrayList<>();
        boolean isHead = true;

        for (Point p : points) {
            SnakePart type = isHead ? SnakePart.HEAD : SnakePart.BODY;
            renderData.add(new Pair<>(p, type));
            isHead = false;
        }

        return renderData;
    }

    @Override
    public List<ConstPoint> getCollider() {
        return new LinkedList<>(points);
    }

    public Direction getStartDirection() {
        return startDirection;
    }
}