package nl.bastiaanbreemer.chase.actors;

import nl.bastiaanbreemer.chase.utils.animations.AnimatedMover;

/**
 * @author R. Springer
 */
public class Fly extends AnimatedMover {

    private final static String ANIMATION_PATH = "enemy/%NAME%%FRAME%.png";

    private Chaser chaser;
    private int direction = 0;

    public Fly(Chaser chaser) {
        super(ANIMATION_PATH);
        addAnimation("fly", 2, 20);
        setAnimation("fly");
        velocityX = 1;
        this.chaser = chaser;
    }

    @Override
    public void act() {
        if (getX() <= 0) {
        if (velocityX > 0) {
            setMirrorHorizontally(true);
        } else {
            setMirrorHorizontally(false);
        }
        super.act();

//        float distance = (float) Math.hypot(getX() - chaser.getX(), getY() - chaser.getY()) / TileEngine.TILE_WIDTH;

        velocityY = (int) (Math.sin(Math.toRadians(getX() * 2)) * 2);

        applyVelocity();
    }
}
