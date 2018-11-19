package nl.bastiaanbreemer.chase.actors;

import greenfoot.Actor;
import greenfoot.Greenfoot;
import nl.bastiaanbreemer.chase.utils.AnimatedMover;

public class Chaser extends AnimatedMover {

    public final static int HEALTH_MAX = 6;
    private final static String ANIMATION_PATH = "players/p1_%NAME%%FRAME%.png";

    private final double gravity;
    private final double acc;
    private final double drag;
    private float health = HEALTH_MAX;
    private boolean isCrouching = false;
    private boolean isSprinting = false;
    private int direction = 0;

    public Chaser() {
        super(ANIMATION_PATH);
        gravity = 5.0;
        acc = 0.2;
        drag = 0.8;

        // Adding all animations
        addAnimation("duck");
        addAnimation("front");
        addAnimation("hurt");
        addAnimation("jump");
        addAnimation("stand");
        addAnimation("walk", 11, 10);

        setAnimation("stand");
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void increaseHealth() {
        if (this.health <= HEALTH_MAX - 0.05)
            this.health += 0.05;
    }

    public void decreaseHealth() {
        if (this.health >= 0.05)
            this.health -= 0.05;
    }


    public void increaseHealth(float amount) {
        if (this.health <= HEALTH_MAX - amount)
            this.health += amount;
    }

    public void decreaseHealth(float amount) {
        if (this.health >= amount)
            this.health -= amount;
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
            int pixelYOffset = (getImage().getHeight() / 2);

            boolean bottomLeft = isTileSolidAtOffset(-(getWidth() / 2) + 1, pixelYOffset);
            boolean bottomMiddle = isTileSolidAtOffset(0, pixelYOffset);
            boolean bottomRight = isTileSolidAtOffset((getWidth() / 2) - 1, pixelYOffset);
            if ((bottomLeft || bottomMiddle || bottomRight))
                velocityY = -10;
        }

        if (Greenfoot.isKeyDown("a")) {
            direction = 1;
            velocityX = isSprinting ? -4 : -2;
        } else if (Greenfoot.isKeyDown("d")) {
            direction = 0;
            velocityX = isSprinting ? 4 : 2;
        }

        boolean canStand = canStand();

        if (Greenfoot.isKeyDown("control")) {
            isCrouching = true;
            isSprinting = false;
        } else if (Greenfoot.isKeyDown("shift") && canStand) {
            isCrouching = false;
            isSprinting = true;
        } else if (canStand) {
            isCrouching = false;
            isSprinting = false;
        } else {
            isCrouching = true;
            isSprinting = false;
        }
    }

    private boolean canStand() {
        if (!isCrouching)
            return true;
        int pixelYOffset = (int) (getImage().getHeight() * 1.5 * -1);

        boolean topLeft = isTileSolidAtOffset(-(getWidth() / 2) + 1, pixelYOffset);
        boolean topMiddle = isTileSolidAtOffset(0, pixelYOffset);
        boolean topRight = isTileSolidAtOffset((getWidth() / 2) - 1, pixelYOffset);

        return !(topLeft || topMiddle || topRight);
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
                this.decreaseHealth(0.01f);
                break;
            }
        }
        getImage().mirrorHorizontally();
    }
}
