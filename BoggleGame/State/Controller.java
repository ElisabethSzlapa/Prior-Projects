package State;

public class Controller {
    public static Play play;
    public static Pause pause;
    public static Exit exit;

    private static TransState tra;

    Controller(){
        play = new Play();
        pause = new Pause();
        exit = new Exit();
    }
    public void setPlayTransState(){
        tra = play;
    }
    public void setPauseTransState(){
        tra = pause;
    }
    public void setExitTransState(){
        tra = exit;
    }
    public void play(){
        tra.play();
    }
    public void pause(){
        tra.pause();
    }
    public void exit(){
        tra.exit();
    }
}
