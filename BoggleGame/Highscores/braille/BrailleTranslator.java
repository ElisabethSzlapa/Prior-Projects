package Highscores.braille;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


/**
 * BrailleTranslator class.  A class to translate ASCII to Braille.
 **/
public class BrailleTranslator {

    /**
     * List containing a Braille Translation
     */
    List<List<String>> translation; 
    /**
     * Map to translate characters to Braille
     */
    Map<Character, List<String>> map;

    /**
     * Create a BrailleTranslator.
     */
    public BrailleTranslator() throws IOException {
        this.translation = new ArrayList<List<String>>();
        this.map = new HashMap<>();
        initializeMap();
    }

    /**
     * Print the translation
     *
     * @return String that includes entire translation stored in translation attribute
     */
    public String toString() {
        String retval = "";
        for (List<String> l: this.translation) {
            retval += l.get(0) + "\n" + l.get(1) + "\n" + l.get(2); //three rows for every one line
            retval += "\n";

        }
        this.translation = new ArrayList<>(); //empty for next phrase
        return retval;
    }

    /**
     * Getter method for map.
     *
     * @return the map used for translation
     */
    public Map<Character, List<String>> getMap() {
        return this.map;
    }


    /**
     * Initialize a map to turn ASCII characters into Braille letters.
     * Read in the file "dictionary.txt" and use it to initialize 
     * the map attribute.  This will be used to translate each ASCII
     * character to a list of strings (e.g. a -> {"10", "00", "00"})
     *
     * @throws IOException if file cannot be accessed
     */
    private void initializeMap() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("dictionary.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                List<String> chars = Arrays.asList(line.substring(4,6), line.substring(8, 10), line.substring(12, 14));
                this.map.put(line.charAt(1),chars);
            }
        }
    }

    /**
     * Translate an ASCII line to a line of Braille.
     * To translate a line, iterate over the line and translate each character.
     * Then, concatenate translated characters to form lines of Braille as List<String> objects.
     * (e.g. The string "ab" should result in -> {"1010", "0010", "0000"}.
     * This corresponds to character a ->  {"10", "00", "00"} concatenated with b ->  {"10", "10", "00"}).
     *
     * @param input: the ASCII line of text to be translated.
     */
    public String translateLine(String input) throws BrailleLetterException {
        String charOne = "";
        String charTwo = "";
        String charThree = "";
        List<String> braille = new ArrayList<>();
        for (char letter : input.toCharArray()){
            List<String> brLetter = translateChar(letter);
            if(brLetter == null){
                break;
                //throw new BrailleLetterException("Sorry, that character is not in the dictionary.");
            }
            charOne += brLetter.get(0);
            charTwo += brLetter.get(1);
            charThree += brLetter.get(2);

            braille = Arrays.asList(charOne, charTwo, charThree);

        }
        this.translation.add(braille);
        return  braille.toString();


    }


    /**
     * Translate an ASCII character to a single Braille letter with positions as follows
     *      0 3
     *      1 4
     *      2 5
     *
     * Each position should contain a zero or a one.
     *
     * @param c: the ASCII character to be translated.
     * @return a Braille character translation
     */
    public List<String> translateChar(char c) {
        if(this.map.containsKey(Character.toLowerCase(c))){
            return this.map.get(Character.toLowerCase(c));
        }
        return null;
    }
}
