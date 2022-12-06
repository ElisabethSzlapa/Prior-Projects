package ScoreCalculator2;
import boggle.*;

public abstract class ScoreCalculator {
    BoggleStats getValue = new BoggleStats();


    private double bonus_score;



    public double getBonus(){return bonus_score;}
    public void setScore(double newScore){bonus_score = newScore; }


}
