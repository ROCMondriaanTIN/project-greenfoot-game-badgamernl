package nl.bastiaanbreemer.chase.utils;

import greenfoot.Actor;

/**
 * @author R. Springer
 */
public class Tile extends Actor {

    private static int id;
    public boolean isSolid = false;
    public int _id;

    /**
     * Constructor of the tile. Creates a tile based on image, width and height
     *
     * @param image  Path to the image file
     * @param width  Width of the tile
     * @param height Height of the tile
     */
    public Tile(String image, int width, int height) {
        super();
        setImage(image);
        getImage().scale(width, height);
        if (CollisionEngine.DEBUG) {
            getImage().drawString("ÏD: " + id, 10, 10);
        }
        _id = id;
        id++;
    }

    @Override
    public String toString() {
        return "id: " + _id + "\n" + "X: " + getX() + "\n" + "Y: " + getY();
    }
}
