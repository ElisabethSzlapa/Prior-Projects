package ScoreCalculator;

import java.util.List;

public class HardMode extends ScoreCalculatorDecorator {

    public HardMode(IScoreCalculator newScore) {
        super(newScore);
    }


    @Override
    public double calculateScore(Double score) {
        return this.calculator.calculateScore(score) * 3;
    }
}
