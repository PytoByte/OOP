package game.controller;

import game.model.Collider;
import game.model.GameModel;
import game.model.Point;
import game.model.Walls;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Random;

public class WallsController implements Controller, ColliderControl {
    GameModel gameModel;
    Walls walls;
    Random random = new Random(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));

    public WallsController(GameModel gameModel, Walls walls) {
        this.gameModel = gameModel;
        this.walls = walls;
        restart();
    }

    @Override
    public void collide(Collider model, Point p) {

    }

    @Override
    public Object getModel() {
        return walls;
    }

    @Override
    public void restart() {
        Point p = new Point(
                random.nextInt(gameModel.getWidth()),
                random.nextInt(gameModel.getHeight())
        );
        walls.getPoints().add(p);
    }
}
