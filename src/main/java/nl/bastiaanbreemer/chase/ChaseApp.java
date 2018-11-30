package nl.bastiaanbreemer.chase;

import greenfoot.Greenfoot;
import greenfoot.export.GreenfootScenarioMain;
import nl.bastiaanbreemer.chase.utils.worlds.BaseWorld;
import nl.bastiaanbreemer.chase.worlds.ChaseWorld;

public class ChaseApp extends GreenfootScenarioMain {
    public final static int LIVES_MAX = 4;
    public static ChaseApp application = new ChaseApp();
    public BaseWorld world;
    public int score;
    public int lives = LIVES_MAX;
    public State state = State.INITIALIZING;

    public static void main(String[] args) {
        GreenfootScenarioMain.main(args);
    }

    public void gameOver() {
        lives = LIVES_MAX;
//        new LoadingWorld();
        new ChaseWorld();
    }

    public void setWorld(BaseWorld world) {
        this.world = world;
        Greenfoot.setWorld(world);
    }

    public void setState(State state) {
        this.state = state;
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