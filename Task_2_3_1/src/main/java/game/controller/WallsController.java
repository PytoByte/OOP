package game.controller;

import game.model.Collider;
import game.model.GameWorld;
import game.model.Point;
import game.model.Walls;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Random;

/**
 * Контроллер для управления игровыми стенами (препятствиями).
 * Отвечает за инициализацию стен при старте или перезапуске игры,
 * а также за их размещение на игровом поле.
 */
public class WallsController implements Controller, ColliderControl {
    GameWorld gameWorld;
    Walls walls;
    Random random = new Random(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));

    /**
     * Создает контроллер стен и связывает его с моделью игры и объектом стен.
     * При создании автоматически вызывает метод {@link #restart()}.
     *
     * @param gameWorld общая модель игры для получения размеров поля.
     * @param walls объект стен, которым будет управлять данный контроллер.
     */
    public WallsController(GameWorld gameWorld, Walls walls) {
        this.gameWorld = gameWorld;
        this.walls = walls;
        restart();
    }

    /**
     * Обрабатывает столкновение стены с другим объектом.
     * На данный момент метод пуст, так как стены статичны и не меняются при коллизиях.
     *
     * @param model объект, с которым произошло столкновение.
     * @param p точка, в которой зафиксировано столкновение.
     */
    @Override
    public void collide(Collider model, Point p) {

    }

    /**
     * Возвращает управляемый объект стен.
     *
     * @return объект {@link Walls}, привязанный к контроллеру.
     */
    @Override
    public Object getModel() {
        return walls;
    }

    /**
     * Сбрасывает состояние стен и создает новое случайное препятствие.
     * Очищает старые точки и добавляет одну новую точку в случайных координатах поля.
     */
    @Override
    public void restart() {
        Point p = new Point(
                random.nextInt(gameWorld.getWidth()),
                random.nextInt(gameWorld.getHeight())
        );
        walls.getPoints().clear();
        walls.getPoints().add(p);
    }
}