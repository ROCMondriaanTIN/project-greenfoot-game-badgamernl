package nl.bastiaanbreemer.chase;

import greenfoot.Greenfoot;
import greenfoot.export.GreenfootScenarioMain;
import nl.bastiaanbreemer.chase.utils.worlds.BaseWorld;
import nl.bastiaanbreemer.chase.worlds.GameWon;
import nl.bastiaanbreemer.chase.worlds.Level1;
import nl.bastiaanbreemer.chase.worlds.Level2;

public class ChaseApp extends GreenfootScenarioMain {
    public final static int LIVES_MAX = 4;
    public final static int LEVELS = 3;
    public static ChaseApp application = new ChaseApp();
    public BaseWorld world;
    public int score;
    public int lives = LIVES_MAX;
    public State state = State.INITIALIZING;

    public static void main(String[] args) {
        GreenfootScenarioMain.main(args);
    }

    public void reset() {
        lives = LIVES_MAX;
        setWorld(new Level1());
    }

    public void gameOver() {
        reset();
    }

    public void gameWon() {
        setWorld(new GameWon());
    }

    public void gameNext() {
        int next = this.world.getId() + 1;
        if (next > LEVELS) {
            gameWon();
            return;
        }
        switch (next) {
            case 1:
                setWorld(new Level1());
                System.out.println("Loading level 1");
                break;
            case 2:
                setWorld(new Level2());
                System.out.println("Loading level 2");
                break;
            case 3:
                setWorld(new Level1());
                System.out.println("Loading level 3");
                break;
            default:
                setWorld(new Level1());
                System.out.println("Loading level 1");
                break;
        }
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
        INITIALIZING, PLAYING, READY, WON
    }
}