package User;

import boggle.BoggleGame;
import boggle.BoggleStats;

import java.lang.management.ThreadInfo;
import java.util.*;

public class User {
    private String username;
    private HashMap<String, BoggleStats> userBase = new HashMap<>();
    public void setUser(String username){
        this.username = username;
    }
    //saving the new states to the dictionary
    public void addUser() {
        BoggleGame bg = new BoggleGame();
        if (!this.userBase.containsKey(this.username)) {
            this.userBase.put(this.username, bg.getGameStats());
        } else {
            this.userBase.replace(this.username, bg.getGameStats());
        }
    }
    public Boolean check_registered(){
        return this.userBase.containsKey(this.username);
    }
    public BoggleStats get_value(){
        return this.userBase.get(this.username);
    }
    public String getUsername(){
        return this.username;
    }
}
