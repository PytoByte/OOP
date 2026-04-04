package game.model;

/**
 * Модель состояния игры. Хранит данные о размерах поля, текущем счете
 * и статусе завершения игрового процесса (победа или поражение).
 */
public class GameModel {
    private int width = 20;
    private int height = 15;
    private int score = 0;
    private int scoreToWin = 3;
    private boolean gameOver = false;
    private boolean gameWin = false;

    /**
     * Создает новую модель игры с заданными параметрами поля и условием победы.
     * @param width ширина игрового поля в ячейках.
     * @param height высота игрового поля в ячейках.
     * @param scoreToWin количество очков, которое необходимо набрать для победы.
     */
    public GameModel(int width, int height, int scoreToWin) {
        this.width = width;
        this.height = height;
        this.scoreToWin = scoreToWin;
    }

    /**
     * Увеличивает текущий счет игрока на указанное значение.
     * @param inc количество добавляемых очков.
     */
    public void increaseScore(int inc) {
        this.score += inc;
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