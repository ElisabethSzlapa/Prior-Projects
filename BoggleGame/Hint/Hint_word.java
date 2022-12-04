package Hint;

import boggle.BoggleGame;

import java.util.Scanner;
import java.util.Set;

public class Hint_word implements Hint_function {

    public static Set<String> word;//all words from the boggle board

    /**
     * Override the hint function
     * This method should return a word from word set
     * @return String
     */
    @Override
    public String hint() {

        //get all word from the boggle board
        HintMain h = new HintMain();
        word = h.get_word();

        //get one word and return it to player
        Object[] word_array = word.toArray();
        String hint = word_array[0].toString();

        //remove the word from the set
        word.remove(hint);

        return "-- HINT:("+hint+") --";
    }
}
