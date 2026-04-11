package game.model;

/**
 * Базовый класс, представляющий точку в двумерном пространстве игрового поля.
 * Используется для хранения координат сегментов змейки, еды и препятствий.
 */
public class Point implements ConstPoint {
    private int coordX;
    private int coordY;

    /**
     * Создает новую точку с заданными координатами.
     *
     * @param x координата по горизонтали.
     * @param y координата по вертикали.
     */
    public Point(int x, int y) {
        this.coordX = x;
        this.coordY = y;
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

        return coordX == p.getX() && coordY == p.getY();
    }

    /**
     * Возвращает хэш точки.
     *
     * @return хэш точки
     */
    @Override
    public int hashCode() {
        return java.util.Objects.hash(coordX, coordY);
    }

    /**
     * Возвращает строковое представление точки.
     *
     * @return координаты в формате "(x, y)".
     */
    @Override
    public String toString() {
        return String.format("(%d, %d)", coordX, coordY);
    }

    public int getX() {
        return coordX;
    }

    public void setX(int coordX) {
        this.coordX = coordX;
    }

    public int getY() {
        return coordY;
    }

    public void setY(int coordY) {
        this.coordY = coordY;
    }
}