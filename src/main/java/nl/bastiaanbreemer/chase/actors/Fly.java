package nl.bastiaanbreemer.chase.actors;

import nl.bastiaanbreemer.chase.utils.animations.AnimatedMover;
import nl.bastiaanbreemer.chase.utils.engine.TileEngine;

/**
 * @author R. Springer
 */
public class Fly extends AnimatedMover {

    private final static String ANIMATION_PATH = "enemy/%NAME%%FRAME%.png";

    private Chaser chaser;

    public Fly(Chaser chaser) {
        super(ANIMATION_PATH);
        addAnimation("fly", 2, 10);
        setAnimation("fly");
        velocityX = -1;
        this.chaser = chaser;
    }

    @Override
    public void act() {
        if (getX() <= 0) {
            velocityX = 1;
        } else if (getX() > TileEngine.MAP_WIDTH * TileEngine.TILE_WIDTH) {
            velocityX = -1;
        }
        if (velocityX > 0) {
            setMirrorHorizontally(true);
        } else {
            setMirrorHorizontally(false);
        }
        super.act();

//        float distance = (float) Math.hypot(getX() - chaser.getX(), getY() - chaser.getY()) / TileEngine.TILE_WIDTH;

        velocityY = (int) (Math.sin(Math.toRadians(getX() * 2)) * 4);

        applyVelocity();
    }
}
