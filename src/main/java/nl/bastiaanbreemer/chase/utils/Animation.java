package nl.bastiaanbreemer.chase.utils;

import greenfoot.GreenfootImage;

public class Animation {
    private final GreenfootImage[] images;
    private final String name;
    private int speed;
    private GreenfootImage current;

    public Animation(String animationPath, String name) {
        this.name = name;
        this.speed = 0;
        this.images = new GreenfootImage[]{new GreenfootImage(animationPath.replaceFirst("%NAME%", this.name).replaceFirst("%FRAME%", String.format("%02d", 1)))
        };
        this.current = this.images[0];
        debug();
    }

    public Animation(String animationPath, String name, int frames, int speed) {
        this.name = name;
        this.speed = speed;
        this.images = new GreenfootImage[frames];
        for (int i = 1; i <= frames; i++) {
            this.images[i - 1] = new GreenfootImage(animationPath.replaceFirst("%NAME%", this.name).replaceFirst("%FRAME%", String.format("%02d", i)))
            ;
            debug(this.images[i - 1]);
        }
        this.current = this.images[0];
    }

    private void debug() {
        System.out.println("-- Animation       (" + this.name + ")--");
        for (int i = 0; i < this.images.length; i++) {
            debug(this.images[i]);
        }
    }

    private void debug(GreenfootImage image) {
        System.out.println("-- Animation frame (" + this.name + ")--");
        System.out.println(image.toString());
    }

    public GreenfootImage nextImage() {
        // Return current image if there is only one image in the animation.
        if (this.images.length == 1)
            return current;
        // Find next image in the animation
        for (int i = 0; i < this.images.length; i++) {
            // Check next image if they are not the same.
            if (!this.images[i].equals(current))
                continue;
            // Check if there is a item after this item in the array.
            // If this is not the case then loop back to the first item in the array.
            if (i + 1 < this.images.length) {
                this.current = this.images[i + 1];
                return this.current;
            } else {
                this.current = this.images[0];
                return this.current;
            }
        }
        // If all else fails then keep the current image and return it.
        return this.current;
    }

    public GreenfootImage getImage() {
        return this.images[0];
    }

    public GreenfootImage getImage(int index) {
        return this.images[index];
    }

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
