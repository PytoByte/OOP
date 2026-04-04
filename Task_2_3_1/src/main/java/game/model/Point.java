package game.model;

/**
 * Базовый класс, представляющий точку в двумерном пространстве игрового поля.
 * Используется для хранения координат сегментов змейки, еды и препятствий.
 */
public class Point {
    int x;
    int y;

    /**
     * Создает новую точку с заданными координатами.
     *
     * @param x координата по горизонтали.
     * @param y координата по вертикали.
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Сравнивает текущую точку с другим объектом.
     * Результат будет true только в том случае, если объект также является точкой
     * и их координаты x и y совпадают.
     *
     * @param o объект для сравнения.
     * @return true, если координаты точек идентичны.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Point p)) {
            return false;
        }

        return x == p.x && y == p.y;
    }

    /**
     * Возвращает хэш точки.
     *
     * @return хэш точки
     */
    @Override
    public int hashCode() {
        return java.util.Objects.hash(x, y);
    }

    /**
     * Возвращает строковое представление точки.
     *
     * @return координаты в формате "(x, y)".
     */
    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}