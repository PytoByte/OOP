package game.model;

public class GameModel {
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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
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
