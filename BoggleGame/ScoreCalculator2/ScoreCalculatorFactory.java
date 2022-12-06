package ScoreCalculator2;

public class ScoreCalculatorFactory {
    public ScoreCalculator calculateScore(String difficulty) {
        if (difficulty.equals("Hard")) {
            return new DifficultyHard();
        } else if (difficulty.equals("Normal")) {
            return new DifficultyNormal();

        } else if (difficulty.equals("Easy")) {
            return new DifficultyEasy();

        }else
            return null;
    }
}
