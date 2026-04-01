package game;

public class GameModel {
    public static final int TILE_SIZE = 30;
    public static final int WIDTH = 20;
    public static final int HEIGHT = 15;
    public static final int T = 3;       // Еда
    public static final int L = 10;      // Длина для победы

    private boolean gameOver = false;
    private boolean gameWin = false;

    public boolean isGameOver() { return gameOver; }
    public boolean isGameWin() { return gameWin; }
}
