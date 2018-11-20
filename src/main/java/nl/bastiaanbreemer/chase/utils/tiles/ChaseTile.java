package nl.bastiaanbreemer.chase.utils.tiles;

public class ChaseTile extends Tile {
    public String type = "";
    public float damagePerTick = 0.0f;

    public ChaseTile(String image, int width, int height) {
        super(image, width, height);
    }
}
