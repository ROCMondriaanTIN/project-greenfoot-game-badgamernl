package nl.bastiaanbreemer.chase.worlds;

import greenfoot.Greenfoot;
import greenfoot.World;
import nl.bastiaanbreemer.chase.actors.Chaser;
import nl.bastiaanbreemer.chase.actors.Enemy;
import nl.bastiaanbreemer.chase.utils.Camera;
import nl.bastiaanbreemer.chase.utils.CollisionEngine;
import nl.bastiaanbreemer.chase.utils.TileEngine;
import nl.bastiaanbreemer.chase.utils.TileMapFactory;

public class ChaseWorld extends World {

    private CollisionEngine ce;

    /**
     * Constructor for objects of class MyWorld.
     */
    public ChaseWorld() {
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(1000, 800, 1, false);
        this.setBackground("bg.png");

        // Declare and initialization of the TileEngine class to add the map to the
        // world.
        TileEngine te = new TileEngine(this, 72, 72);

        TileMapFactory tmf = new TileMapFactory();

        tmf.parseTiles();
        tmf.parseMap("tilemap");

        te.setTileFactory(tmf);

        te.setMap(tmf.getMap());
        // Declare and initialization of the Camera class with the TileEngine class
        // passed as a parameter to let the camera know which tiles move along with the
        // Camera.
        Camera camera = new Camera(te);
        // Declare and initialization of the main character of the game mine is called,
        // Hero. This class needs to be extended by the mover class to work with the
        // Camera class.
        Chaser chaser = new Chaser();
        // Make the Camera follow a class, the class needs to be a extension of the
        // Mover class.
        camera.follow(chaser);

        // Adding all objects to the world: Camera, Hero, Enemy.
        addObject(camera, 0, 0);
        addObject(chaser, 300, 200);
        addObject(new Enemy(), 20 * 72 - 30, 7 * 72);

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
    }

    @Override
    public void act() {
        ce.update();
    }
}
