package boggle;

import Highscores.Highscores;
import Highscores.braille.BrailleLetterException;
import java.io.IOException;
import Hint.*;
import User.User;
import State.*;

import java.sql.Array;
import java.util.*;
/**
 * The BoggleGame class for the first Assignment in CSC207, Fall 2022
 */
public class BoggleGame {

    /**
     * scanner used to interact with the user via console
     */
    public Scanner scanner;
    /**
     * stores game statistics
     */
    private BoggleStats gameStats;
    private Highscores highScores;
    public Date start_time = new Date();
    public ArrayList<Date> start_times = new ArrayList<>();
    private static int tyep_game = 0;
    public long time_stored = 0;
    public long middle_time = 0;
    private User user;
    /**
     * dice used to randomize letter assignments for a small grid
     */
    private final String[] dice_easy_grid = //dice specifications, for small and large grids
            {"AAEEGN", "ABBJOO", "ACHOPS", "AFFKPS", "AOOTTW", "CIMOTU", "DEILRX", "DELRVY",
                    "DISTTY", "EEGHNW", "EEINSU", "EHRTVW", "EIOSST", "ELRTTY", "HIMNQU", "HLNNRZ"};
    /**
     * dice used to randomize letter assignments for a big grid
     */
    private final String[] dice_normal_grid =
            {"AAAFRS", "AAEEEE", "AAFIRS", "ADENNN", "AEEEEM", "AEEGMU", "AEGMNN", "AFIRSY",
                    "BJKQXZ", "CCNSTW", "CEIILT", "CEILPT", "CEIPST", "DDLNOR", "DDHNOT", "DHHLOR",
                    "DHLNOR", "EIIITT", "EMOTTT", "ENSSSU", "FIPRSY", "GORRVW", "HIPRRY", "NOOTUW", "OOOTTU"};

    private final String[] dice_hard_grid =
            {"POISYQ", "EADXWE", "SDXGHU", "SSSTNJ", "ADDTMLP", "PPIYOM", "NNDYWTR", "CCCSER",
                    "BBSBGH", "DEWWRT", "GGKMHT", "SHHMRT", "QWEXEN", "RHYWEE", "GBNGHH", "LKUPWE",
                    "AQAWRE", "BBCVGA", "GIHTWQ", "HWETTU", "FFDOMN", "QWSSSW", "GHACBN", "AWWYUI", "POMKLK",
                    "ASBGRT", "AXCDSE", "ASERTH", "YJUIOM", "ASSAWT", "FFRETU", "BVBNHG", "YHMJUK",
                    "AWSCBH", "DDQWRR", "THNBNM"};


    private List<String> guessed_words;
    /*
     * BoggleGame constructor
     */
    public BoggleGame() {
        this.scanner = new Scanner(System.in);
        this.gameStats = new BoggleStats();
        this.highScores = new Highscores();

        guessed_words = new ArrayList<>();



    }

    /*
     * Provide instructions to the user, so they know how to play the game.
     */
    public void giveInstructions() {
        System.out.println("The Boggle board contains a grid of letters that are randomly placed.");
        System.out.println("We're both going to try to find words in this grid by joining the letters.");
        System.out.println("You can form a word by connecting adjoining letters on the grid.");
        System.out.println("Two letters adjoin if they are next to each other horizontally, ");
        System.out.println("vertically, or diagonally. The words you find must be at least 4 letters long, ");
        System.out.println("and you can't use a letter twice in any single word. Your points ");
        System.out.println("will be based on word length: a 4-letter word is worth 1 point, 5-letter");
        System.out.println("words earn 2 points, and so on. After you find as many words as you can,");
        System.out.println("I will find all the remaining words.");
        System.out.println("\nHit return when you're ready...");
    }

    public ArrayList<Date> getStart_times(){
        return start_times;
    }
    public void setStart_times(ArrayList<Date> start_times){
        this.start_times = start_times;
    }
    /*
     * Gets information from the user to initialize a new Boggle game.
     * It will loop until the user indicates they are done playing.
     */
    public void playGame() throws InterruptedException {
        User u = new User();
        int pscor = 0;
        int cscor = 0;
        long timrec = 0;
        int boardSize = 0;
        while (true) {
            System.out.println("Enter your name (not case sensitive)");
            String playerName = scanner.nextLine();
            u.setUser(playerName);
            this.user = u;
            if(u.check_registered()) {
                System.out.println("Enter '1' to load states, '2' to start over.");
                String choiceLoad = scanner.nextLine();
                while (!choiceLoad.equals("1") && !choiceLoad.equals("2")) {
                    System.out.println("Please try again.");
                    System.out.println("Enter '1' to load states, '2' to start over.");
                    choiceLoad = scanner.nextLine();
                    if(choiceLoad.equals("1")){
                        pscor += u.get_value().getpScoreTotal();
                        cscor += u.get_value().getcScoreTotal();
                    }
                }
            }
            System.out.println("Enter 'hard' to play on a hard (6x6) grid; 'normal' to play on a normal (5x5); 'easy' to play on a normal (4x4):");
            String choiceGrid = scanner.nextLine();

            //get grid size preference
            if (choiceGrid == "") break; //end game if user inputs nothing
            while (!choiceGrid.equals("hard") && !choiceGrid.equals("normal") && !choiceGrid.equals("easy")) {
                System.out.println("Please try again.");
                System.out.println("Enter 'hard' to play on a hard (6x6) grid; 'normal' to play on a normal (5x5); 'easy' to play on a normal (4x4):");
                choiceGrid = scanner.nextLine();
            }

            if (choiceGrid.equals("hard")) {
                tyep_game = 1;
                boardSize = 6;

            }
            if (choiceGrid.equals("normal")) {
                tyep_game = 2;
                boardSize = 5;

            }
            if (choiceGrid.equals("easy")) {
                tyep_game = 3;
                boardSize = 4;

            }

            //get letter choice preference
            System.out.println("Enter 1 to randomly assign letters to the grid; 2 to provide your own.");
            String choiceLetters = scanner.nextLine();

            if (choiceLetters == "") break; //end game if user inputs nothing
            while (!choiceLetters.equals("1") && !choiceLetters.equals("2")) {
                System.out.println("Please try again.");
                System.out.println("Enter 1 to randomly assign letters to the grid; 2 to provide your own.");
                choiceLetters = scanner.nextLine();
            }

            if (choiceLetters.equals("1")) {
                playRound(boardSize, randomizeLetters(boardSize));
            } else {
                System.out.println("Input a list of " + boardSize * boardSize + " letters:");
                choiceLetters = scanner.nextLine();
                while (!(choiceLetters.length() == boardSize * boardSize)) {
                    System.out.println("Sorry, bad input. Please try again.");
                    System.out.println("Input a list of " + boardSize * boardSize + " letters:");
                    choiceLetters = scanner.nextLine();
                }
                playRound(boardSize, choiceLetters.toUpperCase());

            }

            //round is over! So, store the statistics, and end the round.
            this.gameStats.summarizeRound(tyep_game);
            System.out.println("press '1' to save, press '2' to continue without saving");
            String choiceSave = scanner.nextLine();
            if(choiceSave.equals("1")){
                this.gameStats.setpScoreTotal(pscor);
                this.gameStats.setcScoreTotal(cscor);
                u.addUser();
            }
            this.gameStats.endRound(tyep_game);
            this.highScores.scoreComparer(this.gameStats, u); //calls highscores

            //Shall we repeat?
            System.out.println("Play again? Type 'Y' or 'N'");
            String choiceRepeat = scanner.nextLine().toUpperCase();

            if (choiceRepeat == "") break; //end game if user inputs nothing
            while (!choiceRepeat.equals("Y") && !choiceRepeat.equals("N")) {
                System.out.println("Please try again.");
                System.out.println("Play again? Type 'Y' or 'N'");
                choiceRepeat = scanner.nextLine().toUpperCase();
            }

            if (choiceRepeat == "" || choiceRepeat.equals("N")) break; //end game if user inputs nothing4

        }

        //we are done with the game! So, summarize all the play that has transpired and exit.
        this.gameStats.summarizeGame();
        System.out.println("Thanks for playing!");
    }

    /*
     * Play a round of Boggle.
     * This initializes the main objects: the board, the dictionary, the map of all
     * words on the board, and the set of words found by the user. These objects are
     * passed by reference from here to many other functions.
     */
    public void playRound(int size, String letters) {
        //step 1. initialize the grid
        BoggleGrid grid = new BoggleGrid(size);
        grid.initalizeBoard(letters);
        //step 2. initialize the dictionary of legal words
        Dictionary boggleDict = new Dictionary("wordlist.txt"); //you may have to change the path to the wordlist, depending on where you place it.
        //step 3. find all legal words on the board, given the dictionary and grid arrangement.
        Map<String, ArrayList<Position>> allWords = new HashMap<String, ArrayList<Position>>();
        findAllWords(allWords, boggleDict, grid);
        //initialize the hint and add all words in hint list
        HintMain h = new HintMain();
        h.addwords(allWords);
        //Record the starting time
        this.start_time = new Date(System.currentTimeMillis());

        //step 4. allow the user to try to find some words on the grid
        humanMove(grid, allWords);
        //step 5. allow the computer to identify remaining words
        computerMove(allWords);
    }

    /*
     * This method should return a String of letters (length 16 or 25 depending on the size of the grid).
     * There will be one letter per grid position, and they will be organized left to right,
     * top to bottom. A strategy to make this string of letters is as follows:
     * -- Assign a one of the dice to each grid position (i.e. dice_big_grid or dice_small_grid)
     * -- "Shuffle" the positions of the dice to randomize the grid positions they are assigned to
     * -- Randomly select one of the letters on the given die at each grid position to determine
     *    the letter at the given position
     *
     * @return String a String of random letters (length 16 or 25 depending on the size of the grid)
     */
    private String randomizeLetters(int size) {
        Random random = new Random();
        String[] dice_grid;
        //choice the small or big grid
        if (size * size == this.dice_hard_grid.length) {
            dice_grid = dice_hard_grid;
        }
        else if (size * size == this.dice_normal_grid.length) {
            dice_grid = dice_normal_grid;
        }
        else {
            dice_grid = dice_easy_grid;
        }

        List<String> diceList = new ArrayList<String>(Arrays.asList(dice_grid));
        Collections.shuffle(diceList);//shuffle the grid
        StringBuilder letter = new StringBuilder();
        //random choice the dice face
        for (String s : diceList) {
            int dice_face = random.nextInt(6);
            letter.append(s.charAt(dice_face));
        }
        return letter.toString();
    }
    public Date getStart_time(){
        return this.start_time;
    }


    /*
     * This should be a recursive function that finds all valid words on the boggle board.
     * Every word should be valid (i.e. in the boggleDict) and of length 4 or more.
     * Words that are found should be entered into the allWords HashMap.  This HashMap
     * will be consulted as we play the game.
     *
     * Note that this function will be a recursive function.  You may want to write
     * a wrapper for your recursion. Note that every legal word on the Boggle grid will correspond to
     * a list of grid positions on the board, and that the Position class can be used to represent these
     * positions. The strategy you will likely want to use when you write your recursion is as follows:
     * -- At every Position on the grid:
     * ---- add the Position of that point to a list of stored positions
     * ---- if your list of stored positions is >= 4, add the corresponding word to the allWords Map
     * ---- recursively search for valid, adjacent grid Positions to add to your list of stored positions.
     * ---- Note that a valid Position to add to your list will be one that is either horizontal, diagonal, or
     *      vertically touching the current Position
     * ---- Note also that a valid Position to add to your list will be one that, in conjunction with those
     *      Positions that precede it, form a legal PREFIX to a word in the Dictionary (this is important!)
     * ---- Use the "isPrefix" method in the Dictionary class to help you out here!!
     * ---- Positions that already exist in your list of stored positions will also be invalid.
     * ---- You'll be finished when you have checked EVERY possible list of Positions on the board, to see
     *      if they can be used to form a valid word in the dictionary.
     * ---- Food for thought: If there are N Positions on the grid, how many possible lists of positions
     *      might we need to evaluate?
     *
     * @param allWords A mutable list of all legal words that can be found, given the boggleGrid grid letters
     * @param boggleDict A dictionary of legal words
     * @param boggleGrid A boggle grid, with a letter at each position on the grid
     */
    private void findAllWords(Map<String, ArrayList<Position>> allWords, Dictionary boggleDict, BoggleGrid boggleGrid) {

        ArrayList<Position> wordPosition = new ArrayList<Position>();

        for (int row = 0; row < boggleGrid.numRows(); row++) {
            for (int col = 0; col < boggleGrid.numCols(); col++) {
                Position new_position = new Position(row, col);

                //add the position of initial letter to list
                wordPosition.add(new_position);

                //String initial_letter = Character.toString(boggleGrid.getCharAt(row, col));

                //recurse for searching words
                searchWords(allWords, boggleDict, boggleGrid, row, col, "" + boggleGrid.getCharAt(row, col), wordPosition);

                //clear the list for next new word
                wordPosition.clear();
            }
        }
    }

    private void searchWords(Map<String, ArrayList<Position>> allWords, Dictionary boggleDict, BoggleGrid boggleGrid,
                             int row, int col, String word, ArrayList<Position> wordPosition) {


        // base function, if current word is not prefix
        if (!boggleDict.isPrefix(word)) {
            return;
        }

        // if find a correct word
        if (word.length() >= 4 && boggleDict.containsWord(word)) {
            allWords.put(word.toUpperCase(), new ArrayList<Position>(wordPosition));
        }

        int[][] row_col_check = {{-1, -1}, {-1, 0}, {-1, 1},
                {0, -1}, {0, 0}, {0, 1},
                {1, -1}, {1, 0}, {1, 1}};
        int next_row = 0, next_col = 0;
        Position next_position;

        for (int[] ints : row_col_check) {
            boolean isrow = true;
            for (int anInt : ints) {
                if (isrow) {
                    next_row = row + anInt;
                    isrow = false;
                } else {
                    next_col = col + anInt;
                }
            }
            //if next position is out of map, skip it
            if (next_row < 0 || next_row >= boggleGrid.numRows() || next_col < 0 || next_col >= boggleGrid.numCols()) {
                continue;
            }

            next_position = new Position(next_row, next_col);

            //if already have the position, skip it
            boolean already_have = false;
            for (Position p : wordPosition) {
                if (p.getRow() == next_row && p.getCol() == next_col) {
                    already_have = true;
                    break;
                }
            }
            if (already_have) {
                continue;
            }
            // add the position in list.
            wordPosition.add(next_position);
            // recurse, search for next character
            searchWords(allWords, boggleDict, boggleGrid, next_row, next_col, word + boggleGrid.getCharAt(next_row, next_col), wordPosition);
            //the position is not correct, so remove it from list.
            wordPosition.remove(next_position);

        }


    }

    /*
     * Gets words from the user.  As words are input, check to see that they are valid.
     * If yes, add the word to the player's word list (in boggleStats) and increment
     * the player's score (in boggleStats).
     * End the turn once the user hits return (with no word).
     *
     * @param board The boggle board
     * @param allWords A mutable list of all legal words that can be found, given the boggleGrid grid letters
     */

    private void humanMove(BoggleGrid board, Map<String, ArrayList<Position>> allWords) {
        System.out.println("It's your turn to find some words!");
        Controller control = new Controller();
        control.setTy(tyep_game);
        while (true) {
            System.out.println("Enter 'H' to get a hint, 'T' to see time used, 'P' to pause the timer, 'R' to resume timer, and 'EX' to exit.");
            //step 1. Print the board for the user, so they can scan it for words
            System.out.println(board);
            //step 2. Get an input (a word) from the user via the console
            String input_word = this.scanner.nextLine().toUpperCase();
            //step 3. Check to see if it is valid (note validity checks should be case-insensitive)

            //Hint function  when user need a hint to help user play the game
            if (input_word.equals("H")) {
                HintMain h = new HintMain();
                h.main();
                input_word = this.scanner.nextLine().toUpperCase();
            }
            if (input_word.equals("T")){
                Date b = new Date(System.currentTimeMillis());
                long time_spent = b.getTime() - this.start_time.getTime() - time_stored;
                System.out.println("You already spent " + time_spent/1000 + " seconds.");
            }
            control.setEX(input_word);
            if(input_word.equals("P")){
//                System.out.println("P------");
                control.pause();
                Date b = new Date(System.currentTimeMillis());
                middle_time = b.getTime();
            }else if(input_word.equals("R")){
//                System.out.println("R-------------");
                control.play();
                Date c = new Date(System.currentTimeMillis());
                time_stored += c.getTime() - middle_time;
            }else if (input_word.equals("EX")) {
//                System.out.println("ex--------------");
                computerMove(allWords);
                control.setSta(this.gameStats);
                control.exit();
            }

            //step 4. If it's valid, update the player's word list and score (stored in boggleStats)
            if (allWords.containsKey(input_word) && !this.gameStats.getPlayerWords().contains(input_word)) {
                this.gameStats.addWord(input_word, BoggleStats.Player.Human);
            }
            //step 5. Repeat step 1 - 4
            //step 6. End when the player hits return (with no word choice).
            if (input_word.equals("")) {
                return;
            }
        }
    }


    /*
     * Gets words from the computer.  The computer should find words that are
     * both valid and not in the player's word list.  For each word that the computer
     * finds, update the computer's word list and increment the
     * computer's score (stored in boggleStats).
     *
     * @param allWords A mutable list of all legal words that can be found, given the boggleGrid grid letters
     */
    private void computerMove(Map<String, ArrayList<Position>> all_words) {
        for (String word : all_words.keySet())
            if (!this.gameStats.getPlayerWords().contains(word)) {
                this.gameStats.addWord(word, BoggleStats.Player.Computer);
            }
    }

    //highscore pull
    public void getScores() throws IOException, BrailleLetterException {
        this.highScores.scoreSetup();
        this.highScores.scoreComparer(gameStats, user);
        this.highScores.scoreExplanation();
        this.highScores.scoreSaving();
    }
    public BoggleStats getGameStats(){
        return this.gameStats;
    }
}
