package ScoreCalculator;

import java.util.List;

abstract class ScoreCalculatorDecorator implements IScoreCalculator {
    protected IScoreCalculator calculator;

    public ScoreCalculatorDecorator(IScoreCalculator calculator){
        this.calculator = calculator;
    }


}

