package game.controller;

import javafx.scene.Scene;

public interface Controller {
    Object getModel();
    default void setupEvents(Scene scene) {};
    default void update() {};
    void restart();
}
