package State;

import boggle.BoggleStats;

public class Exit implements TransState {
    @Override
    public void pause(Controller c, String s) {
        return;
    }

    @Override
    public void play(Controller c, String s) {
        return;
        //start a new game
    }

    @Override
    public void exit(BoggleStats sta, int ty, String s) {
        sta.endRound(ty);
        sta.summarizeGame();
        System.exit(0);
    }
}
