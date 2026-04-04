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

public class FoodController implements Controller, ColliderControl {
    GameModel gameModel;
    Food food;
    Random random = new Random(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));

    public FoodController(GameModel gameModel, Food food) {
        this.gameModel = gameModel;
        this.food = food;
        restart();
    }

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
                                .noneMatch(redP -> redP.getX() == finalX &&
                                        redP.getY() == finalY)
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

    @Override
    public Object getModel() {
        return food;
    }

    @Override
    public void restart() {
        food.getPoints().clear();
        spawnFood(food.getCollider());
    }
}
