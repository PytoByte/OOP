package game.controller;

/**
 * Абстракция игрового таймера.
 */
public interface GameTimer {
    /**
     * Запуск таймера.
     */
    void play();

    /**
     * Остановка таймера.
     */
    void stop();

    /**
     * Установка обработчика таймера.
     *
     * @param action обработчик таймера
     */
    void setOnTick(Runnable action);
}