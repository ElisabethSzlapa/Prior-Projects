package ScoreCalculator;

import java.util.List;

public class NormalMode extends ScoreCalculatorDecorator {

    public NormalMode(IScoreCalculator newScore) {
        super(newScore);
    }

    @Override
    public double calculateScore(Double score) {
        return this.calculator.calculateScore(score) * 2;
    }
}
