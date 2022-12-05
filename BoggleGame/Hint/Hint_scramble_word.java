package Hint;

import java.util.*;

public class Hint_scramble_word implements Hint_function{

    @Override
    public String hint() {

        //get all word from the boggle board
        HintMain h = new HintMain();
        Set<String> word = h.get_word();

        //get one word and return it to player
        Object[] word_array = word.toArray();
        String hint = word_array[0].toString();
        //remove the word from the set
        word.remove(hint);

        //shuffle the word
        ArrayList<Character>  word_list = new ArrayList<Character>();
        for (int i = 0; i < hint.length(); i++) {
            word_list.add(hint.charAt(i));
        }
        Collections.shuffle(word_list, new Random());

        return "-- HINT:"+ word_list +" --";
    }
}
