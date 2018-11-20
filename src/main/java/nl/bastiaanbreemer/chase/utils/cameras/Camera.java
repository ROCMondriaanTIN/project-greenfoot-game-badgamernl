package nl.bastiaanbreemer.chase.utils.cameras;

import greenfoot.Actor;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import nl.bastiaanbreemer.chase.utils.engine.CollisionEngine;
import nl.bastiaanbreemer.chase.utils.engine.Mover;
import nl.bastiaanbreemer.chase.utils.engine.TileEngine;
import nl.bastiaanbreemer.chase.utils.tiles.Tile;

/**
 * @author R. Springer
 */
public class Camera extends Actor {

    public static int SPEED = 10;
    private final TileEngine tileEngine;
    private int width;
    private int height;
    private int maxX;
    private int maxY;
    private double cameraDrag;
    private double dirX;
    private double dirY;
    private boolean follow;
    private Mover followActor;
    private boolean prevSwitchCameraDown;
    private boolean currentSwitchCameraDown;

    /**
     * The constructor of the Camera class Camera class moves the Tiles and Mover
     * classes around according to the cameras position. Uses the tile engine to
     * retrieve the tiles to move.
     *
     * @param tileEngine TileEngine that is used to retrieve the tiles.
     */
    public Camera(TileEngine tileEngine) {
        this.width = TileEngine.SCREEN_WIDTH;
        this.height = TileEngine.SCREEN_HEIGHT;
        this.setLocation(0, 0);
        this.maxX = TileEngine.MAP_WIDTH * TileEngine.TILE_WIDTH - this.width;
        this.maxY = TileEngine.MAP_HEIGHT * TileEngine.TILE_HEIGHT - this.height;
        this.tileEngine = tileEngine;
        this.cameraDrag = 0.9;
        this.setImage(new GreenfootImage(1, 1));
    }

    /**
     * The constructor of the Camera class Camera class moves the Tiles and Mover
     * classes around according to the cameras position. Uses the tile engine to
     * retrieve the tiles to move.
     *
     * @param tileEngine TileEngine that is used to retrieve te tiles.
     * @param speed      the speed of the movement of the cameras (Free movement)
     */
    public Camera(TileEngine tileEngine, int speed) {
        this(tileEngine);
        SPEED = speed;
    }

    /**
     * This method will make this class follow the Mover you give.
     *
     * @param mover A Mover class or an extend of it. The Mover class is able to be
     *              followed.
     */
    public void follow(Mover mover) {
        this.follow = true;
        mover.setCamera(this);
        this.followActor = mover;
    }

    @Override
    public void act() {
        dirX *= cameraDrag;
        dirY *= cameraDrag;

        int x;
        int y;

        // If you are in debug mode, you can decouple the cameras from the actor with the "e" key.
        // Then you can control the cameras with the arrow keys/
        if (CollisionEngine.DEBUG) {
            currentSwitchCameraDown = Greenfoot.isKeyDown("e");
            if (currentSwitchCameraDown && !prevSwitchCameraDown) {
                follow = !follow;
            }
            prevSwitchCameraDown = currentSwitchCameraDown;
        }

        if (follow) {
            this.followActor.screenX = this.width / 2;
            this.followActor.screenY = this.height / 2;

            x = this.followActor.getX() - this.width / 2;
            y = this.followActor.getY() - this.height / 2;

            x = Math.max(0, Math.min(x, this.maxX));
            y = Math.max(0, Math.min(y, this.maxY));

            this.setLocation(x, y);

            // left and right sides
            if (this.followActor.getX() < this.width / 2 || this.followActor.getX() > this.maxX + this.width / 2) {

                this.followActor.screenX = this.followActor.getX() - this.getX();
            }

            // top and bottom sides
            if (this.followActor.getY() < this.height / 2 || this.followActor.getY() > this.maxY + this.height / 2) {
                this.followActor.screenY = this.followActor.getY() - this.getY();
            }
        } else {
            if (Greenfoot.isKeyDown("UP")) {
                dirY = -1;
            } else if (Greenfoot.isKeyDown("DOWN")) {
                dirY = 1;
            }
            if (Greenfoot.isKeyDown("LEFT")) {
                dirX = -1;
            } else if (Greenfoot.isKeyDown("RIGHT")) {
                dirX = 1;
            }
            this.move(dirX, dirY);
        }

        this.updateView();
    }

    /**
     * This method can be used to move the cameras around the world. Make sure you
     * don't move when you are following.
     *
     * @param dirX The direction x
     * @param dirY The direction y
     */
    public void move(double dirX, double dirY) {
        int x = this.getX();
        int y = this.getY();

        x += dirX * SPEED;
        y += dirY * SPEED;

        x = Math.max(0, Math.min(x, this.maxX));
        y = Math.max(0, Math.min(y, this.maxY));
        this.setLocation(x, y);
        System.out.println("Move at location: " + getX() + ", " + getY());
    }

    /**
     * This method will update the Tiles from the engine to match the view. Also it
     * will update all the Movers classes in the world. So objects move around with
     * the cameras. If an Actor class is added it will not move around with the
     * cameras. It has to be a class that is the Mover or a extend of the Mover
     * class.
     */
    public void updateView() {
        int startCol = (int) Math.floor(this.getY() / TileEngine.TILE_HEIGHT);
        int endCol = startCol + (this.height / TileEngine.TILE_HEIGHT);
        int startRow = (int) Math.floor(this.getX() / TileEngine.TILE_WIDTH);
        int endRow = startRow + (this.width / TileEngine.TILE_WIDTH);
        int offsetX = -this.getX() + startCol * TileEngine.TILE_WIDTH;
        int offsetY = -this.getY() + startRow * TileEngine.TILE_HEIGHT;

        Tile currentTile;
        int x = 0;
        int y = 0;
        for (y = 0; y < TileEngine.MAP_HEIGHT; y++) {
            for (x = 0; x < TileEngine.MAP_WIDTH; x++) {
                currentTile = tileEngine.getTileAt(x, y);
                if (currentTile == null) {
                    continue;
                }

                int xPos = ((x - startCol) * TileEngine.TILE_WIDTH + offsetX) + (TileEngine.TILE_WIDTH / 2);
                int yPos = ((y - startRow) * TileEngine.TILE_HEIGHT + offsetY) + (TileEngine.TILE_HEIGHT / 2);
                currentTile.setLocation(xPos, yPos);
            }
        }

        if (!this.follow) {
            this.followActor.screenX = this.followActor.getX() - this.getX();
            this.followActor.screenY = this.followActor.getY() - this.getY();
        }

        for (Mover actor : this.getWorld().getObjects(Mover.class)) {
            if (actor == this.followActor) {
                continue;
            }
            actor.screenX = actor.getX() - this.getX();
            actor.screenY = actor.getY() - this.getY();
        }
    }
}
