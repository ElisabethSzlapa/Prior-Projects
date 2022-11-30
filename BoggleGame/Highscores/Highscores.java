package Highscores;
import boggle.BoggleGame;
import boggle.BoggleStats;

import java.util.*;
public class Highscores {

    /**
     * scanner used to interact with the user via console
     */
    public Scanner scanner;

    /**scoreboards
     *
     */
    private Map<String, Integer> pointHighscores = new HashMap<>(10);
    private Map<String, Integer> ratioHighscores = new HashMap<>(10);
    private Map<String, Integer> allUsersHighscores = new HashMap<>();
    public void scoreComparer(BoggleStats game) {
        int currScore = game.getScore();
        String playerName = ""; //relies on the as of yet not implemented user class!
        overallScore(currScore, playerName);
        for (String player : pointHighscores.keySet()){
            if(player == null){
                pointHighscores.put(playerName, currScore);
            }
            else if (pointHighscores.get(player) < currScore) { //no equals case; scoreboard placement is first come first serve
                int oldScore = pointHighscores.get(player);
                String oldPlayer = player;
                pointHighscores.put(playerName, currScore);
                currScore = oldScore;
                playerName = oldPlayer;
            }
        }

    }
    public void overallScore(int currScore, String playerName) {
        if (allUsersHighscores.containsKey(playerName)){
            if(currScore > allUsersHighscores.get(playerName)){
                allUsersHighscores.put(playerName, currScore);
            }
        }
        else{
            allUsersHighscores.put(playerName, currScore);
        }

    }
    public void scoreExplanation(){
        System.out.println("There are three types of scoreboards to see;");
        System.out.println("Point based, with the 10 highest scores for points won,");
        System.out.println("Ratio based, with the 10 highest scores for the highest ratio of words found to words possible on the board,");
        System.out.println("and Personal bests, the best score that every user who's played has received.");
        System.out.println("0111001110101100111010111010000110100001111010100100011100101010011010100010010100\n" +
                "1010000101000100101101100111001111010010000111011000100100101100101010010011101100\n" +
                "0000001110110000101000000010001000000010001010001000001000001000001010000000001000");
    }
    public void scoreInterface() {
        System.out.println("To see point scores, press 1. To see ratio scores, press 2.");
        System.out.println("To see personal bests, press 3. And to have the boards explained again, hit 0.");
        System.out.println("Want to exit the scoreboard system? Hit 4.");
        String choice = scanner.nextLine();
        while(true) {
            if (choice == "1") {
                formatted(pointHighscores);
                break;
            }

            if (choice == "2") {
                formatted(ratioHighscores);
                break;
            }
            if (choice == "3") {
                formatted(allUsersHighscores);
                break;
            }
            if (choice == "0") {
                scoreInterface();
                break;
            }
            if (choice == "6"){
            }
            if (choice == "4") {
                break;
            }
            while(choice != "1" || choice != "2" || choice != "3" || choice != "4" || choice != "0"){
                System.out.println("That's not one of the numbers! Press 0 to have the options explained again. Changed your mind about seeing the scores? Hit 4 to exit.");
                if (choice == "4"){

                }
            }
        }
    }
    public void formatted(Map<String, Integer> scoreboard){
        int i = 1;
        for (String key : scoreboard.keySet()){
            System.out.println(i+". " + key+", "+scoreboard.get(key));
            i++;
        }
    }
    public void brailleMode(){

    }

    public void formattedBraille(Map<String, Integer> scoreboard){
        int i = 1;
        for (String key : scoreboard.keySet()){
            System.out.println(i+". " + key+", "+scoreboard.get(key));
            i++;
        }
    }
}
