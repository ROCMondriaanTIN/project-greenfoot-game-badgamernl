package nl.bastiaanbreemer.chase.utils;

import greenfoot.World;

import java.util.List;

/**
 * @author R. Springer
 */
public class TileEngine {

    public static int TILE_WIDTH;
    public static int TILE_HEIGHT;
    public static int SCREEN_HEIGHT;
    public static int SCREEN_WIDTH;
    public static int MAP_WIDTH;
    public static int MAP_HEIGHT;

    private World world;
    private int[][] map;
    private Tile[][] generateMap;
    private TileFactory tileFactory;

    /**
     * Constructor of the TileEngine
     *
     * @param world      A World class or a extend of it.
     * @param tileWidth  The width of the tile used in the TileFactory and
     *                   calculations
     * @param tileHeight The height of the tile used in the TileFactory and
     *                   calculations
     */
    public TileEngine(World world, int tileWidth, int tileHeight) {
        this.world = world;
        TILE_WIDTH = tileWidth;
        TILE_HEIGHT = tileHeight;
        SCREEN_WIDTH = world.getWidth();
        SCREEN_HEIGHT = world.getHeight();
        this.tileFactory = new TileFactory();
    }

    /**
     * Constructor of the TileEngine
     *
     * @param world      A World class or a extend of it.
     * @param tileWidth  The width of the tile used in the TileFactory and
     *                   calculations
     * @param tileHeight The height of the tile used in the TileFactory and
     *                   calculations
     * @param map        A tilemap with numbers
     */
    public TileEngine(World world, int tileWidth, int tileHeight, int[][] map) {
        this(world, tileWidth, tileHeight);
        this.setMap(map);
    }

    /**
     * The setMap method used to set a map. This method also clears the previous map
     * and generates a new one.
     *
     * @param map 2D int array of Tile id's
     */
    public void setMap(int[][] map) {
        this.clearTilesWorld();
        this.map = map;
        MAP_HEIGHT = this.map.length;
        MAP_WIDTH = this.map[0].length;
        this.generateMap = new Tile[MAP_HEIGHT][MAP_WIDTH];
        this.generateWorld();
    }

    /**
     * The setTileFactory sets a tileFactory. You can use this if you want to create
     * you own tileFactory and use it in the class.
     *
     * @param tf A tileFactory or extend of it.
     */
    public void setTileFactory(TileFactory tf) {
        this.tileFactory = tf;
    }

    /**
     * Removes al the tiles from the world.
     */
    public void clearTilesWorld() {
        List<Tile> removeObjects = this.world.getObjects(Tile.class);
        this.world.removeObjects(removeObjects);
        this.map = null;
        this.generateMap = null;
        MAP_HEIGHT = 0;
        MAP_WIDTH = 0;
    }

    /**
     * Creates the tile world based on the TileFactory and the map icons.
     */
    public void generateWorld() {
        for (int y = 0; y < MAP_HEIGHT; y++) {
            for (int x = 0; x < MAP_WIDTH; x++) {
                // Getting number in the int array
                int mapIcon = this.map[y][x];

                // If the mapIcon is -1 then the code below this if statement is skipped
                // (-1 tile id is air so no reason to add it because it doesn't have a texture,
                // the air texture is set in the world class by the background image)
                if (mapIcon == -1) {
                    continue;
                }

                Tile createdTile = this.tileFactory.createTile(mapIcon);

                addTileAt(createdTile, x, y);
            }
        }
    }

    /**
     * Adds a tile on the column and row. Calculation is based on TILE_WIDTH and
     * TILE_HEIGHT
     *
     * @param tile   The Tile
     * @param column The column where the tile exist in the map
     * @param row    The row where the tile exist in the map
     */
    public void addTileAt(Tile tile, int column, int row) {
        // The X and Y position are int the middle of the Actor.
        // The tilemap generates a world based on if there's a X and a Y
        // in the top left position. That's why half of the width and the height are added,
        // the X and the Y are in the top left of the Actor before adding it to the object.
        this.world.addObject(tile, (column * TILE_WIDTH) + TILE_WIDTH / 2, (row * TILE_HEIGHT) + TILE_HEIGHT / 2);
        // Adding to local array, for ease of use to get a tile from the array based on X and Y position.
        this.generateMap[row][column] = tile;
    }

    /**
     * Retrieves a tile at the location based on column and row in the map
     *
     * @param column column index
     * @param row    row index
     * @return The tile at the location column and row. Returns null if it cannot
     * find a tile.
     */
    public Tile getTileAt(int column, int row) {
        try {
            return this.generateMap[row][column];
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Retrieves a tile based on a x and y position in the world
     *
     * @param x X-position in the world
     * @param y Y-position in the world
     * @return The tile at the location column and row. Returns null if it cannot
     * find a tile.
     */
    public Tile getTileAtXY(int x, int y) {
        int col = getColumn(x);
        int row = getRow(y);

        Tile tile = getTileAt(col, row);
        return tile;
    }

    /**
     * This method checks if a tile on a x and y position in the world is solid or
     * not.
     *
     * @param x X-position in the world
     * @param y Y-position in the world
     * @return Tile at location is solid
     */
    public boolean checkTileSolid(int x, int y) {
        Tile tile = getTileAtXY(x, y);
        if (tile != null && tile.isSolid) {
            return true;
        }
        return false;
    }

    /**
     * This method returns a column based on a x position.
     *
     * @param x column index
     * @return the column
     */
    public int getColumn(int x) {
        return (int) Math.floor(x / TILE_WIDTH);
    }

    /**
     * This method returns a row based on a y position.
     *
     * @param y row index
     * @return the row
     */
    public int getRow(int y) {
        return (int) Math.floor(y / TILE_HEIGHT);
    }

    /**
     * This method returns a x position based on the column
     *
     * @param col col index
     * @return The x position
     */
    public int getX(int col) {
        return col * TILE_WIDTH;
    }

    /**
     * This method returns a y position based on the row
     *
     * @param row row index
     * @return The y position
     */
    public int getY(int row) {
        return row * TILE_HEIGHT;
    }

}
