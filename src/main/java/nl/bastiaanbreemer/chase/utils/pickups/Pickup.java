package nl.bastiaanbreemer.chase.utils.pickups;

import greenfoot.GreenfootImage;

public class Pickup {

    public final String type;
    protected final GreenfootImage image;
    private final int timeout;

    public Pickup(String image, String type, int timeout) {
        this.image = new GreenfootImage(image);
        this.type = type;
        this.timeout = timeout;
    }

    public Pickup(GreenfootImage image, String type, int timeout) {
        this.image = image;
        this.type = type;
        this.timeout = timeout;
    }

    public GreenfootImage getImage() {
        return this.image;
    }

    public int getTimeout() {
        return this.timeout;
    }
}
