package nl.bastiaanbreemer.chase.worlds;

import greenfoot.Greenfoot;
import nl.bastiaanbreemer.chase.ChaseApp;
import nl.bastiaanbreemer.chase.actors.Chaser;
import nl.bastiaanbreemer.chase.utils.cameras.Camera;
import nl.bastiaanbreemer.chase.utils.cameras.Overlay;
import nl.bastiaanbreemer.chase.utils.cameras.ParallaxBackground;
import nl.bastiaanbreemer.chase.utils.engine.CollisionEngine;
import nl.bastiaanbreemer.chase.utils.engine.TileEngine;
import nl.bastiaanbreemer.chase.utils.engine.TileMapFactory;
import nl.bastiaanbreemer.chase.utils.worlds.BaseWorld;

public class GameWon extends BaseWorld {

    public GameWon() {
        super(72 * 14, 72 * 12, 1, false);
        te = new TileEngine(this, 72, 72);
        tmf = new TileMapFactory();

        pb = new ParallaxBackground(this, "parallax-background_01.png");

        tmf.parseTiles();
        tmf.parseMap("gameWon");

        te.setTileFactory(tmf);

        te.setMap(tmf.getMap());
        pb.updateMap();
        Camera camera = new Camera(te);
        Chaser chaser = new Chaser(TileEngine.TILE_WIDTH * 10, TileEngine.TILE_HEIGHT * 9);
        camera.follow(chaser);
        pb.setParent(chaser);
        Overlay overlay = new Overlay(this, chaser);
        addObject(camera, 0, 0);
        addObject(chaser, chaser.spawnX, chaser.spawnY);
        Greenfoot.setSpeed(60);

        ce = new CollisionEngine(te, camera);
        ce.addCollidingMover(chaser);
        ChaseApp.application.setWorld(this);
        ChaseApp.application.setState(ChaseApp.State.WON);
    }

    @Override
    public void act() {
        ce.update();
    }
}
