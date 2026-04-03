package game.model;

public class GameModel {
    private int tileSize = 30;
    private int width = 20;
    private int height = 15;
    private int score = 0;
    private int scoreToWin = 3;
    private boolean gameOver = false;
    private boolean gameWin = false;

    public GameModel(int width, int height, int scoreToWin) {
        this.width = width;
        this.height = height;
        this.scoreToWin = scoreToWin;
    }

    public int getTileSize() {
        return tileSize;
    }

    public void setTileSize(int tileSize) {
        this.tileSize = tileSize;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getScore() {
        return score;
    }

    public void increaseScore(int inc) {
        this.score += inc;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScoreToWin() {
        return scoreToWin;
    }

    public void setScoreToWin(int scoreToWin) {
        this.scoreToWin = scoreToWin;
    }

    public boolean getGameOver() {
        return gameOver;
    }

    public boolean getGameWin() {
        return gameWin;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void setGameWin(boolean gameWin) {
        this.gameWin = gameWin;
    }
}
