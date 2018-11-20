package nl.bastiaanbreemer.chase.utils;

import greenfoot.Actor;
import greenfoot.GreenfootImage;
import greenfoot.World;
import nl.bastiaanbreemer.chase.actors.Chaser;
import org.jetbrains.annotations.NotNull;

public class Overlay extends Actor {

    private Chaser parent;

    private int tick = 0;

    private GreenfootImage[] hearts = new GreenfootImage[]{
        new GreenfootImage("overlay/hud_heartEmpty.png"),
        new GreenfootImage("overlay/hud_heartHalf.png"),
        new GreenfootImage("overlay/hud_heartFull.png")
    };
    private GreenfootImage[] numbers = new GreenfootImage[10];

    private GreenfootImage bomb = new GreenfootImage("items/bomb01.png");

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
     * Draws a bomb at the location, specified "x,y" is the top-left of the image
     *
     * @param g2d Greenfoot overlay image
     * @param x   column to draw at
     * @param y   row to draw at
     */
    private void drawBomb(@NotNull GreenfootImage g2d, int x, int y) {
        g2d.drawImage(bomb, x, y);
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
        float health = parent.getHealth() * -1;
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

        String str = String.format("%05d", tick / 120);

        for (int i = 0; i < str.length(); i++) {
            int num = Integer.parseInt(Character.toString(str.charAt(i)));
            int center = getWorld().getWidth() / 2;
            int width = numbers[0].getWidth();
            int totalWidth = (str.length() * width) + ((str.length() - 1) * 5);
            drawNumber(g2d, num, (center - totalWidth / 2) + (i * width + 5), 10);
        }

        int yOffset = g2d.getHeight() - 10 - bomb.getHeight();
        xOffset = 0;
        for (int i = 0; i < parent.getBombs(); i++) {
            drawBomb(g2d, 10 + xOffset, yOffset);
            xOffset += hearts[0].getWidth() + 5;
        }

    }

    @Override
    public void act() {
        tick++;
        this.draw(getImage());
    }
}
