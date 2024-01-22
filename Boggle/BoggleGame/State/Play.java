package State;

import boggle.BoggleStats;

public class Play implements TransState{
    @Override
    public void exit(BoggleStats sta, int ty,  String s) {
        String ex = s.toUpperCase();
        if(ex.equals("EX")){
            Exit e = new Exit();
            e.exit(sta,ty,s);
        }
        //end the game and save the state
    }
    @Override
    public void pause(Controller c, String s) {
        c.setPauseTransState(s);
        System.out.println("The game is now paused, timer will not be ticking.");
        //pause the timer
    }

    @Override
    public void play(Controller c, String s) {
        System.out.println("You are already playing my friend.");
        //do nothing
    }


}
