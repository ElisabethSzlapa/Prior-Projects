package Hint;

import boggle.*;

import java.util.*;

public class HintMain {
    public static Scanner scanner;
    public static Set<String> allword = new HashSet<String>();
    public static ArrayList<Position> word_position = new ArrayList<Position>();

    /**
     * User choice the hint that they want
     * 1-get a word location
     * 2-get the start character and end character form a word
     * 2-get a word
     *
     * @param ct String the choice of different hint
     */
    public static void chose_type(String ct) {
        if (ct.equals("1")) {
            //initialize the hint_location
            //show the hint for player
            Hint hint_location = new Hint(new Hint_location());
            System.out.println(hint_location.hint());
        } else if (ct.equals("2")) {
            //initialize the hint_character
            //show the hint for player
            Hint hint_character = new Hint(new Hint_character());
            System.out.println(hint_character.hint());
        } else if (ct.equals("3")) {
            //initialize the hint_word
            //show the hint for player
            Hint hint_word = new Hint(new Hint_word());
            System.out.println(hint_word.hint());
        }
        System.out.println("☆---------------------☆----------------------☆");

    }

    /**
     * Show the information user until user choose a hint or exit
     */
    public void main() {
        scanner = new Scanner(System.in);
        System.out.println("☆--------------------HINT---------------------☆");
        System.out.println("1-Get a position of a word");
        System.out.println("2-Get the first and last character");
        System.out.println("3-Get a word");
        System.out.println("E-Exit hint");
        System.out.println("Please enter 1 2 or 3 to get a hint");
        String choicetype = scanner.nextLine().toUpperCase();
        // System.out.println(ty); debug
        while (true) {
            // player enter 1 2 or 3 to get the corresponding hint
            if (choicetype.equals("1") || choicetype.equals("2") || choicetype.equals("3")) {
                chose_type(choicetype);
                break;
            } else if (choicetype.equals("E")) {// exit the hint when user do not want get a hint
                System.out.println("☆-----------------EXIT  HINT------------------☆");
                break;
            } else {//continue to ask user enter a choice when player enter other
                System.out.println("please enter '1' '2' '3' or 'E' to exit hint");
                choicetype = scanner.nextLine().toUpperCase();
            }

        }

    }

    /**
     * Add all word that find from boggle board to the allword set.
     *
     * @param words A mutable list of all legal words that can be found, given the boggleGrid grid letters
     */
    public void addwords(Map<String, ArrayList<Position>> words) {
        // if(!allword.isEmpty()) System.out.println("--not clear--"); //debug
        allword.clear();
        // System.out.println("--clear--"); //debug
        allword.addAll(words.keySet());
    }

    public void addposition(ArrayList<Position> po){
        if (!word_position.isEmpty()) word_position.clear();
        word_position.addAll(po);
    }



    /**
     * get the allword set when other call this method.
     *
     * @return allword
     */
    public Set<String> get_word() {
        return allword;
    }

    /**
     * get the word position when other call this method.
     * @return word_position
     */
    public ArrayList<Position> get_position(){
        return word_position;
    }

    public static void main(String[] args) {
        //HintMain h = new HintMain();
        //h.main();
        BoggleGame b = new BoggleGame();
        String wo = "Y G S A K E T A E T D E H I I O M P R M C N U P O".replaceAll(" ", "");
        b.playRound(5, wo);
        System.out.println(word_position);
        System.out.println();
        for (Position w : word_position) {
            System.out.println(w);
        }
    }


}

