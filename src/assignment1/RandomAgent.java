package assignment1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import assignment1.Piece.Type;

public class RandomAgent extends Agent {

    public RandomAgent(Board board) {
        super(board);
    }

    /**
     * Gets a valid random move the RandomAgent can do.
     * @return a valid Move that the RandomAgent can perform on the Board
     */
    @Override
    public Move getMove() {
    	Piece.Type agentPiece = board.getTurn(); //get the turn to determine piece type
    	List<Cell> playablePieces = new ArrayList<>(); //creates empty arraylist to contain playable pieces
    	List<Cell> possibleDestinations = new ArrayList<>(); //creates empty arraylist to contain possible destinations
    	List<Move> playableMoves = new ArrayList<>(); //creates empty arraylist to contain playable moves
    	if (agentPiece == Type.MUSKETEER) { //if agentPiece is musketeer, playable pieces are all musketeers
    		playablePieces = board.getMusketeerCells();
    	}
    	if (agentPiece == Type.GUARD) { //if agentPiece is guard, playable pieces are all guards
    		playablePieces = board.getGuardCells();
    	}
    	for (Cell cell : playablePieces) { //iterates through cells in playablePieces
    		possibleDestinations = board.getPossibleDestinations(cell); //adds each cell's possible destinations to possibleDestinations
    		for (Cell destination : possibleDestinations) { //iterates through cells in possibleDestinations
    			Move playable = new Move(cell, destination); //creates move from cell to destination
    			playableMoves.add(playable); //adds move to playableMoves
    		}
    	}
    	int randomMove = (int) Math.round(Math.random() * playableMoves.size()); //gets a random number from 0-playableMoves.size()
    	if(randomMove == playableMoves.size()) { //if randomMove is equal to the size of playableMoves, subtract 1 to account for range staring at 0 and not including playableMoves.size(
    		randomMove -= 1;
    	}
    	return playableMoves.get(randomMove); //returns randomMove from playableMoves
    }

}
