package ScoreCalculator;

import java.util.Scanner;

public class CalculatorMain {
    public void main (String[] arg) {
        String game_mode;
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Game mode: ");
            game_mode = scanner.nextLine();
            if(game_mode.equals("hard"))
                break;
            else if (game_mode.equals("normal"))
                break;
            else System.out.println("Please choose a game mode (hard/normal)");


        }
    }
}

