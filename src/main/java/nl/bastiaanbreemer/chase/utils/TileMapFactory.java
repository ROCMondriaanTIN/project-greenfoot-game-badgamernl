package nl.bastiaanbreemer.chase.utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.net.URL;
import java.util.HashMap;

public class TileMapFactory extends TileFactory {
    private HashMap<String, ITile> tiles = new HashMap<String, ITile>();

    private int[][] map;

    public void parseTiles() {
        System.out.println("[START] parseTiles()");
        JSONParser parser = new JSONParser();
        try {
            URL url = this.getClass().getClassLoader().getResource("tiles.json");
            if (url == null)
                throw new Exception("getResource() returned null");
            JSONObject tilesDataObj = (JSONObject) parser.parse(new FileReader(url.getFile()));
            JSONArray jsonTiles = (JSONArray) tilesDataObj.get("tiles");
            for (Object tileObject : jsonTiles) {
                JSONObject tileObj = (JSONObject) tileObject;
                int id = Math.toIntExact((Long) tileObj.get("id"));
                String image = (String) tileObj.get("image");
//                int height = Math.toIntExact((Long) tileObj.get("imageheight"));
//                int width = Math.toIntExact((Long) tileObj.get("imagewidth"));
                JSONArray tileProperties = (JSONArray) tileObj.get("properties");
                ITile tile = new ITile(image, TileEngine.TILE_WIDTH, TileEngine.TILE_HEIGHT);
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
                }
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
            URL url = this.getClass().getClassLoader().getResource(mapName + ".json");
            if (url == null)
                throw new Exception("getResource() returned null");
            JSONObject tilesMapObj = (JSONObject) parser.parse(new FileReader(url.getFile()));
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
        String image;
        int width;
        int height;
        boolean isSolid;
        boolean mirrorHorizontally;
        boolean mirrorVertically;

        public ITile(String image, int width, int height) {
            this.image = image;
            this.width = width;
            this.height = height;
        }

        public Tile toTile() {
            Tile tile = new Tile(this.image, this.width, this.height);
            if (isSolid)
                tile.isSolid = true;
            if (mirrorHorizontally)
                tile.getImage().mirrorHorizontally();
            if (mirrorVertically)
                tile.getImage().mirrorVertically();
            return tile;
        }
    }
}
