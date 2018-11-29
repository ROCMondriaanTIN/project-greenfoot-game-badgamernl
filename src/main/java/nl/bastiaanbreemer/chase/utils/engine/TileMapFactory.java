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
    private HashMap<String, ITile> tiles = new HashMap<String, ITile>();

    private int[][] map;

    public void parseTiles() {
        System.out.println("[START] parseTiles()");
        JSONParser parser = new JSONParser();
        try {
            // json simple can't read resource streams
            InputStream stream = this.getClass().getClassLoader().getResourceAsStream("tiles.json");
            if (stream == null)
                throw new Exception("getResourceAsStream() returned null");
            String result = new BufferedReader(new InputStreamReader(stream))
                .lines().collect(Collectors.joining("\n"));
            JSONObject tilesDataObj = (JSONObject) parser.parse(result);
            JSONArray jsonTiles = (JSONArray) tilesDataObj.get("tiles");
            for (Object tileObject : jsonTiles) {
                JSONObject tileObj = (JSONObject) tileObject;
                int id = Math.toIntExact((Long) tileObj.get("id"));
                String image = (String) tileObj.get("image");
//                int height = Math.toIntExact((Long) tileObj.get("imageheight"));
//                int width = Math.toIntExact((Long) tileObj.get("imagewidth"));
                JSONArray tileProperties = (JSONArray) tileObj.get("properties");
                ITile tile = new ITile(image, TileEngine.TILE_WIDTH, TileEngine.TILE_HEIGHT);
                tile.type = (String) tileObj.get("type");
                for (Object propertyObject : tileProperties) {
                    JSONObject property = (JSONObject) propertyObject;
                    String name = (String) property.get("name");
                    if (name.equals("isSolid")) {
                        tile.isSolid = (boolean) property.get("value");
                    }
                    if (name.equals("mirrorHorizontally")) {
                        tile.mirrorHorizontally = (boolean) property.get("value");
                    }
                    if (name.equals("mirrorVertically")) {
                        tile.mirrorVertically = (boolean) property.get("value");
                    }
                    if (name.equals("damagePerTick")) {
                        tile.damagePerTick = Math.toIntExact((Long) property.get("value")) / 1000f;
                    }
                }
                tile.apply();
                this.tiles.put(Integer.toString(id), tile);
            }
            this.tiles.forEach((String id, ITile tile) ->
                System.out.println("[LOADED] ID: " + id + " path: " + tile.image));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        System.out.println("[DONE] parseTiles()");
    }

    public void parseMap(String mapName) {
        System.out.println("[START] parseMap()");
        JSONParser parser = new JSONParser();
        try {
            // json simple can't read resource streams
            InputStream stream = this.getClass().getClassLoader().getResourceAsStream(mapName + ".json");
            if (stream == null)
                throw new Exception("getResourceAsStream() returned null");
            String result = new BufferedReader(new InputStreamReader(stream))
                .lines().collect(Collectors.joining("\n"));
            JSONObject tilesMapObj = (JSONObject) parser.parse(result);
            JSONArray tileMapLayersArr = (JSONArray) tilesMapObj.get("layers");
            for (Object tileMapLayer : tileMapLayersArr) {
                JSONObject tileMapLayerObj = (JSONObject) tileMapLayer;
                String name = (String) tileMapLayerObj.get("name");

                if (name.equals("foreground")) {
                    JSONArray tileIDs = (JSONArray) tileMapLayerObj.get("data");

                    int height = Math.toIntExact((Long) tileMapLayerObj.get("height"));
                    int width = Math.toIntExact((Long) tileMapLayerObj.get("width"));
                    this.map = new int[height][width];
                    int row = 0;
                    int col = 0;
                    for (Object tileID : tileIDs) {
                        int id = Math.toIntExact((Long) tileID);
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

    public int[][] getMap() {
        return map;
    }

    @Override
    public Tile createTile(int mapIcon) {
        ITile tile = tiles.get(Integer.toString(mapIcon));
        if (tile == null)
            return tiles.get("0").toTile();
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

        public ITile(String image, int width, int height) {
            this.image = new GreenfootImage(image);
            this.width = width;
            this.height = height;
        }

        public void apply() {
            if (mirrorHorizontally)
                image.mirrorHorizontally();
            if (mirrorVertically)
                image.mirrorVertically();
        }

        public ChaseTile toTile() {
            ChaseTile tile = new ChaseTile(this.image, this.width, this.height);
            if (isSolid)
                tile.isSolid = true;
            tile.damagePerTick = damagePerTick;
            tile.type = this.type;
            return tile;
        }
    }
}
