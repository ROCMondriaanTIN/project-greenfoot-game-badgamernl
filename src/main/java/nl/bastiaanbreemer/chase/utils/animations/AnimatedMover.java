package nl.bastiaanbreemer.chase.utils.animations;

import greenfoot.GreenfootImage;
import nl.bastiaanbreemer.chase.utils.engine.Mover;
import nl.bastiaanbreemer.chase.utils.tiles.Tile;

import java.util.HashMap;
import java.util.List;

public class AnimatedMover extends Mover {

    private final String animationPath;
    private int tick;
    private HashMap<String, Animation> animations = new HashMap<>();
    private Animation current;
    private boolean mirrorHorizontally = false;
    private boolean mirrorVertically = false;

    public AnimatedMover(String animationPath) {
        super();
        this.animationPath = animationPath;
    }

    public void setMirrorHorizontally(boolean mirrorHorizontally) {
        this.mirrorHorizontally = mirrorHorizontally;
        getImage().mirrorHorizontally();
    }

    public void setMirrorVertically(boolean mirrorVertically) {
        this.mirrorVertically = mirrorVertically;
        getImage().mirrorVertically();
    }

    public Animation addAnimation(String name) {
        return animations.put(name, new Animation(this.animationPath, name));
    }

    public Animation addAnimation(String name, int frames, int speed) {
        return animations.put(name, new Animation(this.animationPath, name, frames, speed));
    }

    public Animation getAnimation(String name) {
        return animations.get(name);
    }

    public Animation getCurrent() {
        return current;
    }

    public Animation setAnimation(String name) {
        if (this.current != null) {
            if (this.current.equals(animations.get(name)))
                return this.current;
        }
        // Change to other animations
        this.current = animations.get(name);
        // Reset tick to start over
        this.tick = 0;
        setImage(new GreenfootImage(this.current.getImage()));
        return current;
    }

    @Override
    public void act() {
        if (current.getSpeed() == 0) {
            setImage(new GreenfootImage(current.getImage(0)));
            GreenfootImage frame = getImage();
            //frame.drawRect(0, 0, frame.getWidth() - 1, frame.getHeight() - 1);
            if (mirrorHorizontally)
                frame.mirrorHorizontally();
            if (mirrorVertically)
                frame.mirrorVertically();
        } else {
            if (tick != current.getSpeed()) {
                tick++;
            } else {
                tick = 0;
                setImage(new GreenfootImage(current.nextImage()));
                GreenfootImage frame = getImage();
                //frame.drawRect(0, 0, frame.getWidth() - 1, frame.getHeight() - 1);
                if (mirrorHorizontally)
                    frame.mirrorHorizontally();
                if (mirrorVertically)
                    frame.mirrorVertically();
            }
        }

    }

    protected boolean isTileSolidAtOffset(int dx, int dy) {
        List<Tile> tiles = getObjectsAtOffset(dx, dy, Tile.class);
        for (Tile tile : tiles) {
            if (tile.isSolid) return true;
        }
        return false;
    }

    protected float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }

    public int getWidth() {
        return getImage().getWidth();
    }

    public int getHeight() {
        return getImage().getHeight();
    }
}
