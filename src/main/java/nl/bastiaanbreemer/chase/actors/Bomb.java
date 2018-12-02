package nl.bastiaanbreemer.chase.actors;

import greenfoot.GreenfootSound;
import nl.bastiaanbreemer.chase.ChaseApp;
import nl.bastiaanbreemer.chase.utils.animations.AnimatedMover;
import nl.bastiaanbreemer.chase.utils.animations.Animation;
import nl.bastiaanbreemer.chase.utils.tiles.ChaseTile;

import java.util.ArrayList;

public class Bomb extends AnimatedMover {

    public final static int TIMEOUT = 8;
    private final static String ANIMATION_PATH = "items/%NAME%%FRAME%.png";
    private static ArrayList<Bomb> bombs = new ArrayList<>();
    public final int radius = 125;
    private final float gravity = 5.0f;
    private final float acc = 0.1f;
    private final float drag = 0.5f;
    public int time = 0;
    private int update = 0;

    public Bomb(double velocityX, double velocityY) {
        super(ANIMATION_PATH);
        this.velocityX = velocityX;
        this.velocityY = velocityY;

        addAnimation("bomb", 2, 50);

        setAnimation("bomb");
        bombs.add(this);
    }

    public static ArrayList<Bomb> getBombs() {
        return bombs;
    }

    private void explode() {
        // TODO: add search radius and decrease health of Chaser inside
        GreenfootSound boom = new GreenfootSound("sounds/small-explosion-" + ((Math.random() <= 0.5) ? 1 : 2) + ".wav");
        boom.setVolume(10);
        boom.play();

        for (ChaseTile tile : getObjectsInRange(this.radius, ChaseTile.class)) {
            if (tile.isSolid)
                ChaseApp.application.world.getTileEngine().removeTile(tile);
        }

        for (Chaser chaser : getObjectsInRange(this.radius, Chaser.class)) {
            chaser.reset();
        }

        bombs.remove(this);
        getWorld().removeObject(this);
    }

    @Override
    public void act() {
        super.act();
        setRotation(getRotation() + (int) velocityX);
        int height = getHeight() / 2;
        int width = getWidth() / 2;

        boolean top = isTileSolidAtOffset(0, -height + 7);
        boolean bottom = isTileSolidAtOffset(0, height - 7);
        if ((top && velocityY < 0)) {
            velocityY *= -1;
        } else if ((bottom && velocityY > 0)) {
            velocityY *= -0.9; // make it bounce less when going up so it stops bouncing gradually
        }
        boolean left = isTileSolidAtOffset(-width + 7, 0);
        boolean right = isTileSolidAtOffset(width - 7, 0);
        if ((left && velocityX < 0) || (right && velocityX > 0)) {
            velocityX *= -1;
        }

        if (velocityX > 1)
            velocityX -= drag;
        else if (velocityX < -1)
            velocityX += drag;
        velocityY += acc;
        if (velocityY > gravity) {
            velocityY = gravity;
        }
        applyVelocity();
        update++;
        if (update >= 120) {
            update = 0;
            time++;
            Animation animation = getAnimation("bomb");
            animation.setSpeed(animation.getSpeed() - 1);
            if (time >= TIMEOUT) {
                this.explode();
            }
        }
    }
}
