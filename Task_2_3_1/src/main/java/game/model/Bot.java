package game.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Класс бота.
 */
public class Bot extends Snake {

    private boolean alive = true;

    /**
     * Базовый конструктор класса.
     *
     * @param startX начальный X
     * @param startY начальный Y
     * @param startSize начальный размер
     * @param startDirection начальное направление
     * @param gameWorld мир игры
     */
    public Bot(
            int startX,
            int startY,
            int startSize,
            Direction startDirection,
            GameWorld gameWorld
    ) {
        super(startX, startY, startSize, startDirection, gameWorld);
    }

    /**
     * Проверка жив ли бот.
     *
     * @return результат проверки
     */
    public boolean isAlive() {
        return alive;
    }

    @Override
    protected void selfCollision() {
        alive = false;
    }

    /**
     * Обработчик коллизии с другой змейкой.
     *
     * @param p Точка в которой произошла коллизия
     */
    private void snakeCollision(ConstPoint p) {
        if (getHead().equals(p)) {
            alive = false;
        }
    }

    @Override
    public void onCollision(Collider other, ConstPoint p) {
        if (other instanceof FoodManager) {
            foodCollision(p);
        } else if (other instanceof Snake) {
            snakeCollision(p);
        }
    }

    /**
     * Возвращает список возможных ходов из текущей позиции.
     *
     * @param direction текущее направление движения
     * @return список пар направление-точка
     */
    private List<Pair<Direction, ConstPoint>> getPossibleMovement(Direction direction) {
        ConstPoint head = getHead();
        int fieldWidth = gameWorld.getWidth();
        int fieldHeight = gameWorld.getHeight();

        List<Pair<Direction, ConstPoint>> possibleMovement = new LinkedList<>();
        Direction[] possibleDirs = { direction, direction.getLeft(), direction.getRight() };

        for (Direction dir : possibleDirs) {
            Point p = Point.fromConstPoint(head);
            p.setX((p.getX() + dir.vecX + fieldWidth) % fieldWidth);
            p.setY((p.getY() + dir.vecY + fieldHeight) % fieldHeight);
            possibleMovement.add(new Pair<>(dir, p));
        }

        return possibleMovement;
    }

    @Override
    public void update() {
        if (!isAlive()) {
            return;
        }

        List<ConstPoint> allColliders = gameWorld.getAllCollidersPoints();
        List<ConstPoint> foods = gameWorld.getFoodPoints();
        List<Pair<Direction, ConstPoint>> possibleMoves = getPossibleMovement(direction);

        List<ConstPoint> realObstacles = new LinkedList<>(allColliders);
        realObstacles.removeAll(foods);

        List<Pair<Direction, ConstPoint>> safeMoves = new LinkedList<>();
        for (Pair<Direction, ConstPoint> move : possibleMoves) {
            if (!realObstacles.contains(move.value())) {
                safeMoves.add(move);
            }
        }

        if (safeMoves.isEmpty()) {
            safeMoves = possibleMoves;
        }

        Pair<Direction, ConstPoint> bestMove = safeMoves.get(0);
        int minDistance = Integer.MAX_VALUE;

        for (Pair<Direction, ConstPoint> move : safeMoves) {
            int distance = getDistanceToNearestFood(move.value(), foods);
            if (distance < minDistance) {
                minDistance = distance;
                bestMove = move;
            }
        }

        setDirection(bestMove.key());
        super.update();
    }

    /**
     * Вычисляет расстояние до ближайшей еды.
     *
     * @param from стартовая точка
     * @param foods список точек с едой
     * @return минимальное расстояние
     */
    private int getDistanceToNearestFood(ConstPoint from, List<ConstPoint> foods) {
        if (foods == null || foods.isEmpty()) {
            return 0;
        }

        int minDist = Integer.MAX_VALUE;
        for (ConstPoint food : foods) {
            int dist = Math.abs(from.getX() - food.getX()) + Math.abs(from.getY() - food.getY());
            if (dist < minDist) {
                minDist = dist;
            }
        }
        return minDist;
    }

    @Override
    public List<ConstPoint> getCollider() {
        if (!isAlive()) {
            return Collections.emptyList();
        }
        return super.getCollider();
    }

    @Override
    public List<Pair<ConstPoint, SnakePart>> getRenderData() {
        if (!isAlive()) {
            return Collections.emptyList();
        }
        return super.getRenderData();
    }

    @Override
    public void restart() {
        alive = true;
        super.restart();
    }
}
