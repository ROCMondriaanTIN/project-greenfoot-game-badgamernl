package nl.bastiaanbreemer.chase.worlds;

import nl.bastiaanbreemer.chase.ChaseApp;
import nl.bastiaanbreemer.chase.utils.cameras.Overlay;
import nl.bastiaanbreemer.chase.utils.worlds.BaseWorld;

public class LoadingWorld extends BaseWorld {

    private final Overlay overlay;

    public LoadingWorld() {
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(72 * 14, 72 * 12, 1, false);
        this.setBackground("bg.png");

        this.overlay = new Overlay(this, null);

        ChaseApp.application.state = ChaseApp.State.LOADING;
    }


    @Override
    public void act() {
    }
}
