package nl.bastiaanbreemer.chase.worlds;

import greenfoot.Greenfoot;
import nl.bastiaanbreemer.chase.ChaseApp;
import nl.bastiaanbreemer.chase.actors.Chaser;
import nl.bastiaanbreemer.chase.actors.Fly;
import nl.bastiaanbreemer.chase.utils.cameras.Camera;
import nl.bastiaanbreemer.chase.utils.cameras.Overlay;
import nl.bastiaanbreemer.chase.utils.cameras.ParallaxBackground;
import nl.bastiaanbreemer.chase.utils.engine.CollisionEngine;
import nl.bastiaanbreemer.chase.utils.engine.TileEngine;
import nl.bastiaanbreemer.chase.utils.engine.TileMapFactory;
import nl.bastiaanbreemer.chase.utils.worlds.BaseWorld;

public class ChaseWorld extends BaseWorld {

    public ChaseWorld() {
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(72 * 14, 72 * 12, 1, false);

        te = new TileEngine(this, 72, 72);
        tmf = new TileMapFactory();

        pb = new ParallaxBackground(this, "parallax-background_01.png");

        tmf.parseTiles();
        tmf.parseMap("tilemap");

        te.setTileFactory(tmf);

        te.setMap(tmf.getMap());
        pb.updateMap();
        // Declare and initialization of the Camera class with the TileEngine class
        // passed as a parameter to let the cameras know which tiles move along with the
        // Camera.
        Camera camera = new Camera(te);
        // Declare and initialization of the main character of the game mine is called,
        // Hero. This class needs to be extended by the mover class to work with the
        // Camera class.
        Chaser chaser = new Chaser(TileEngine.TILE_WIDTH * 5, TileEngine.TILE_HEIGHT * 5);
        // Make the Camera follow a class, the class needs to be a extension of the
        // Mover class.
        camera.follow(chaser);
        pb.setParent(chaser);
        Overlay overlay = new Overlay(this, chaser);

        // Adding all objects to the world: Camera, Hero, Fly.
        addObject(camera, 0, 0);
        addObject(chaser, chaser.spawnX, chaser.spawnY);
        Fly fly = new Fly(chaser);
        addObject(fly, 10 * TileEngine.TILE_WIDTH, TileEngine.MAP_HEIGHT / 2 * TileEngine.TILE_HEIGHT);

        // From what I can find this sets the updates/s
        // Also setting this so the increments in movement can be smaller / more fluent
        Greenfoot.setSpeed(60);

        // Initializing of the CollisionEngine, this is needed for the Mover classes to
        // not move through the tiles.
        // The CollisionEngine only looks at the tiles that have the variable solid on
        // false.
        ce = new CollisionEngine(te, camera);
        // Add the Actors that are a extension from the Mover class to the
        // CollisionEngine.
        ce.addCollidingMover(chaser);
        ChaseApp.application.setWorld(this);
        ChaseApp.application.setState(ChaseApp.State.PLAYING);
    }

    @Override
    public void act() {
        ce.update();
    }
}
