package nl.bastiaanbreemer.chase.pickups;

import greenfoot.GreenfootImage;
import nl.bastiaanbreemer.chase.utils.pickups.Pickup;

public class BombItem extends Pickup {
    public static int TIMEOUT = 600;
    public static String TYPE = "bomb";
    public static GreenfootImage IMG = new GreenfootImage("items/bomb01.png");

    public BombItem() {
        super(IMG, TYPE, TIMEOUT);
    }
}
