package game.model;

/**
 * Интерфейс игровых моделей, требующих обновление на каждый тик.
 */
public interface Updatable {
    /**
     * Метод вызываемый каждый игровой тик.
     */
    void update();
}
