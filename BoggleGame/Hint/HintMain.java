package Hint;

import boggle.*;

import java.util.Scanner;

public class HintMain {
    private int type_number;
    public static Scanner scanner;

    public void _init_() {
        scanner = new Scanner(System.in);
    }

    public void chose_type(int n) {
        Hint hint_location = new Hint(new Hint_location());
        Hint hint_character = new Hint(new Hint_character());
        Hint hint_word = new Hint(new Hint_word());
        while (true) {
            if (n == 1) {
                hint_location.hint();
            } else if (n == 2) {
                hint_character.hint();
            } else if (n == 3) {
                hint_word.hint();
            } else {
                break;
            }
        }


    }

    public static void main(String[] args) {
        String choiceGrid = scanner.nextLine();
        


    }


}
