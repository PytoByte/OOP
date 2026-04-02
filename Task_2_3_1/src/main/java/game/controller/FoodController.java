package game.controller;

import game.GameModel;
import game.model.Food;
import game.model.Point;
import game.model.Snake;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Random;

public class FoodController implements Controller, ColliderControl{
    GameModel gameModel;
    Food food;
    Random random = new Random(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
    public FoodController(GameModel gameModel, Food food) {
        this.gameModel = gameModel;
        this.food = food;
        for (int i = 0; i < food.getMaxCount(); i++) {
            spawnFood();
        }
    }

    private void spawnFood() {
        food.getPoints().add(
                new Point(
                        random.nextInt(gameModel.getWidth()),
                        random.nextInt(gameModel.getHeight())
                )
        );
    }

    @Override
    public void collide(Object model, Point p) {
        if (model instanceof Snake) {
            food.getPoints().remove(p);
            spawnFood();
        }
    }

    @Override
    public Object getModel() {
        return food;
    }
}
