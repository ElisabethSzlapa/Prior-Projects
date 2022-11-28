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
    }
    public void scoreInterface() {
        System.out.println("To see point scores, press 1. To see ratio scores, press 2.");
        System.out.println("To see personal bests, press 3. And to have the boards explained again, hit 0.");
        String choice = scanner.nextLine();
        if(choice == "1"){
        }
    }
}
