package nl.bastiaanbreemer.chase.actors;

import greenfoot.Actor;
import greenfoot.Greenfoot;
import nl.bastiaanbreemer.chase.ChaseApp;
import nl.bastiaanbreemer.chase.pickups.BombItem;
import nl.bastiaanbreemer.chase.utils.animations.AnimatedMover;
import nl.bastiaanbreemer.chase.utils.engine.TileEngine;
import nl.bastiaanbreemer.chase.utils.pickups.Pickup;
import nl.bastiaanbreemer.chase.utils.tiles.ChaseTile;
import nl.bastiaanbreemer.chase.utils.tiles.Tile;
import nl.bastiaanbreemer.chase.worlds.ChaseWorld;

import java.util.ArrayList;
import java.util.List;

public class Chaser extends AnimatedMover {

    public final static int HEALTH_MAX = 6;
    private final static String ANIMATION_PATH = "players/p1_%NAME%%FRAME%.png";
    public final int spawnX;
    public final int spawnY;
    private final double gravity;
    private final double acc;
    private final double drag;
    public ArrayList<Pickup> pickups = new ArrayList<>();
    private float health = HEALTH_MAX;
    private boolean isCrouching = false;
    private boolean isSprinting = false;
    private int direction = 0;
    private int pickupUseTimeout = 0;
    private int tick = 0;

    public Chaser(int spawnX, int spawnY) {
        super(ANIMATION_PATH);

        this.spawnX = spawnX;
        this.spawnY = spawnY;

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

    private void setLives(int lives) {
        if (lives <= 0) ChaseApp.application.gameOver();
        else ChaseApp.application.lives = lives;
    }

    public void reset() {
        setLocation(this.spawnX, this.spawnY);

        this.velocityX = 0;
        this.velocityY = 0;
        this.isCrouching = false;
        this.isSprinting = false;

        pickups.clear();
        pickups.add(new BombItem());
        pickups.add(new BombItem());
        pickups.add(new BombItem());

        this.setHealth(HEALTH_MAX);
        setLives(ChaseApp.application.getLives() - 1);
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
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

        if ((Greenfoot.isKeyDown("w") && !isCrouching) || (Greenfoot.isKeyDown("space") && !isCrouching)) {
            int pixelYOffset = (getImage().getHeight() / 2);

            if (isTileSolidAtOffset(-(getWidth() / 2) + 1, pixelYOffset))
                velocityY = -10;
            else if (isTileSolidAtOffset(0, pixelYOffset))
                velocityY = -10;
            else if (isTileSolidAtOffset((getWidth() / 2) - 1, pixelYOffset))
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

        if (Greenfoot.isKeyDown("k")) {
            this.setLives(-1);
        }
        if (Greenfoot.isKeyDown("escape")) {
            System.exit(0);
        }
    }

    private void handleDamage() {
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

    private void handlePickup() {
        List<Tile> tiles = getObjectsAtOffset(0, 0, Tile.class);

        if (tiles.size() <= 0)
            return;

        for (Tile tile : tiles) {
            ChaseTile chaseTile = (ChaseTile) tile;
            String[] types = chaseTile.type.split("/");
            if (!types[0].equals("pickup"))
                continue;
            switch (types[1]) {
                case "bomb":
                    ((ChaseWorld) getWorld()).getTileEngine().removeTileAt(tile.getColom(), tile.getRow());
                    pickups.add(new BombItem());
                    break;
                default:
                    break;
            }
        }
    }

    private boolean canStand() {
        if (!isCrouching)
            return true;
        int pixelYOffset = (int) (getImage().getHeight() * 1.5 * -1);

        if (isTileSolidAtOffset(-(getWidth() / 2) + 1, pixelYOffset))
            return false;
        else if (isTileSolidAtOffset(0, pixelYOffset))
            return false;
        else return !isTileSolidAtOffset((getWidth() / 2) - 1, pixelYOffset);
    }

    private void handleSound() {
        if (velocityX != 0)
            for (ChaseTile tile : getObjectsAtOffset(0, getHeight() / 2, ChaseTile.class)) {
                if (!tile.isSolid) continue;
                switch (tile.type) {
                    case "grass":
                        Greenfoot.playSound("sounds/walking/grass-0" + ((Math.random() <= 0.5) ? 2 : 3) + ".wav");
                        break;
                    case "sand":
                    case "snow":
                        Greenfoot.playSound("sounds/walking/sand-0" + ((Math.random() <= 0.5) ? 2 : 3) + ".wav");
                        break;
                    case "stone":
                    case "bridge":
                    case "box":
                        Greenfoot.playSound("sounds/walking/concrete-0" + ((Math.random() <= 0.5) ? 2 : 3) + ".wav");
                        break;
                    default:
                        Greenfoot.playSound("sounds/walking/dirt-0" + ((Math.random() <= 0.5) ? 2 : 3) + ".wav");
                        break;
                }
            }
    }

    @Override
    public void act() {
        tick++;
        handlePickup();
        handleInput();
        if (tick % 30 == 0)
            handleDamage();
        handleAnimations();
        if (direction == 0)
            setMirrorHorizontally(false);
        else if (direction == 1)
            setMirrorHorizontally(true);
        super.act();
        velocityX *= drag;
        velocityY += acc;
        if (velocityY > gravity) {
            velocityY = gravity;
        }
        if (tick % 60 == 0)
            handleSound();
        applyVelocity();
    }
}

// hallo dit is Gijs zijn code