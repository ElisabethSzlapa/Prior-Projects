package Hint;

import java.util.Set;

public class Hint_character implements Hint_function {
    public static Set<String> word;//all words from the boggle board
    /**
     * Override the hint function
     * only show the first and last character, other parts hide with "*".
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

        //hide the middle part to "*", only show the first and last character
        String replace = hint.substring(1,hint.length()-1);
        String character = hint.replace(replace, "*".repeat(replace.length()));

        return "-- HINT:("+character+") --";
    }
}
