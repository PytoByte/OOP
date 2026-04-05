package game.model;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Класс, представляющий змейку как управляемый игровой объект.
 * Реализует логику движения, хранения сегментов тела и смены направления.
 */
public class Snake implements Renderable<SnakePart>, Collider, Updatable {
    private final List<Point> points = new LinkedList<>();
    private final int startX;
    private final int startY;
    private final int startSize;
    private Direction direction;
    private final GameWorld gameWorld;

    public Snake(
            int startX,
            int startY,
            int startSize,
            Direction startDirection,
            GameWorld gameWorld
    ) {
        this.direction = startDirection;
        this.startSize = startSize;
        this.startX = startX;
        this.startY = startY;
        this.gameWorld = gameWorld;
        restart();
    }

    @Override
    public void update() {
        Point head = getHead();
        Point nextPos = new Point(head.getCoordX(), head.getCoordY());

        int fieldWidth = gameWorld.getWidth();
        int fieldHeight = gameWorld.getHeight();

        switch (direction) {
            case UP -> {
                nextPos.setCoordY((head.getCoordY() - 1 + fieldHeight) % fieldHeight);
            }
            case DOWN -> {
                nextPos.setCoordY((head.getCoordY() + 1) % fieldHeight);
            }
            case LEFT -> {
                nextPos.setCoordX((head.getCoordX() - 1 + fieldWidth) % fieldWidth);
            }
            case RIGHT -> {
                nextPos.setCoordX((head.getCoordX() + 1) % fieldWidth);
            }
            default -> {
                System.err.printf("Unexpected snake direction %s\n", direction);
            }
        }

        for (Point body : points) {
            int oldX = body.getCoordX();
            body.setCoordX(nextPos.getCoordX());

            int oldY = body.getCoordY();
            body.setCoordY(nextPos.getCoordY());

            nextPos.setCoordX(oldX);
            nextPos.setCoordY(oldY);

            if (body != head && body.equals(head)) {
                gameWorld.setGameOver(true);
            }
        }
    }

    @Override
    public void restart() {
        points.clear();
        points.add(new Point(startX, startY));
        for (int i = 0; i < startSize - 1; i++) {
            points.add(new Point(-1, -1));
        }
    }

    @Override
    public void onCollision(Collider other) {
        if (other instanceof Food) {
            points.add(new Point(-1, -1));
        }
    }

    public Point getHead() {
        return points.get(0);
    }

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
    public List<Pair<Point, SnakePart>> getRenderData() {
        List<Pair<Point, SnakePart>> renderData = new ArrayList<>();
        boolean isHead = true;

        for (Point p : points) {
            SnakePart type = isHead ? SnakePart.HEAD : SnakePart.BODY;
            renderData.add(new Pair<>(p, type));
            isHead = false;
        }

        return renderData;
    }

    @Override
    public List<Point> getCollider() {
        return new LinkedList<>(points);
    }

    public Direction getDirection() {
        return direction;
    }
}