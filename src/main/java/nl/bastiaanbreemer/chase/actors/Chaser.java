package nl.bastiaanbreemer.chase.actors;

import greenfoot.Actor;
import greenfoot.Greenfoot;
import nl.bastiaanbreemer.chase.pickups.BombItem;
import nl.bastiaanbreemer.chase.utils.animations.AnimatedMover;
import nl.bastiaanbreemer.chase.utils.engine.TileEngine;
import nl.bastiaanbreemer.chase.utils.pickups.Pickup;
import nl.bastiaanbreemer.chase.utils.tiles.ChaseTile;
import nl.bastiaanbreemer.chase.utils.tiles.Tile;
import nl.bastiaanbreemer.chase.worlds.ChaseWorld;

import java.util.ArrayList;

public class Chaser extends AnimatedMover {

    public final static int HEALTH_MAX = 6;
    public final static int LIVES_MAX = 4;
    public final static int START_X = TileEngine.TILE_WIDTH * 5;
    public final static int START_Y = TileEngine.TILE_HEIGHT * 5;
    private final static String ANIMATION_PATH = "players/p1_%NAME%%FRAME%.png";
    private final double gravity;
    private final double acc;
    private final double drag;
    public ArrayList<Pickup> pickups = new ArrayList<>();
    private float health = HEALTH_MAX;
    private boolean isCrouching = false;
    private boolean isSprinting = false;
    private int direction = 0;
    private int lives = LIVES_MAX;
    private int pickupUseTimeout = 0;

    public Chaser() {
        super(ANIMATION_PATH);
        gravity = 5.0;
        acc = 0.2;
        drag = 0.8;

        reset();

        // Adding all animations
        addAnimation("duck");
        addAnimation("front");
        addAnimation("hurt");
        addAnimation("jump");
        addAnimation("stand");
        addAnimation("walk", 11, 10);

        setAnimation("stand");
    }

    public int getLives() {
        return lives;
    }

    private void setLives(int lives) {
        if (lives < 0) gameOver();
        this.lives = lives;
    }

    public void gameOver() {
        Greenfoot.setWorld(new ChaseWorld());
    }

    public void reset() {
        this.setHealth(HEALTH_MAX);
        this.velocityX = 0;
        this.velocityY = 0;
        this.isCrouching = false;
        this.isSprinting = false;

        pickups.clear();
        pickups.add(new BombItem());
        pickups.add(new BombItem());
        pickups.add(new BombItem());

        setLocation(START_X, START_Y);
        setLives(lives - 1);
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void increaseHealth(float amount) {
        this.health = clamp(this.health + amount, 0, HEALTH_MAX);
        if (this.health == 0) reset();
    }

    public void decreaseHealth(float amount) {
        this.health = clamp(this.health - amount, 0, HEALTH_MAX);
        if (this.health == 0) reset();
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
        if (pickupUseTimeout > 0)
            pickupUseTimeout--;
        if (pickupUseTimeout == 0 && Greenfoot.isKeyDown("b")) {
            for (Pickup pickup : pickups) {
                if (!pickup.type.equals("bomb"))
                    continue;
                double bombVelocityX = direction == 1 ? -10 : 10;
                Bomb bomb = new Bomb(bombVelocityX, -7 + velocityY);
                getWorld().addObject(bomb, 0, 0);
                bomb.setLocation(getX(), getY() + 1);
                pickups.remove(pickup);
                pickupUseTimeout = pickup.getTimeout();
                break;
            }
        }

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

    private void handleDamage() {
        for (Actor enemy : getIntersectingObjects(Enemy.class)) {
            if (enemy != null) {
                this.decreaseHealth(0.01f);
                break;
            }
        }
        for (Actor actor : getIntersectingObjects(Tile.class)) {
            if (actor != null) {
                ChaseTile tile = (ChaseTile) actor;
                if (tile.damagePerTick > 0) {
                    decreaseHealth(tile.damagePerTick);
                }
            }
        }

        if (getY() > TileEngine.MAP_HEIGHT * TileEngine.TILE_HEIGHT)
            reset();
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
        handleDamage();
        getImage().mirrorHorizontally();
    }
}
