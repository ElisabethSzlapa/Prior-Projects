package State;

import boggle.BoggleStats;

public interface TransState {
    void pause(Controller c, String s);
    void play(Controller c, String s);
    void exit(BoggleStats sta, int ty, String s);
}
