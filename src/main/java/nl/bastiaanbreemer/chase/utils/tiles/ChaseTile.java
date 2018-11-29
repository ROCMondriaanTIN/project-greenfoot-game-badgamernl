package nl.bastiaanbreemer.chase.utils.tiles;

import greenfoot.GreenfootImage;

public class ChaseTile extends Tile {
    public String type = "";
    public float damagePerTick = 0.0f;

    public ChaseTile(GreenfootImage image, int width, int height) {
        super(image, width, height);
    }
}
