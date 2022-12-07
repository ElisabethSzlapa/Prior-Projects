package boggle;

import java.util.HashSet;
import java.util.Set;

import ScoreCalculator2.ScoreCalculatorFactory;

/**
 * The BoggleStats class for the first Assignment in CSC207, Fall 2022
 * The BoggleStats will contain statsitics related to game play Boggle
 */
public class BoggleStats {

    /**
     * set of words the player finds in a given round
     */
    private Set<String> playerWords = new HashSet<String>();
    /**
     * set of words the computer finds in a given round
     */
    private Set<String> computerWords = new HashSet<String>();
    /**
     * the player's score for the current round
     */
    private int pScore;
    /**
     * the computer's score for the current round
     */
    private int cScore;
    /**
     * the player's total score across every round
     */
    private int pScoreTotal;
    /**
     * the computer's total score across every round
     */
    private int cScoreTotal;
    /**
     * the average number of words, per round, found by the player
     */
    private double pAverageWords;
    /**
     * the average number of words, per round, found by the computer
     */
    private double cAverageWords;
    /**
     * the current round being played
     */
    private int round;


    /**
     * enumarable types of players (human or computer)
     */
    public enum Player {
        Human("Human"),
        Computer("Computer");
        private final String player;

        Player(final String player) {

            this.player = player;
        }
    }

    /* BoggleStats constructor
     * ----------------------
     * Sets round, totals and averages to 0.
     * Initializes word lists (which are sets) for computer and human players.
     */
    public BoggleStats() {
        this.pScore = 0;
        this.cScore = 0;
        this.pScoreTotal = 0;
        this.cScoreTotal = 0;
        this.pAverageWords = 0.0;
        this.cAverageWords = 0.0;
        this.round = 0;
        this.playerWords = new HashSet<String>();
        this.computerWords = new HashSet<String>();
    }

    /*
     * Add a word to a given player's word list for the current round.
     * You will also want to increment the player's score, as words are added.
     *
     * @param word     The word to be added to the list
     * @param player  The player to whom the word was awarded
     */
    public void addWord(String word, Player player) {
        if (player == Player.Human) {
            this.playerWords.add(word);
            if (word.length() <= 4) {
                // if the word length smaller or equal to 4, add 1 point,
                // 1 additional point for every letter over 4.
                this.pScore += 1;
            } else {
                this.pScore += 1;//add 1 point for word itself
                // add 1 point for every letter over than 4.
                this.pScore += word.length() - 4;// the total length minus 4, the result is the additional points.
            }

        } else {
            if (word.length() <= 4) {
                // if the word length smaller or equal to 4, add 1 point,
                // 1 additional point for every letter over 4.
                this.cScore += 1;
            } else {
                this.cScore += 1;//add 1 point for word itself
                // add 1 point for every letter over than 4.
                this.cScore += word.length() - 4;// the total length minus 4, the result is the additional points.
            }
            this.computerWords.add(word);
        }
    }

    /*
     * End a given round.
     * This will clear out the human and computer word lists, so we can begin again.
     * The function will also update each player's total scores, average scores, and
     * reset the current scores for each player to zero.
     * Finally, increment the current round number by 1.
     */
    public void endRound(int type) {
        double sc = 0;
        if (type == 1) {
            ScoreCalculatorFactory f = new ScoreCalculatorFactory();
            sc = f.calculateScore("Hard", this.pScore);
        }else if (type == 2) {
            ScoreCalculatorFactory f = new ScoreCalculatorFactory();
            sc = f.calculateScore("Normal", this.pScore);
        }else {
            ScoreCalculatorFactory f = new ScoreCalculatorFactory();
            sc = f.calculateScore("Easy", this.pScore);
        }

        // get the player's last total number word
        double last_total_number_word_player = this.pAverageWords * (this.round - 1);
        // last total add new number of word, then divide by the round to get new average
        this.pAverageWords = (last_total_number_word_player + this.playerWords.size()) / this.round;

        // same as player average words
        double last_total_number_word_computer = this.cAverageWords * (this.round - 1);
        this.cAverageWords = (last_total_number_word_computer + this.computerWords.size()) / this.round;

        //reset the lists
        this.playerWords.clear();
        this.computerWords.clear();

        this.pScoreTotal += sc;//update the total scores
        this.pScore = 0;//reset
        this.cScoreTotal += this.cScore;//update the total scores
        this.cScore = 0;//reset

        this.round += 1;

    }

    /*
     * Summarize one round of boggle.  Print out:
     * The words each player found this round.
     * Each number of words each player found this round.
     * Each player's score this round.
     */
    public void summarizeRound(int type) {
        double sc = 0;
        if (type == 1) {
            ScoreCalculatorFactory f = new ScoreCalculatorFactory();
            sc = f.calculateScore("Hard", this.pScore);
        }else if (type == 2) {
            ScoreCalculatorFactory f = new ScoreCalculatorFactory();
            sc = f.calculateScore("Normal", this.pScore);
        }else {
            ScoreCalculatorFactory f = new ScoreCalculatorFactory();
            sc = f.calculateScore("Easy", this.pScore);
        }
        System.out.println(Player.Human + ":");
        System.out.printf("You score are %f\n", sc);
        System.out.printf("You found %d words\n", this.playerWords.size());
        for (String word : this.playerWords) {
            System.out.print(word);
            System.out.print(", ");
        }
        System.out.println();

        System.out.println("----------------");

        System.out.println(Player.Computer + ":");
        System.out.printf("Computer score are %d\n", this.cScore);
        System.out.printf("computer found %d words\n", this.computerWords.size());
        for (String word : this.computerWords) {
            System.out.print(word);
            System.out.print(", ");
        }
        System.out.println();

    }

    /*
     * Summarize the entire boggle game.  Print out:
     * The total number of rounds played.
     * The total score for either player.
     * The average number of words found by each player per round.
     */
    public void summarizeGame() {

        System.out.printf("Total Round: %d\n", this.round);
        System.out.println(Player.Human + ":");
        System.out.printf("You total score are %d\n", this.pScoreTotal);
        System.out.printf("The average number of words you found are %f words\n", this.pAverageWords);

        System.out.println("----------------");

        System.out.println(Player.Computer + ":");
        System.out.printf("Computer total score are %d\n", this.cScoreTotal);
        System.out.printf("The average number of words computer found are %f words\n", this.cAverageWords);


    }

    /*
     * @return Set<String> The player's word list
     */
    public Set<String> getPlayerWords() {
        return this.playerWords;
    }


    /*
     * @return int The number of rounds played
     */
    public int getRound() {
        return this.round;
    }

    /*
     * @return int The current player score
     */
    public int getScore() {
        return this.pScore;
    }

    public Set<String> getComputerWords() {
        return this.computerWords;
    }

    public int getcScore() {
        return this.cScore;
    }

    public int getcScoreTotal() {
        return this.cScoreTotal;
    }

    public double getpAverageWords() {
        return this.pAverageWords;
    }

    public double getcAverageWords() {
        return this.cAverageWords;
    }


}
