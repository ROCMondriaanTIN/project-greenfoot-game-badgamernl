package nl.bastiaanbreemer.chase.actors;

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


        bombs.remove(this);
        getWorld().removeObject(this);
    }

    @Override
    public void act() {
        super.act();
        int height = getHeight() / 2;
        int width = getWidth() / 2;
        boolean topLeft = isTileSolidAtOffset(-width + 1, -height + 1);
        boolean topRight = isTileSolidAtOffset(width - 1, -height + 1);
        boolean bottomLeft = isTileSolidAtOffset(-width + 1, height - 1);
        boolean bottomRight = isTileSolidAtOffset(width - 1, height - 1);
        if (bottomLeft && bottomRight && velocityY > 0)
            velocityY *= -1;
        if (topLeft && topRight && velocityY < 0)
            velocityY *= -1;
        if (topRight && bottomRight && velocityX > 0)
            velocityY *= -1;
        if (topLeft && bottomLeft && velocityX < 0)
            velocityY *= -1;
//        velocityX *= drag;
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
