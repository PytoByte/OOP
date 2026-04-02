package game.controller;

import game.GameModel;
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
    public void collide(Object model, Point p) {

    }

    @Override
    public Object getModel() {
        return walls;
    }
}
