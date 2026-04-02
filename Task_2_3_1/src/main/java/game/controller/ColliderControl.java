package game.controller;

import game.model.Collider;
import game.model.Point;

public interface ColliderControl {
    void collide(Collider model, Point p);
}
