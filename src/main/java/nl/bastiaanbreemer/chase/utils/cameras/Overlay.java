package nl.bastiaanbreemer.chase.utils.cameras;

import greenfoot.Actor;
import greenfoot.GreenfootImage;
import greenfoot.World;
import nl.bastiaanbreemer.chase.actors.Chaser;
import nl.bastiaanbreemer.chase.utils.pickups.Pickup;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Overlay extends Actor {

    private Chaser parent;

    private int tick = 0;

    private GreenfootImage life = new GreenfootImage("overlay/hud_p1Alt.png");

    private GreenfootImage[] hearts = new GreenfootImage[]{
        new GreenfootImage("overlay/hud_heartEmpty.png"),
        new GreenfootImage("overlay/hud_heartHalf.png"),
        new GreenfootImage("overlay/hud_heartFull.png")
    };
    private GreenfootImage[] numbers = new GreenfootImage[10];

    public Overlay(World world, Chaser parent) {
        this.parent = parent;
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = new GreenfootImage("overlay/hud_" + i + ".png");
        }
//        bomb.scale(, hearts[0].getWidth());
        // Doing these here (should'nt really by OOP standard but i need the image and this saves allot of mess in ChaseWorld)
        world.addObject(this, world.getWidth() / 2, world.getHeight() / 2);
        // Setting to a empty image with the world dimensions
        this.setImage(new GreenfootImage(world.getWidth(), world.getHeight()));
    }

    /**
     * Draws a number of index (number) at the location, specified "x,y" is the top-left of the image
     *
     * @param g2d   Greenfoot overlay image
     * @param index Of the number, in this case 0-9.
     * @param x     column to draw at
     * @param y     row to draw at
     */
    private void drawNumber(@NotNull GreenfootImage g2d, int index, int x, int y) {
        g2d.drawImage(numbers[index], x, y);
    }

    /**
     * Draws a heart of index (0: empty, 1: half, 2: full) at the location, specified "x,y" is the top-left of the image
     *
     * @param g2d   Greenfoot overlay image
     * @param index Of the heart image in the hearts array
     * @param x     column to draw at
     * @param y     row to draw at
     */
    private void drawHeart(@NotNull GreenfootImage g2d, int index, int x, int y) {
        g2d.drawImage(hearts[index], x, y);
    }

    private void draw(@NotNull GreenfootImage g2d) {
        g2d.clear();

        drawHearts(g2d, parent.getHealth() * -1);

        drawScore(g2d, String.format("%05d", tick / 120));

        drawPickups(g2d, parent.pickups);

        drawLives(g2d, parent.getLives());
    }

    private void drawHearts(GreenfootImage g2d, float health) {
        int xOffset = 0;

        for (int i = 0; i < Chaser.HEALTH_MAX / 2; i++) {
            if (health <= -2) {
                health += 2;
                this.drawHeart(g2d, 2, 10 + xOffset, 10);
            } else if (health <= -1) {
                health += 1;
                this.drawHeart(g2d, 1, 10 + xOffset, 10);
            } else {
                this.drawHeart(g2d, 0, 10 + xOffset, 10);
            }
            xOffset += hearts[0].getWidth() + 5;
        }
    }

    private void drawScore(GreenfootImage g2d, String score) {
        for (int i = 0; i < score.length(); i++) {
            int num = Integer.parseInt(Character.toString(score.charAt(i)));
            int center = getWorld().getWidth() / 2;
            int width = numbers[0].getWidth();
            int totalWidth = (score.length() * width) + ((score.length() - 1) * 5);
            drawNumber(g2d, num, (center - totalWidth / 2) + (i * width + 5), 10);
        }
    }

    private void drawPickups(GreenfootImage g2d, ArrayList<Pickup> list) {
        int xOffset = 10;
        for (Pickup pickup : list) {
            GreenfootImage img = new GreenfootImage(pickup.getImage());
            int yOffset = g2d.getHeight() - 10 - img.getHeight();
            g2d.drawImage(img, xOffset, yOffset);
            xOffset += img.getWidth() + 5;
        }
    }

    private void drawLives(GreenfootImage g2d, int number) {
        int xOffset = g2d.getWidth() - 10;
        for (int i = 0; i < number; i++) {
            drawFromTopRight(g2d, life, xOffset, 10);
            xOffset -= 5 + life.getWidth();
        }
    }

    private void drawFromTopLeft(GreenfootImage g2d, GreenfootImage img, int x, int y) {
        g2d.drawImage(img, x, y);
    }

    private void drawFromTopRight(GreenfootImage g2d, GreenfootImage img, int x, int y) {
        g2d.drawImage(img, x - img.getWidth(), y);
    }

    private void drawFromBottomLeft(GreenfootImage g2d, GreenfootImage img, int x, int y) {
        g2d.drawImage(img, x, y - img.getHeight());
    }

    private void drawFromBottomRight(GreenfootImage g2d, GreenfootImage img, int x, int y) {
        g2d.drawImage(img, x - img.getWidth(), y - img.getHeight());
    }

    private void drawFromCenter(GreenfootImage g2d, GreenfootImage img, int x, int y) {
        g2d.drawImage(img, x - img.getWidth() / 2, y - img.getHeight() / 2);
    }

    @Override
    public void act() {
        tick++;
        this.draw(getImage());
    }
}
