package game.controller;

public interface GameTimer {
    void play();
    void stop();
    void setOnTick(Runnable action);
}