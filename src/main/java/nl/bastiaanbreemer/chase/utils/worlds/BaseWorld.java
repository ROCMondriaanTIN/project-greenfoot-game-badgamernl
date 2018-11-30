package nl.bastiaanbreemer.chase.utils.worlds;

import greenfoot.World;
import nl.bastiaanbreemer.chase.utils.cameras.ParallaxBackground;
import nl.bastiaanbreemer.chase.utils.engine.CollisionEngine;
import nl.bastiaanbreemer.chase.utils.engine.TileEngine;
import nl.bastiaanbreemer.chase.utils.engine.TileMapFactory;

public abstract class BaseWorld extends World {

    protected TileEngine te;
    protected TileMapFactory tmf;
    protected CollisionEngine ce;
    protected ParallaxBackground pb;

    public BaseWorld(int worldWidth, int worldHeight, int cellSize, boolean bounded) {
        super(worldWidth, worldHeight, cellSize, bounded);
    }

    public TileEngine getTileEngine() {
        return this.te;
    }

    public TileMapFactory getTileMapFactory() {
        return this.tmf;
    }

    public CollisionEngine getCollisionEngine() {
        return this.ce;
    }

    public ParallaxBackground getParallaxBackground() {
        return this.pb;
    }
}
