package Highscores;
import Highscores.braille.BrailleLetterException;
import Highscores.braille.BrailleTranslator;
import User.User;
import boggle.BoggleStats;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
public class Highscores {

    /**
     * scanner used to interact with the user via console
     */
    public Scanner scanner;

    /**scoreboards
     *
     */
    private Map<String, Double> pointHighscores = new HashMap<>(10);
    private Map<String, Double> ratioHighscores = new HashMap<>(10);
    private Map<String, Double> allUsersHighscores = new HashMap<>();

    public Highscores(){
        this.scanner = new Scanner(System.in);
    }

    public void scoreSetup() throws FileNotFoundException {
        File pointHigh = new File("BoggleGame/Highscores/pointscores.txt");
        File ratioHigh = new File("BoggleGame/Highscores/ratioscores.txt");
        File overallHigh = new File("BoggleGame/Highscores/overallscores.txt");

        Scanner pointReader = new Scanner(pointHigh);
        while(pointReader.hasNextLine()){
            String[] data = pointReader.nextLine().split(",");
            pointHighscores.put(data[0], Double.valueOf(data[1]));
        }
        pointReader.close();

        Scanner overallReader = new Scanner(overallHigh);
        while(overallReader.hasNextLine()){
            String[] data = overallReader.nextLine().split(",");
            allUsersHighscores.put(data[0], Double.valueOf(data[1]));
        }
        overallReader.close();

        Scanner ratioReader = new Scanner(ratioHigh);
        while(ratioReader.hasNextLine()){
            String[] data = ratioReader.nextLine().split(",");
            ratioHighscores.put(data[0], Double.valueOf(data[1]));
        }
        ratioReader.close();

    }
    public void scoreSaving() throws IOException {
        FileWriter pointWriter = new FileWriter("BoggleGame/Highscores/pointscores.txt");
        FileWriter ratioWriter = new FileWriter("BoggleGame/Highscores/ratioscores.txt");
        FileWriter overallWriter = new FileWriter("BoggleGame/Highscores/overallscores.txt");

        for (String player : allUsersHighscores.keySet()) {
            overallWriter.write(player + "," + String.valueOf(allUsersHighscores.get(player)));
        }
        overallWriter.close();

        for (String player : pointHighscores.keySet()) {
            pointWriter.write(player + "," + String.valueOf(pointHighscores.get(player)));
        }
        pointWriter.close();

        for (String player : ratioHighscores.keySet()) {
            ratioWriter.write(player + "," + String.valueOf(ratioHighscores.get(player)));
        }
        ratioWriter.close();
    }

    public void scoreComparer(BoggleStats game, User u) {
        double currScore = game.getScore();
        String playerName = u.toString();
        overallScore(currScore, playerName);
        for (String player : pointHighscores.keySet()){
            if(player == null){
                pointHighscores.put(playerName, currScore);
            }
            else if (pointHighscores.get(player) < currScore) { //no equals case; scoreboard placement is first come first serve
                double oldScore = pointHighscores.get(player);
                String oldPlayer = player;
                pointHighscores.put(playerName, currScore);
                currScore = oldScore;
                playerName = oldPlayer;
            }
        }
        double ratioScore = game.getPlayerWords().size()/game.getComputerWords().size();
        for (String player : ratioHighscores.keySet()) {
            if (player == null) {

                ratioHighscores.put(playerName, ratioScore);
            } else if (ratioHighscores.get(player) < ratioScore) { //no equals case; scoreboard placement is first come first serve
                double oldScore = ratioHighscores.get(player);
                String oldPlayer = player;
                ratioHighscores.put(playerName, ratioScore);
                ratioScore = oldScore;
                playerName = oldPlayer;
            }
        }

    }
    public void overallScore(double currScore, String playerName) {
        if (allUsersHighscores.containsKey(playerName)){
            if(currScore > allUsersHighscores.get(playerName)){
                allUsersHighscores.put(playerName, currScore);
            }
        }
        else{
            allUsersHighscores.put(playerName, currScore);
        }

    }


    public void scoreExplanation() throws IOException, BrailleLetterException {
        System.out.println("There are three types of scoreboards to see;");
        System.out.println("Point based, with the 10 highest scores for points won,");
        System.out.println("Ratio based, with the 10 highest scores for the highest ratio of words found to words possible on the board,");
        System.out.println("and Personal bests, the best score that every user who's played has received.");
        scoreInterface();
    }
    public void scoreInterface() throws IOException, BrailleLetterException {
        System.out.println("To see point scores, press 1. To see ratio scores, press 2.");
        System.out.println("To see personal bests, press 3.");
        System.out.println("Want to exit the scoreboard system? Hit 4.");
        System.out.println("0111001110101100111010111010000110100001111010100100011100101010011010100010010100" +
                        "1010000101000100101101100111001111010010000111011000100100101100101010010011101100" +
                "0000001110110000101000000010001000000010001010001000001000001000001010000000001000");
        String choice = scanner.nextLine();
        while(true) {
            if (Objects.equals(choice, "1")) {
                formatted(pointHighscores);
                break;
            }

            if (Objects.equals(choice, "2")) {
                formatted(ratioHighscores);
                break;
            }
            if (Objects.equals(choice, "3")) {
                formatted(allUsersHighscores);
                break;
            }
            if (Objects.equals(choice, "6")){
                brailleMode();
                break;
            }
            if (Objects.equals(choice, "4")) {
                break;
            }
            else {
                System.out.println("That's not one of the numbers! Changed your mind about seeing the scores? Hit 4 to exit.");
            }


        }
    }
    public void formatted(Map<String, Double> scoreboard) throws IOException, BrailleLetterException {
        int i = 1;
        if(scoreboard.keySet().isEmpty()){
            System.out.println("Scoreboard empty!");
            scoreExplanation();
        }
        for (String key : scoreboard.keySet()){
            System.out.println(i+". " + key+", "+scoreboard.get(key));
            i++;
        }
    }
    public void brailleMode() throws IOException, BrailleLetterException {
        BrailleTranslator translator = new BrailleTranslator();
        System.out.println(translator.translateLine("To see point scores, press 1. To see ratio scores, press 2."));
        System.out.println(translator.translateLine("To see personal bests, press 3."));
        System.out.println(translator.translateLine("Want to exit the scoreboard system? Hit 4."));
        String choice = scanner.nextLine();
        while(true) {
            if (Objects.equals(choice, "1")) {
                formattedBraille(pointHighscores);
                break;
            }

            if (Objects.equals(choice, "2")) {
                formattedBraille(ratioHighscores);
                break;
            }
            if (Objects.equals(choice, "3")) {
                formattedBraille(allUsersHighscores);
                break;
            }

            if (Objects.equals(choice, "4")) {
                break;
            }
            else {
                System.out.println(translator.translateLine("Changed your mind about seeing the scores? Hit 4 to exit."));

            }

        }
    }

    public void formattedBraille(Map<String, Double> scoreboard) throws IOException, BrailleLetterException {

        BrailleTranslator translator = new BrailleTranslator();
        int i = 1;
        for (String key : scoreboard.keySet()){
            System.out.println(i+". " + translator.translateLine(key)+", "+scoreboard.get(key));
            i++;
        }
    }
}
