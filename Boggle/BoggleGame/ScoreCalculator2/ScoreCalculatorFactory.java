package ScoreCalculator2;

public class ScoreCalculatorFactory {
    public double calculateScore(String difficulty, int score) {
        if (difficulty.equals("Hard")) {
            DifficultyHard h = new DifficultyHard();
            h.Difficulty_Hard(score);
            return h.getScore();
        } else if (difficulty.equals("Normal")) {
            DifficultyNormal n = new DifficultyNormal();
            n.Difficulty_Normal(score);
            return n.getScore();
        } else if (difficulty.equals("Easy")) {
            DifficultyEasy e = new DifficultyEasy();
            e.Difficulty_Easy(score);
            return e.getScore();
        }
        return 0;
    }

}
