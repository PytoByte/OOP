package game.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Модель змейки.
 */
public class Snake implements Renderable<SnakePart>, Collider, Updatable, Restartable {
    protected final GameWorld gameWorld;
    private final List<Point> points = new LinkedList<>();
    private final int startX;
    private final int startY;
    private final int startSize;
    private final Direction startDirection;
    protected Direction direction;
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

        int fieldWidth = gameWorld.getWidth();
        int fieldHeight = gameWorld.getHeight();

        Point nextPos = new Point(
                (head.getX() + direction.vecX + fieldWidth) % fieldWidth,
                (head.getY() + direction.vecY + fieldHeight) % fieldHeight
        );

        for (Point body : points) {
            int oldX = body.getX();
            body.setX(nextPos.getX());

            int oldY = body.getY();
            body.setY(nextPos.getY());

            nextPos.setX(oldX);
            nextPos.setY(oldY);

            if (body.equals(head) && body != head) {
                selfCollision();
            }
        }

        Point newBody = new Point(nextPos.getX(), nextPos.getY());
        if (points.size() < size) {
            points.add(newBody);
        }

        if (newBody.equals(head)) {
            selfCollision();
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
        } else if (other instanceof Bot bot) {
            botCollision(bot, p);
        }
    }

    /**
     * Обработчик коллизии с едой.
     *
     * @param p Точка в которой произошла коллизия
     */
    protected void foodCollision(ConstPoint p) {
        if (getHead().equals(p)) {
            size++;
        }
    }

    /**
     * Обработчик коллизии с самим собой.
     */
    protected void selfCollision() {
        gameWorld.setGameOver(true);
    }

    /**
     * Обработчик коллизии с ботом.
     *
     * @param p Точка в которой произошла коллизия
     */
    private void botCollision(Bot bot, ConstPoint p) {
        if (getHead().equals(p) && bot.isAlive()) {
            gameWorld.setGameOver(true);
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