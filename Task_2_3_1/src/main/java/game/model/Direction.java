package game.model;

/**
 * Перечисление возможных направлений движения игровых объектов.
 */
public enum Direction {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    public final int vecX;
    public final int vecY;

    /**
     * Базовый конструктор класса.
     *
     * @param vecX вектор направления по X
     * @param vecY вектор направления по Y
     */
    Direction(int vecX, int vecY) {
        this.vecX = vecX;
        this.vecY = vecY;
    }

    /**
     * Направление слева от текущего.
     */
    public Direction getLeft() {
        return switch (this) {
            case UP -> LEFT;
            case DOWN -> RIGHT;
            case LEFT -> DOWN;
            case RIGHT -> UP;
        };
    }

    /**
     * Направление справа от текущего.
     */
    public Direction getRight() {
        return switch (this) {
            case UP -> RIGHT;
            case DOWN -> LEFT;
            case LEFT -> UP;
            case RIGHT -> DOWN;
        };
    }
}