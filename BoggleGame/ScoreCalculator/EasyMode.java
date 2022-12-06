package ScoreCalculator;

import java.util.List;

public class EasyMode extends ScoreCalculatorDecorator {

    public EasyMode(IScoreCalculator newScore) {
        super(newScore);
    }


    @Override
    public double calculateScore(Double score) {
        return this.calculator.calculateScore(score);
    }
}
