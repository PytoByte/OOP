package game.controller;

import game.model.Collider;
import game.model.Food;
import game.model.GameModel;
import game.model.Point;
import game.model.Snake;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Random;

/**
 * Контроллер для управления объектами еды.
 * Отвечает за генерацию (спавн) еды в свободных ячейках игрового поля,
 * обработку поедания еды змейкой и обновление игрового счета.
 */
public class FoodController implements Controller, ColliderControl {
    GameModel gameModel;
    Food food;
    Random random = new Random(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));

    /**
     * Создает контроллер еды и инициализирует начальное состояние объектов еды на поле.
     *
     * @param gameModel общая модель игры для доступа к размерам поля.
     * @param food объект управления едой, которым будет оперировать контроллер.
     */
    public FoodController(GameModel gameModel, Food food) {
        this.gameModel = gameModel;
        this.food = food;
        restart();
    }

    /**
     * Пытается создать недостающее количество еды на поле, избегая занятых точек.
     * Если случайная координата занята, метод ищет первую свободную ячейку перебором.
     * Если свободных ячеек не осталось, максимальное количество еды ограничивается текущим.
     *
     * @param redZone список координат, в которых нельзя создавать еду (змея, стены и т.д.).
     */
    private void spawnFood(List<Point> redZone) {
        int spawnFoodCount = food.getMaxCount() - food.getCount();
        for (int i = 0; i < spawnFoodCount; i++) {
            boolean found = true;
            Point p = new Point(
                    random.nextInt(gameModel.getWidth()),
                    random.nextInt(gameModel.getHeight())
            );

            if (redZone.contains(p)) {
                found = false;
                for (int x = 0; x < gameModel.getWidth() && !found; x++) {
                    for (int y = 0; y < gameModel.getHeight(); y++) {
                        int finalX = x;
                        int finalY = y;
                        if (redZone.stream()
                                .noneMatch(redP -> redP.getX() == finalX
                                        && redP.getY() == finalY)
                        ) {
                            p.setX(x);
                            p.setY(y);
                            found = true;
                            break;
                        }
                    }
                }
            }

            if (!found) {
                food.setMaxCount(food.getCount());
                break;
            }

            food.addFood(p);
            redZone.add(p);
        }
    }

    /**
     * Обрабатывает столкновение еды с другим объектом (обычно змейкой).
     * При столкновении удаляет съеденную еду, инициирует появление новой
     * и увеличивает счет в модели игры, если инициатор — змейка.
     *
     * @param model объект, столкнувшийся с едой.
     * @param p точка, в которой произошло столкновение.
     */
    @Override
    public void collide(Collider model, Point p) {
        food.removeFood(p);
        List<Point> collider = model.getCollider();
        collider.addAll(food.getCollider());
        spawnFood(collider);

        if (model instanceof Snake) {
            gameModel.increaseScore(1);
        }
    }

    /**
     * Возвращает управляемую модель еды.
     *
     * @return объект {@link Food}.
     */
    @Override
    public Object getModel() {
        return food;
    }

    /**
     * Сбрасывает состояние еды: очищает поле и создает начальный набор объектов еды.
     */
    @Override
    public void restart() {
        food.getPoints().clear();
        spawnFood(food.getCollider());
    }
}
