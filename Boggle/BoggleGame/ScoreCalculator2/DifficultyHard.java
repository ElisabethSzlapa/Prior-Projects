package ScoreCalculator2;

public class DifficultyHard{
    private double score;
    public void Difficulty_Hard(int pscore){
        this.score = pscore * 3;
    }

    public double getScore(){
        return this.score;
    }


}

