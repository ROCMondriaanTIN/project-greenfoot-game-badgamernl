package nl.bastiaanbreemer.chase;

import greenfoot.Greenfoot;
import greenfoot.World;
import greenfoot.export.GreenfootScenarioMain;
import nl.bastiaanbreemer.chase.worlds.LoadingWorld;

public class ChaseApp extends GreenfootScenarioMain {
    public final static int LIVES_MAX = 4;
    public static ChaseApp application = new ChaseApp();
    public World world;
    public int score;
    public int lives = LIVES_MAX;
    public State state = State.INITIALIZING;

    public static void main(String[] args) {
        GreenfootScenarioMain.main(args);
    }

    public void gameOver() {
        lives = LIVES_MAX;
        Greenfoot.setWorld(new LoadingWorld());
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public static enum State {
        INITIALIZING, PLAYING, LOADING, READY
    }
}