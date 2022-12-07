package State;

import boggle.BoggleStats;

public class Pause implements TransState{
    @Override
    public void pause(Controller c, String s) {
        System.out.println("It is already paused dude, we cannot stop the time.");
        //do nothing
    }

    @Override
    public void play(Controller c, String s ) {
        c.setPlayTransState(s);
        System.out.println("The game is now resumed. Tiktok tiktok.");
        //resume the timer
    }

    @Override
    public void exit(BoggleStats sta, int ty, String s) {
        String ex = s.toUpperCase();
        if (ex.equals("EX")) {
            Exit e = new Exit();
            e.exit(sta,ty,s);
        }
        //end the game and save the state
    }
}
