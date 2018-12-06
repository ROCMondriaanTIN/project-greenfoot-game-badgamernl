package nl.bastiaanbreemer.chase.utils.engine;

import greenfoot.GreenfootImage;
import nl.bastiaanbreemer.chase.utils.tiles.ChaseTile;
import nl.bastiaanbreemer.chase.utils.tiles.Tile;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.stream.Collectors;

public class TileMapFactory extends TileFactory {
    private static HashMap<String, ITile> tiles = new HashMap<String, ITile>();

    private int[][] map;

    public void parseTiles() {
        if (TileMapFactory.tiles.size() > 0) {
            System.out.println("[INFO] parseTiles() tiles already parsed");
            return;
        }
        System.out.println("[START] parseTiles()");
        try {
            JSONObject tiles = parseFileToJSONObject("tiles.json");
            JSONArray tilesArr = (JSONArray) tiles.get("tiles");
            for (Object tileObj : tilesArr) {
                JSONObject tile = (JSONObject) tileObj;
                int id = objToInt(tile.get("id"));
                String image = (String) tile.get("image");
                JSONArray tileProperties = (JSONArray) tile.get("properties");
                ITile iTile = new ITile(image, TileEngine.TILE_WIDTH, TileEngine.TILE_HEIGHT);
                iTile.type = (String) tile.get("type");
                for (Object propertyObject : tileProperties) {
                    JSONObject property = (JSONObject) propertyObject;
                    String name = (String) property.get("name");
                    if (name.equals("isSolid")) {
                        iTile.isSolid = (boolean) property.get("value");
                    }
                    if (name.equals("mirrorHorizontally")) {
                        iTile.mirrorHorizontally = (boolean) property.get("value");
                    }
                    if (name.equals("mirrorVertically")) {
                        iTile.mirrorVertically = (boolean) property.get("value");
                    }
                    if (name.equals("damagePerTick")) {
                        iTile.damagePerTick = objToInt(property.get("value")) / 1000f;
                    }
                }
                iTile.apply();
                TileMapFactory.tiles.put(Integer.toString(id), iTile);
            }
            TileMapFactory.tiles.forEach((String id, ITile tile) ->
                System.out.println("[LOADED] ID: " + id + " path: " + tile.image));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        System.out.println("[DONE] parseTiles()");
    }

    public void parseMap(String mapName) {
        System.out.println("[START] parseMap()");
        try {
            JSONObject tileMap = parseFileToJSONObject(mapName + ".json");
            JSONArray layers = (JSONArray) tileMap.get("layers");
            for (Object layerObj : layers) {
                JSONObject layer = (JSONObject) layerObj;
                String name = (String) layer.get("name");
                if (name.equals("foreground")) {
                    JSONArray tileIDs = (JSONArray) layer.get("data");

                    int height = objToInt(layer.get("height"));
                    int width = objToInt(layer.get("width"));
                    this.map = new int[height][width];
                    int row = 0;
                    int col = 0;
                    for (Object tileID : tileIDs) {
                        int id = objToInt(tileID);
                        this.map[row][col] = id - 1; // JSON export adds 1 to the ID
                        col++;
                        if (col >= width) {
                            col = 0;
                            row++;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        System.out.println("[DONE] parseMap()");
    }

    private JSONObject parseFileToJSONObject(String filename) {
        try {
            JSONParser parser = new JSONParser();
            // json simple can't read resource streams
            InputStream stream = this.getClass().getClassLoader().getResourceAsStream(filename);
            if (stream == null)
                throw new Exception("getResourceAsStream() returned null");
            String result = new BufferedReader(new InputStreamReader(stream))
                .lines().collect(Collectors.joining("\n"));
            return (JSONObject) parser.parse(result);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return null;
    }

    private int objToInt(Object obj) {
        return Math.toIntExact((Long) obj);
    }

    public int[][] getMap() {
        return map;
    }

    @Override
    public Tile createTile(int mapIcon) {
        ITile tile = TileMapFactory.tiles.get(Integer.toString(mapIcon));
        if (tile == null)
            return TileMapFactory.tiles.get("0").toTile();
        return tile.toTile();
    }

    // Class to hold tile data, this is here because the tile factory generates a new class for every tile and you cannot copy the class.
    private class ITile {
        GreenfootImage image;
        int width;
        int height;
        boolean isSolid;
        boolean mirrorHorizontally;
        boolean mirrorVertically;
        float damagePerTick;
        String type = "undefined";

        private ITile(String image, int width, int height) {
            this.image = new GreenfootImage(image);
            this.width = width;
            this.height = height;
        }

        private void apply() {
            if (mirrorHorizontally)
                image.mirrorHorizontally();
            if (mirrorVertically)
                image.mirrorVertically();
        }

        private ChaseTile toTile() {
            ChaseTile tile = new ChaseTile(this.image, this.width, this.height);
            if (isSolid)
                tile.isSolid = true;
            tile.damagePerTick = damagePerTick;
            tile.type = this.type;
            return tile;
        }
    }
}
