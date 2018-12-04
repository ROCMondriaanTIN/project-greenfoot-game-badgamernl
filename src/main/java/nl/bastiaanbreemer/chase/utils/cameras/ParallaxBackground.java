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
    private double prevParentX;
    private double prevParentY;
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

        double halfWidth = WIDTH / 2;
        double halfHeight = HEIGHT / 2;
        // Get parent pos clamped to the inner world dimensions because camera x,y is private.
        double px = clamp(parent.getX(), halfWidth, mapWidth - halfWidth);
        double py = clamp(parent.getY(), halfHeight, mapHeight - halfHeight);

        double imagesX = Math.ceil(mapWidth / BG_WIDTH);
        double imagesY = Math.ceil(mapHeight / BG_HEIGHT);
        if (px == prevParentX && py == prevParentY) {
            return;
        }
        for (int y = 0; y < imagesY; y++) {
            for (int x = 0; x < imagesX; x++) {
                double imageX = x * BG_WIDTH;
                double imageY = y * BG_HEIGHT;
                // Check if image is out of vision.
                if (imageX > px - WIDTH / 2 && imageX + BG_WIDTH < px + WIDTH / 2)
                    continue;
                if (imageY > py + HEIGHT / 2 && imagesY + BG_HEIGHT < py - HEIGHT / 2)
                    continue;
                double offsetX = imageX - px - (double) parent.getWidth() / 2;
                double offsetY = imageY - py - (double) parent.getHeight() / 2;
                g2d.drawImage(image, (int) offsetX, (int) offsetY);
            }
        }
        prevParentX = px;
        prevParentY = py;
    }

    protected double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }

    @Override
    public void act() {
        this.draw(getImage());
    }
}
