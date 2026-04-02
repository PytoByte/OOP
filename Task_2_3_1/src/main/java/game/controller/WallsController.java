package game.controller;

import game.model.Collider;
import game.model.GameModel;
import game.model.Point;
import game.model.Walls;

public class WallsController implements Controller, ColliderControl {
    GameModel gameModel;
    Walls walls;

    public WallsController(GameModel gameModel, Walls walls) {
        this.gameModel = gameModel;
        this.walls = walls;
    }

    @Override
    public void collide(Collider model, Point p) {

    }

    @Override
    public Object getModel() {
        return walls;
    }
}
