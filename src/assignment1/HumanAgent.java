package assignment1;

import java.util.Scanner;

import assignment1.ThreeMusketeers.GameMode;
import assignment1.Piece;
import assignment1.Piece.Type;

public class HumanAgent extends Agent {

    private final Scanner scanner = new Scanner(System.in);
    public HumanAgent(Board board) {
        super(board);
    }

    /**
     * Asks the human for a move with from and to coordinates and makes sure its valid.
     * Create a Move object with the chosen fromCell and toCell
     * @return the valid human inputted Move
     */
    @Override
    public Move getMove() {

    	
    	System.out.println("""
                Type the coordinates of your piece's current location"""); //getting coordinates of the current location
    	System.out.print("""
    			Type the column of your piece's current location""");
    	int inputCol = letterToNumber(getColumn()); //calling getColumn, and proceeding to convert the returned value using LetterToNumber
    	System.out.print("""
                Type the row of your piece's current location""");
    	int inputRow = getRow(); //calling getRow
    	System.out.println("Type the coordinates of the location you want your piece moved to"); //getting coordinates of the desired location
    	System.out.print("""
    			Type the column of the desired location""");
    	int outputCol = letterToNumber(getColumn()); //calling getColumn, and proceeding to convert the returned value using LetterToNumber
    	System.out.print("""
    			Type the row of the desired location""");
    	int outputRow = getRow(); //calling getRow
    	
    	
    	
    	Coordinate inputCoord = new Coordinate(inputRow, inputCol); //creating desired origin coordinates
    	Cell inputCell = new Cell(inputCoord); //creating cell using inputCoord
    	Coordinate outputCoord = new Coordinate(outputRow, outputCol); //creating desired destination coordinates
    	Cell outputCell = new Cell(outputCoord); //creating cell using outputCoord
    	
    	Piece inputPiece = board.getCell(inputCoord).getPiece(); //creating piece using piece found on board at that coordinate
    	Piece outputPiece = board.getCell(outputCoord).getPiece(); //creating piece using piece found on board at that coordinate
    	
    	inputCell.setPiece(inputPiece); //setting input cell to the inputPiece
    	outputCell.setPiece(outputPiece); //setting output cell to the outputPiece\
    	
    	
    	Move attempt = new Move(inputCell, outputCell); //creating move from inputCell to outputCell
    	System.out.println(attempt);
    	if (!board.isValidMove(attempt)) { //returning error message if attempt is not a valid move
    		System.out.println("""
                    Ruh roh! invalid move! try again!!!""");
    		return getMove(); //trying again
    	}
    	
    	return attempt; //returning successful move
    
    }

	private int letterToNumber(String input) { //converts string input to the column it represents
    	if (input.equals("A")) {
    		return 0;
    	}
    	if (input.equals("B")) {
    		return 1;
    	}
    	if (input.equals("C")) {
    		return 2;
    	}
    	if (input.equals("D")) {
    		return 3;
    	}
    	if (input.equals("E")) {

    		return 4;
    	}
    	return 0;
    }
    
    private String getColumn() { //takes an input of a, b, c, d, or e to specify column, and capitalizes it
        System.out.printf("Enter A, B, C, D, or E: ");
        while (!scanner.hasNext("[AaBbCcDdEe]")) {
            System.out.print("Invalid option. Enter A, B, C, D, or E: ");
            scanner.next();
        }
            return scanner.next().toUpperCase();
    }
    private int getRow() { //takes an input of 1, 2, 3, 4, or 5 to specify row
        System.out.printf("Enter 1, 2, 3, 4, or 5: ");
        while (!scanner.hasNextInt()) {
                System.out.print("Invalid option. Enter 1, 2, 3, 4, or 5: ");
                scanner.next();
            }
            final int mode = scanner.nextInt();
            if (mode < 1 || mode > 5) {
                System.out.println("Invalid option.");
                return getRow();
            }
            return mode - 1; //subtracts 1 to represent the array beginning at 0
    }
}
