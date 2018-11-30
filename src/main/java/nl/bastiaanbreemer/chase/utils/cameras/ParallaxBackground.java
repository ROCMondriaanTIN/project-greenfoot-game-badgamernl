package nl.bastiaanbreemer.chase.utils.cameras;

import greenfoot.Actor;
import greenfoot.GreenfootImage;
import greenfoot.World;
import nl.bastiaanbreemer.chase.actors.Chaser;
import nl.bastiaanbreemer.chase.utils.engine.TileEngine;

public class ParallaxBackground extends Actor {

    private final double WIDTH;
    private final double HEIGHT;
    private final double BG_WIDTH;
    private final double BG_HEIGHT;
    private final GreenfootImage image;
    private double mapWidth;
    private double mapHeight;
    private int prevParentX;
    private int prevParentY;
    private Chaser parent;

    public ParallaxBackground(World world, String image) {
        this.image = new GreenfootImage(image);
        BG_WIDTH = this.image.getWidth();
        BG_HEIGHT = this.image.getHeight();
        world.addObject(this, world.getWidth() / 2, world.getHeight() / 2);
        // Setting to a empty image with the world dimensions
        WIDTH = world.getWidth();
        HEIGHT = world.getHeight();
        this.setImage(new GreenfootImage((int) WIDTH, (int) HEIGHT));
    }

    public void updateMap() {
        this.mapWidth = TileEngine.MAP_WIDTH * TileEngine.TILE_WIDTH;
        this.mapHeight = TileEngine.MAP_HEIGHT * TileEngine.TILE_HEIGHT;
    }

    public void setParent(Chaser parent) {
        this.parent = parent;
    }

    private void draw(GreenfootImage g2d) {
        if (parent == null)
            return;
        double topLeft = parent.getX() - WIDTH / 2;
        double topRight = parent.getX() + WIDTH / 2;
        double imagesX = Math.ceil(mapWidth / BG_WIDTH);
        int parentX = getParentLeft();
        int parentY = getParentTop();
        if (parentX == prevParentX && parentY == prevParentY) {
            return;
        }

        for (int i = 0; i < imagesX; i++) {
            double imageX = i * BG_WIDTH;
            if (imageX > topLeft && imageX + BG_WIDTH < topRight)
                continue;
            double offsetX = imageX - parentX;
            g2d.drawImage(image, (int) offsetX, 0);
        }
        prevParentX = parentX;
        prevParentY = parentY;
    }

    private int getParentLeft() {
        return parent.getX() - (parent.getWidth() / 2);
    }

    private int getParentTop() {
        return parent.getY() - (parent.getHeight() / 2);
    }

    @Override
    public void act() {
        this.draw(getImage());
    }
}
