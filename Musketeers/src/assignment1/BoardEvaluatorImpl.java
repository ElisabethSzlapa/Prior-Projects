package assignment1;

public class BoardEvaluatorImpl implements BoardEvaluator {

    /**
     * Calculates a score for the given Board
     * A higher score means the Musketeer is winning
     * A lower score means the Guard is winning
     * Add heuristics to generate a score given a Board
     * @param board Board to calculate the score of
     * @return int Score of the given Board
     */
    @Override
    public int evaluateBoard(Board board) { 
    	int originalGuardCount = 22; //original number of guards on a 5 by 5 board
    	
        return originalGuardCount - board.getGuardCells().size(); //returns the original guard count minus the remaining guards
    }
}
