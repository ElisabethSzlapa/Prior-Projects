import Highscores.Highscores;
import Highscores.braille.BrailleLetterException;
import boggle.BoggleGame;

import java.io.IOException;

/**
 * The Main class for the first Assignment in CSC207, Fall 2022
 */
public class Main {
    /**
    * Main method. 
    * @param args command line arguments.
    **/
    public static void main(String[] args) throws IOException, BrailleLetterException, InterruptedException {
        BoggleGame b = new BoggleGame();
        b.getScores();

    b.giveInstructions();

        b.playGame();
        b.getScores();
    }

}
