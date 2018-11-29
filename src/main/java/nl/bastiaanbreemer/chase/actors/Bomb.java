package nl.bastiaanbreemer.chase.actors;

import greenfoot.GreenfootSound;
import nl.bastiaanbreemer.chase.utils.animations.AnimatedMover;
import nl.bastiaanbreemer.chase.utils.animations.Animation;

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
        new GreenfootSound("sounds/small-explosion-" + ((Math.random() <= 0.5) ? 1 : 2) + ".wav").play();

        bombs.remove(this);
        getWorld().removeObject(this);
    }

    @Override
    public void act() {
        super.act();
        if (velocityX > 0)
            setRotation(getRotation() + 2);
        else if (velocityX < 0)
            setRotation(getRotation() - 2);
        System.out.println(velocityX);
        int height = getHeight() / 2;
        int width = getWidth() / 2;
        boolean topLeft = isTileSolidAtOffset(-width + 2, -height + 2);
        boolean topRight = isTileSolidAtOffset(width - 2, -height + 2);
        boolean bottomLeft = isTileSolidAtOffset(-width + 2, height - 2);
        boolean bottomRight = isTileSolidAtOffset(width - 2, height - 2);

        boolean sideCollision = false;
        if (bottomLeft && bottomRight && velocityY > 0) {
            velocityY *= -0.9;
            sideCollision = true;
        } else if (topLeft && topRight && velocityY < 0) {
            velocityY *= -1;
            sideCollision = true;
        } else if (topRight && bottomRight && velocityX > 0) {
            velocityX *= -1;
            sideCollision = true;
        } else if (topLeft && bottomLeft && velocityX < 0) {
            velocityX *= -1;
            sideCollision = true;
        }
//        if (!sideCollision) {
//            if (topLeft && velocityX < 0) {
//                velocityX *= -1;
//            } else if (topRight && velocityY < 0) {
//                velocityY *= -1;
//            }
//        }

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
