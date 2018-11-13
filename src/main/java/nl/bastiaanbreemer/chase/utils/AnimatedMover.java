package nl.bastiaanbreemer.chase.utils;

import greenfoot.GreenfootImage;

import java.util.HashMap;

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
        // Change to other animation
        this.current = animations.get(name);
        // Reset tick to start over
        this.tick = 0;
        setImage(this.current.getImagePath());
        return current;
    }

    @Override
    public void act() {
        if (current.getSpeed() == 0) {
            setImage(current.getImagePath(0));
            GreenfootImage frame = getImage();
            frame.drawRect(0, 0, frame.getWidth() - 1, frame.getHeight() - 1);
            if (mirrorHorizontally)
                frame.mirrorHorizontally();
            if (mirrorVertically)
                frame.mirrorVertically();
        } else {
            if (tick != current.getSpeed()) {
                tick++;
            } else {
                tick = 0;
                setImage(current.nextImage());
                GreenfootImage frame = getImage();
                frame.drawRect(0, 0, frame.getWidth() - 1, frame.getHeight() - 1);
                if (mirrorHorizontally)
                    frame.mirrorHorizontally();
                if (mirrorVertically)
                    frame.mirrorVertically();
            }
        }

    }
}
