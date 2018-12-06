package nl.bastiaanbreemer.chase.utils.worlds;

import greenfoot.World;
import nl.bastiaanbreemer.chase.utils.cameras.ParallaxBackground;
import nl.bastiaanbreemer.chase.utils.engine.CollisionEngine;
import nl.bastiaanbreemer.chase.utils.engine.TileEngine;
import nl.bastiaanbreemer.chase.utils.engine.TileMapFactory;

public abstract class BaseWorld extends World {

    protected final int id;
    protected TileEngine te;
    protected TileMapFactory tmf;
    protected CollisionEngine ce;
    protected ParallaxBackground pb;

    public BaseWorld(int worldWidth, int worldHeight, int cellSize, boolean bounded, int id) {
        super(worldWidth, worldHeight, cellSize, bounded);
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public TileEngine getTileEngine() {
        return this.te;
    }

    protected void setTileEngine(TileEngine te) {
        this.te = te;
    }

    public TileMapFactory getTileMapFactory() {
        return this.tmf;
    }

    protected void setTileMapFactory(TileMapFactory tmf) {
        this.tmf = tmf;
    }

    public CollisionEngine getCollisionEngine() {
        return this.ce;
    }

    protected void setCollisionEngine(CollisionEngine ce) {
        this.ce = ce;
    }

    public ParallaxBackground getParallaxBackground() {
        return this.pb;
    }

    protected void setParallaxBackground(ParallaxBackground pb) {
        this.pb = pb;
    }
}
