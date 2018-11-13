package nl.bastiaanbreemer.chase.actors;

import greenfoot.Actor;
import greenfoot.Greenfoot;
import nl.bastiaanbreemer.chase.utils.AnimatedMover;

public class Chaser extends AnimatedMover {

    private final static String ANIMATION_PATH = "players/p1_%NAME%%FRAME%.png";

    private final double gravity;
    private final double acc;
    private final double drag;

    private boolean isCrouching = false;
    private boolean isSprinting = false;
    private int direction = 0;

    public Chaser() {
        super(ANIMATION_PATH);
        gravity = 5.0;
        acc = 0.2;
        drag = 0.8;

        // Set largest image to be the first
        setImage("players/p1_walk01.png");

        // Adding all animations
        addAnimation("duck");
        addAnimation("front");
        addAnimation("hurt");
        addAnimation("jump");
        addAnimation("stand");
        addAnimation("walk", 11, 10);

        setAnimation("stand");
    }

    public void handleAnimations() {
        if (isCrouching) {
            setAnimation("duck");
        } else if (velocityY > 0 + acc * 3) {
            setAnimation("hurt");
        } else if (velocityY < 0 - acc) {
            setAnimation("jump");
        } else {
            if (velocityX >= 4 || velocityX <= -4) {
                setAnimation("walk").setSpeed(4);
            } else if (velocityX >= 2 || velocityX <= -2) {
                setAnimation("walk").setSpeed(5);
            } else {
                setAnimation("stand");
            }
        }
    }

    public void handleInput() {

        if (Greenfoot.isKeyDown("1")) {
            setAnimation("duck");
        }
        if (Greenfoot.isKeyDown("2")) {
            setAnimation("hurt");
        }
        if (Greenfoot.isKeyDown("3")) {
            setAnimation("jump");
        }
        if (Greenfoot.isKeyDown("4")) {
            setAnimation("stand");
        }
        if (Greenfoot.isKeyDown("5")) {
            setAnimation("walk");
        }


        if (Greenfoot.isKeyDown("w") || Greenfoot.isKeyDown("space")) {
            velocityY = -5;
        }

        if (Greenfoot.isKeyDown("a")) {
            direction = 1;
            velocityX = isSprinting ? -4 : -2;
        } else if (Greenfoot.isKeyDown("d")) {
            direction = 0;
            velocityX = isSprinting ? 4 : 2;
        }

        if (Greenfoot.isKeyDown("control")) {
            isCrouching = true;
            isSprinting = false;
        } else if (Greenfoot.isKeyDown("shift")) {
            isCrouching = false;
            isSprinting = true;
        } else {
            isCrouching = false;
            isSprinting = false;
        }
    }

    @Override
    public void act() {
        handleInput();
        handleAnimations();
        if (direction == 0)
            setMirrorHorizontally(true);
        else if (direction == 1)
            setMirrorHorizontally(false);
        super.act();
        velocityX *= drag;
        velocityY += acc;
        if (velocityY > gravity) {
            velocityY = gravity;
        }
        applyVelocity();


        for (Actor enemy : getIntersectingObjects(Enemy.class)) {
            if (enemy != null) {
                getWorld().removeObject(this);
                break;
            }
        }
        getImage().mirrorHorizontally();
    }

    public int getWidth() {
        return getImage().getWidth();
    }

    public int getHeight() {
        return getImage().getHeight();
    }

}
