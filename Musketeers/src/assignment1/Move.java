package assignment1;

public class Move {
    final Cell fromCell;
    final Cell toCell;

    /**
     * Construct a new Move
     * A Move represents moving a Piece in fromCell to toCell
     * @param fromCell the Cell the Piece is in
     * @param toCell the Cell the Piece will move to 
     */
    public Move(final Cell fromCell, final Cell toCell) {
        this.fromCell = fromCell;
        this.toCell = toCell;
    }

    /**
     * Create a copy of a Move
     * @param move a Move to make a copy of
     */
    public Move(Move move) {
        this.fromCell = new Cell(move.fromCell);
        this.toCell = new Cell(move.toCell);
    }
    /**
     * Tests the validity of a move, by ensuring the pieces are adjacent.
     * returns the validity as a boolean
     */
    public Boolean validDistance() {
    	
    	if (-1 == this.toCell.getCoordinate().row - this.fromCell.getCoordinate().row || this.toCell.getCoordinate().row - this.fromCell.getCoordinate().row == 1) //Checks if cells are not vertically adjacent by one space
    	{
    		if (0 == this.toCell.getCoordinate().col - this.fromCell.getCoordinate().col) //ensures that cell are therefore in the same column, keeping total distance 1 unit
        	{
        		return true;
        	}
    	}
    	if (0 == this.toCell.getCoordinate().row - this.fromCell.getCoordinate().row) //Checks if cells are horizontally adjacent 
    	{
    		if (-1 == this.toCell.getCoordinate().col - this.fromCell.getCoordinate().col || this.toCell.getCoordinate().col - this.fromCell.getCoordinate().col == 1) //ensures cells are only seperated by one space horizontally, keeping total distance 1 uniti
        	{
        		return true;
        	}
    	}
    	return false;
    }

    @Override
    public String toString() {
        return String.format("(%s) -> (%s)", fromCell.getCoordinate(), toCell.getCoordinate());
    }
}
