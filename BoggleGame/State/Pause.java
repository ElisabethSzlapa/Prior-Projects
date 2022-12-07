package State;

public class Pause implements TransState{
    @Override
    public void pause() {
        System.out.println("It is already paused dude, we cannot stop the time.");
        //do nothing
    }

    @Override
    public void play() {
        System.out.println("The game is now resumed. Tiktok tiktok.");
        //resume the timer
    }

    @Override
    public void exit() {
        System.out.println("Thank you for playing.");
        //end the game and save the state
    }
}
