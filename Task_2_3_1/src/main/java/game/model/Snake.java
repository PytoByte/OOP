package game.model;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Класс, представляющий змейку как управляемый игровой объект.
 * Реализует логику движения, хранения сегментов тела и смены направления.
 */
public class Snake implements Renderable, Collider, Updatable {
    private final List<Point> points = new LinkedList<>();
    private final int startX;
    private final int startY;
    private final int startSize;
    private Direction direction;
    private boolean isDead = false;

    // Ссылка на мир нужна, чтобы змейка могла начислить очки или завершить игру
    private final GameWorld gameWorld;

    public Snake(int startX, int startY, int startSize, Direction startDirection, GameWorld gameWorld) {
        this.direction = startDirection;
        this.startSize = startSize;
        this.startX = startX;
        this.startY = startY;
        this.gameWorld = gameWorld;
        restart();
    }

    /**
     * Логика перемещения змейки. Переехала сюда из контроллера.
     */
    @Override
    public void update() {
        if (isDead) {
            return;
        }

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

        // Логика перемещения сегментов тела
        for (Point body : points) {
            int oldX = body.getCoordX();
            body.setCoordX(nextPos.getCoordX());

            int oldY = body.getCoordY();
            body.setCoordY(nextPos.getCoordY());

            nextPos.setCoordX(oldX);
            nextPos.setCoordY(oldY);

            // Проверка на столкновение головы с телом
            if (body != head && body.equals(head)) {
                System.out.println("Suicide");
                isDead = true;
                gameWorld.setGameOver(true);
            }
        }
    }

    @Override
    public void restart() {
        points.clear();
        isDead = false;
        points.add(new Point(startX, startY));
        for (int i = 0; i < startSize - 1; i++) {
            points.add(new Point(-1, -1));
        }
    }

    /**
     * Реакция змейки на столкновения.
     */
    @Override
    public void onCollision(Collider other) {
        if (other instanceof Food) {
            // Если столкнулись с едой — растем
            points.add(new Point(-1, -1));
        } else if (other instanceof Walls) {
            // Если со стеной — умираем
            isDead = true;
            gameWorld.setGameOver(true);
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
    public List<Pair<Point, String>> getRenderData() {
        List<Pair<Point, String>> renderData = new ArrayList<>();
        boolean isHead = true;

        for (Point p : points) {
            String type = isHead ? "SNAKE_HEAD" : "SNAKE_BODY";
            renderData.add(new Pair<>(p, type));
            isHead = false;
        }

        return renderData;
    }

    @Override
    public List<Point> getCollider() {
        return new LinkedList<>(points);
    }

    public int getStartX() { return startX; }
    public int getStartY() { return startY; }
    public int getStartSize() { return startSize; }
    public Direction getDirection() { return direction; }
}