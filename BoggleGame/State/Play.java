package State;

public class Play implements TransState{
    @Override
    public void exit() {
        System.out.println("Thank you for playing.");
        //end the game and save the state
    }
    @Override
    public void pause() {
        System.out.println("The game is now paused, timer will not be ticking.");
        //pause the timer
    }

    @Override
    public void play() {
        System.out.println("You are already playing my friend.");
        //do nothing
    }


}
