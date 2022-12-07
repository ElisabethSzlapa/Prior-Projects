package ScoreCalculator2;

public class DifficultyEasy {
    private double score;

    public void Difficulty_Easy(int pscore) {
        this.score = pscore * 1.5;
    }

    public double getScore() {
        return this.score;
    }
}