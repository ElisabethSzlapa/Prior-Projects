package assignment1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import assignment1.Piece.Type;

public class Board {
    public int size = 5;

    // 2D Array of Cells for representation of the game board
    public final Cell[][] board = new Cell[size][size];

    private Piece.Type turn;
    private Piece.Type winner;

    /**
     * Create a Board with the current player turn set.
     */
    public Board() {
        this.loadBoard("Boards/Starter.txt");
    }

    /**
     * Create a Board with the current player turn set and a specified board.
     * @param boardFilePath The path to the board file to import (e.g. "Boards/Starter.txt")
     */
    public Board(String boardFilePath) {
        this.loadBoard(boardFilePath);
    }

    /**
     * Creates a Board copy of the given board.
     * @param board Board to copy
     */
    public Board(Board board) {
        this.size = board.size;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                this.board[row][col] = new Cell(board.board[row][col]);
            }
        }
        this.turn = board.turn;
        this.winner = board.winner;
    }

    /**
     * @return the Piece.Type (Muskeeteer or Guard) of the current turn
     */
    public Piece.Type getTurn() {
        return turn;
    }

    /**
     * Get the cell located on the board at the given coordinate.
     * @param coordinate Coordinate to find the cell
     * @return Cell that is located at the given coordinate
     */
    public Cell getCell(Coordinate coordinate) { 
    	for (int i=0; i < size; i++) { //iterates through board until we are at the given coordinates
    		for (int j=0; j < size; j++) {
    			if (board[i][j].getCoordinate().equals(coordinate)) { //returns cell locates at coordinate
        			return board[i][j];
        		}
        	}
    	}
        return null; //returns null if coordinates are never found
    }

    /**
     * @return the game winner Piece.Type (Muskeeteer or Guard) if there is one otherwise null
     */
    public Piece.Type getWinner() {
        return winner;
    }

    /**
     * Gets all the Musketeer cells on the board.
     * @return List of cells
     */
    public List<Cell> getMusketeerCells() { 
    	List<Cell> Musketeers = new ArrayList<>(); //Creates empty arraylist to contain musketeers
    	for (int i=0; i < size; i++) {
    		for (int j=0; j < size; j++) {

    			if (board[i][j].getPiece() != null) { //iterates through all cells and adds any musketeers to Musketeers
    				if (board[i][j].getPiece().getType() == Type.MUSKETEER) {
    					Musketeers.add(board[i][j]);
        			
        		
        			}
    			}
        	}
    	}
        return Musketeers;
    }

    /**
     * Gets all the Guard cells on the board.
     * @return List of cells
     */
    public List<Cell> getGuardCells() { 
    	List<Cell> Guards = new ArrayList<>(); //Creates empty arraylist to contain guards
    	for (int i=0; i < size; i++) {
    		for (int j=0; j < size; j++) {
    			if (board[i][j].getPiece() != null) { //iterates through all cells and adds any guard to Guards
            		if (board[i][j].getPiece().getType() == Type.GUARD) {
            			Guards.add(board[i][j]);
            		
            		}
    			}
        	}
    	}
        return Guards;
    }

    /**
     * Executes the given move on the board and changes turns at the end of the method.
     * @param move a valid move
     */
    public void move(Move move) { 
    	Piece piece = move.fromCell.getPiece(); //taking the piece at from cell
    	Coordinate prior = new Coordinate(move.fromCell.getCoordinate().row, move.fromCell.getCoordinate().col); //taking the coordinates of fromCell
    	this.getCell(prior).removePiece(); //removing the piece at prior
    	this.getCell(move.toCell.getCoordinate()).setPiece(piece); //setting new piece down on the destination of toCell
    	
    	if (this.turn == Type.MUSKETEER) { //switching turns
    		this.turn = Type.GUARD;
    	}
    	else {
    		this.turn = Type.MUSKETEER;
    	}
    }

    /**
     * Undo the move given.
     * @param move Copy of a move that was done and needs to be undone. The move copy has the correct piece info in the
     *             from and to cell fields. Changes turns at the end of the method.
     */
    public void undoMove(Move move) { 
    	Piece piece = move.fromCell.getPiece(); //taking origin piece
    	Piece priorPiece = move.toCell.getPiece(); //taking the piece that was originally at the destination
    	Cell prior = new Cell(move.fromCell); //creating Cell identical to the departure cell
    	Cell destination = new Cell(move.toCell); //creating Cell identical to the destination cell
    	this.getCell(prior.getCoordinate()).setPiece(piece); //finding origin location on board and setting back to piece
    	this.getCell(destination.getCoordinate()).setPiece(priorPiece); //finding destination location on board and placing priorPiece
    	if (this.turn == Type.MUSKETEER) { //switching turns
    		this.turn = Type.GUARD;
    	}
    	this.turn = Type.MUSKETEER;
    }

    /**
     * Checks if the given move is valid. Things to check:
     * (1) the toCell is next to the fromCell
     * (2) the fromCell piece can move onto the toCell piece.
     * @param move a move
     * @return     True, if the move is valid, false otherwise
     */
    public Boolean isValidMove(Move move) { 

    	Piece toMove = move.fromCell.getPiece(); //taking piece we are attempting to move
    	Piece toTake = move.toCell.getPiece(); //taking piece we are attempting to take
    	
    	if (toMove == null) { //if no piece is being moved, the move is invalid
    		return false;
    	}

    	if(toMove.getType() != this.turn) { //if the piece being moved is not the piece whose turn it is, the move is invalid
    		return false;
    	}
    	if(!move.validDistance()) { //calling validDistance on the move, if the distance is not valid, the move is invalid
    		return false;
    	}
    	if(toTake == null) { //if there's nothing at the destination, toMove can be placed there
    		return true;
    	}
    	if(toTake.getType() == Type.MUSKETEER) { //if toTake is a musketeer, the move is invalid as musketeers cannot be taken
    		return false;
    	}
    	
    	if(toMove.getType() == Type.GUARD) { //if there's something at the location and toMove is a guard, the move is invalid
    		return (toTake == null);
    	}
    	
    	return true; //if all tests have been passed, return true
    }

    /**
     * Get all the possible cells that have pieces that can be moved this turn.
     * @return      Cells that can be moved from the given cells
     */
    public List<Cell> getPossibleCells() { 
    	List<Cell> possibleCells = new ArrayList<>(); //creating empty arraylist to contain possible cells
        
    	for (int i=0; i < size; i++) { //iterating through board
    		for (int j=0; j < size; j++) {
        		if (this.getPossibleDestinations(board[i][j]).size() > 0 && (board[i][j].getPiece() != null)) {
        			possibleCells.add(board[i][j]); //if there are possible destinations for the cell and the piece is not invalid, add to possibleCells
        		}
        	}
    	}
        return possibleCells;
    }

    /**
     * Get all the possible cell destinations that is possible to move to from the fromCell.
     * @param fromCell The cell that has the piece that is going to be moved
     * @return List of cells that are possible to get to
     */
    public List<Cell> getPossibleDestinations(Cell fromCell) { 
    	
    	List<Cell> validCells = new ArrayList<>(); //creating new empty arraylist to contain valid cells
    
    	for (int i=0; i < size; i++) { //iterating through every cell on board
    		for (int j=0; j < size; j++) {
    			Move attempt = new Move(fromCell, board[i][j]); //creates a move to attempt
        		if (this.isValidMove(attempt)) { //if attempt is valid, add to validCells
        			validCells.add(board[i][j]);
        		
        		}
        	}
    	}
    	return validCells;
    }

    /**
     * Get all the possible moves that can be made this turn.
     * @return List of moves that can be made this turn
     */
    public List<Move> getPossibleMoves() { 
    	List<Move> possibleMoves = new ArrayList<>(); //creates empty arraylist to contain possible moves
        
    	for (int i=0; i < size; i++) { //iterates through every cell on board
    		for (int j=0; j < size; j++) {
    	    	for (int k=0; k < size; k++) { //iterates through every cell on board
    	    		for (int l=0; l < size; l++) {
    	    			Move attempt = new Move(board[i][j], board[k][l]); //creates a move between the two cells
    	        		if (this.isValidMove(attempt)) { //if attempt is valid, ass to possibleMoves
    	        			possibleMoves.add(attempt);
    	        		
    	        		}
    	        	}
    	    	}
        	}
    	}
        return possibleMoves;
    }

    /**
     * Checks if the game is over and sets the winner if there is one.
     * @return True, if the game is over, false otherwise.
     */
    public boolean isGameOver() { 
    	if (this.getGuardCells().size() == 0) { //if there are no guard cells, musketeer wins!
    		this.winner = Type.MUSKETEER;
    		return true;
    	}
    	int musketeerRow = -1;  //setting musketeerRow and musketeerColumn to impossible values to start with
    	int musketeerColumn = -1;
    	boolean secondMusketeerDecision = false; //a boolean to see if we reached the second musketeer yet
    	for(Cell musketeer : this.getMusketeerCells()) { //iterating through all musketeer cells
    		if(musketeerRow == -1 && musketeerColumn == -1) { //if this is the first pass, set musketeerRow and musketeerColumn to the musketeers values
    			musketeerRow = musketeer.getCoordinate().row;
    			musketeerColumn = musketeer.getCoordinate().col;
    		}
    		else if (!secondMusketeerDecision) { //else if we're on our second pass
    			if(musketeerRow == musketeer.getCoordinate().row) { //if the new musketeer matches musketeerRow, musketeerColumn is no longer a valid match
    				musketeerColumn = -1;
    			}
    			if(musketeerColumn == musketeer.getCoordinate().col) { //if the new musketeer matches musketeerColumn, musketeerRow is no longer a valid match
    				musketeerRow = -1;
    			}
    			secondMusketeerDecision = true; //setting that we have tested the second musketeer
    		}
    		if (musketeerRow != musketeer.getCoordinate().row  && musketeerColumn != musketeer.getCoordinate().col){ //if musketeer does not match the chosen column, the musketeers are not lined up and the game continues
    			return false;
    		}
    	}
    	
    	this.winner = Type.GUARD; //if all musketeers are alligned, the game is over and the guards won
		return true;
    	
    	
    }

    /**
     * Saves the current board state to the boards directory
     */
    public void saveBoard() {
        String filePath = String.format("boards/%s.txt",
                new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
        File file = new File(filePath);

        try {
            file.createNewFile();
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            writer.write(turn.getType() + "\n");
            for (Cell[] row: board) {
                StringBuilder line = new StringBuilder();
                for (Cell cell: row) {
                    if (cell.getPiece() != null) {
                        line.append(cell.getPiece().getSymbol());
                    } else {
                        line.append("_");
                    }
                    line.append(" ");
                }
                writer.write(line.toString().strip() + "\n");
            }
            writer.close();
            System.out.printf("Saved board to %s.\n", filePath);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.printf("Failed to save board to %s.\n", filePath);
        }
    }

    @Override
    public String toString() {
        StringBuilder boardStr = new StringBuilder("  | A B C D E\n");
        boardStr.append("--+----------\n");
        for (int i = 0; i < size; i++) {
            boardStr.append(i + 1).append(" | ");
            for (int j = 0; j < size; j++) {
                Cell cell = board[i][j];
                boardStr.append(cell).append(" ");
            }
            boardStr.append("\n");
        }
        return boardStr.toString();
    }

    /**
     * Loads a board file from a file path.
     * @param filePath The path to the board file to load (e.g. "Boards/Starter.txt")
     */
    private void loadBoard(String filePath) {
        File file = new File(filePath);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.err.printf("File at %s not found.", filePath);
            System.exit(1);
        }

        turn = Piece.Type.valueOf(scanner.nextLine().toUpperCase());

        int row = 0, col = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] pieces = line.trim().split(" ");
            for (String piece: pieces) {
                Cell cell = new Cell(new Coordinate(row, col));
                switch (piece) {
                    case "O" -> cell.setPiece(new Guard());
                    case "X" -> cell.setPiece(new Musketeer());
                    default -> cell.setPiece(null);
                }
                this.board[row][col] = cell;
                col += 1;
            }
            col = 0;
            row += 1;
        }
        scanner.close();
        System.out.printf("Loaded board from %s.\n", filePath);
    }
}
