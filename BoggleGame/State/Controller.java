package State;

import boggle.BoggleStats;

public class Controller {
    public static Play play = new Play();
    public static Pause pause = new Pause();
    public static Exit exit = new Exit();

    private BoggleStats sta;
    private String ex;
    private int ty;
    private TransState tra = new Play();

    /*
     public Controller(){
        play = new Play();
        pause = new Pause();
        exit = new Exit();
    }
     */
    public void setPlayTransState(String s) {
        this.ex = s;
        tra = play;
    }

    public void setPauseTransState(String s) {
        this.ex = s;
        tra = pause;
    }

    public void setExitTransState(BoggleStats s) {
        this.sta = s;
        tra = exit;
    }

    public void setTy(int ty){
        this.ty = ty;
    }

    public void setEX(String s){
        this.ex = s.toUpperCase();
    }
    public void setSta(BoggleStats sta){
        this.sta = sta;
    }

    public void play() {
        tra.play(this, this.ex);
    }

    public void pause() {
        tra.pause(this, this.ex);
    }

    public void exit() {
        tra.exit(this.sta, this.ty, this.ex);
    }

}
