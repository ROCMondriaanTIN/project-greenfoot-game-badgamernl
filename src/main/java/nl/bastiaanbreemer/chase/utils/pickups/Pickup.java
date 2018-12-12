package nl.bastiaanbreemer.chase.utils.pickups;

import greenfoot.GreenfootImage;

public class Pickup {

    public final String type;
    protected final GreenfootImage image;

    public Pickup(String image, String type) {
        this.image = new GreenfootImage(image);
        this.type = type;
    }

    public Pickup(GreenfootImage image, String type) {
        this.image = image;
        this.type = type;
    }

    public GreenfootImage getImage() {
        return this.image;
    }
}
