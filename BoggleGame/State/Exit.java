package State;

public class Exit implements TransState{
    @Override
    public void pause() {
        System.out.println("Play is your only option now.");
        //do nothing
    }

    @Override
    public void play() {
        //start a new game
    }

    @Override
    public void exit() {
        System.out.println("Play is your only option now.");
        //do nothing
    }
}
