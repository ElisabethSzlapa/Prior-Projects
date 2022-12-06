package ScoreCalculator2;

import Highscores.braille.BrailleLetterException;
import boggle.BoggleStats;

import java.io.IOException;
import java.util.Scanner;

public class ScoreCalculatorMain {
    public static Scanner scanner;
    public void gameMode(String args) {
        if (args.equals("Hard")) {
            DifficultyHard hard = new DifficultyHard();
            hard.DifficultyHard();
        }


    }
    public void summarizeRound() {
        System.out.println(BoggleStats.Player.Human + ":");
        System.out.printf("You score are %d\n", this.pScore );
        System.out.printf("You found %d words\n", this.playerWords.size());
        for (String word : this.playerWords) {
            System.out.print(word);
            System.out.print(", ");
        }
        System.out.println();

        System.out.println("----------------");

        System.out.println(BoggleStats.Player.Computer + ":");
        System.out.printf("Computer score are %d\n", this.cScore);
        System.out.printf("computer found %d words\n", this.computerWords.size());
        for (String word : this.computerWords) {
            System.out.print(word);
            System.out.print(", ");
        }
        System.out.println();

    }
}
