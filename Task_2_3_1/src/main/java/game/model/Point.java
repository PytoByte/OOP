package game.model;

/**
 * Базовый класс, представляющий точку в двумерном пространстве игрового поля.
 * Используется для хранения координат сегментов змейки, еды и препятствий.
 */
public class Point {
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

        return coordX == p.coordX && coordY == p.coordY;
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

    public int getCoordX() {
        return coordX;
    }

    public void setCoordX(int coordX) {
        this.coordX = coordX;
    }

    public int getCoordY() {
        return coordY;
    }

    public void setCoordY(int coordY) {
        this.coordY = coordY;
    }
}