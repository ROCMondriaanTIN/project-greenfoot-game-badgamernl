package nl.bastiaanbreemer.chase.utils.cameras;

import greenfoot.Actor;
import greenfoot.GreenfootImage;
import greenfoot.World;
import nl.bastiaanbreemer.chase.ChaseApp;
import nl.bastiaanbreemer.chase.actors.Bomb;
import nl.bastiaanbreemer.chase.actors.Chaser;
import nl.bastiaanbreemer.chase.utils.pickups.Pickup;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Overlay extends Actor {

    public final int WIDTH;
    public final int HEIGHT;

    private Chaser parent;

    private int tick = 0;
    private float last_health = Chaser.HEALTH_MAX;
    private GreenfootImage life = new GreenfootImage("overlay/hud_p1Alt.png");

    private GreenfootImage[] hearts = new GreenfootImage[]{
        new GreenfootImage("overlay/hud_heartEmpty.png"),
        new GreenfootImage("overlay/hud_heartHalf.png"),
        new GreenfootImage("overlay/hud_heartFull.png")
    };

    private FontBitMapRenderer fontBitMapRenderer = new FontBitMapRenderer();

    public Overlay(World world, Chaser parent) {
        this.parent = parent;
//        bomb.scale(, hearts[0].getWidth());
        // Doing these here (should'nt really by OOP standard but i need the image and this saves allot of mess in ChaseWorld)
        world.addObject(this, world.getWidth() / 2, world.getHeight() / 2);
        // Setting to a empty image with the world dimensions
        WIDTH = world.getWidth();
        HEIGHT = world.getHeight();
        this.setImage(new GreenfootImage(WIDTH, HEIGHT));
    }


    private void draw(@NotNull GreenfootImage g2d) {
        g2d.clear();
        // Drawing this first so other elements get drawn over this

        if (ChaseApp.application.state == ChaseApp.State.PLAYING && parent != null) {
            drawHurt(g2d);

            drawHearts(g2d, (int) parent.getHealth() * -1);

            drawScore(g2d, String.format("%05d", tick / 120));

            drawPickups(g2d, parent.pickups);

            drawLives(g2d, ChaseApp.application.getLives());
//            Debug to test font bit map rendering
//            fontBitMapRenderer.drawText(g2d, " !@#$%^&*()-_?><{}[]\n|\\//:;\"\'\nABCDEFGHIJKLM\nNOPQRSTUVWXYZ\nabcdefghijklm\nnopqrstuvwxyz", 10, 100);

            drawBombDamageRadius(g2d);
        } else if (ChaseApp.application.state == ChaseApp.State.LOADING) {
            fontBitMapRenderer.drawTextMiddle(g2d, "loading", HEIGHT / 2 - fontBitMapRenderer.letterHeight / 2, WIDTH);
            fontBitMapRenderer.drawTextMiddle(g2d, "...", HEIGHT / 2 + fontBitMapRenderer.letterHeight / 2, WIDTH);
        } else if (ChaseApp.application.state == ChaseApp.State.WON) {
            fontBitMapRenderer.drawTextMiddle(g2d, "GAME WON", HEIGHT / 2 - fontBitMapRenderer.letterHeight * 2, WIDTH);
            fontBitMapRenderer.drawTextMiddle(g2d, "enter to restart", HEIGHT / 2 + fontBitMapRenderer.letterHeight * 2, WIDTH);
        }
    }

    public void drawHurt(GreenfootImage g2d) {
        if (parent.getHealth() != last_health) {
            last_health = parent.getHealth();
            g2d.setColor(new greenfoot.Color(255, 0, 0, 50));
            g2d.fillRect(0, 0, WIDTH, HEIGHT);
        }
    }


    public void drawBombDamageRadius(GreenfootImage g2d) {
        for (Bomb bomb : Bomb.getBombs()) {
            if (Bomb.TIMEOUT - 1 > bomb.time)
                continue;
            g2d.setColor(new greenfoot.Color(255, 0, 0, 50));
            g2d.fillOval(bomb.screenX - bomb.radius, bomb.screenY - bomb.radius, bomb.radius * 2, bomb.radius * 2);
        }
    }

    public void drawHearts(GreenfootImage g2d, int health) {
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

    public void drawScore(GreenfootImage g2d, @NotNull String score) {
        fontBitMapRenderer.drawTextMiddle(g2d, score, 10, WIDTH);
    }

    public void drawPickups(GreenfootImage g2d, @NotNull ArrayList<Pickup> list) {
        int xOffset = 10;
        for (Pickup pickup : list) {
            GreenfootImage img = new GreenfootImage(pickup.getImage());
            int yOffset = g2d.getHeight() - 10 - img.getHeight();
            g2d.drawImage(img, xOffset, yOffset);
            xOffset += img.getWidth() + 5;
        }
    }

    public void drawLives(@NotNull GreenfootImage g2d, int number) {
        int xOffset = g2d.getWidth() - 10;
        for (int i = 0; i < number; i++) {
            drawFromTopRight(g2d, life, xOffset, 10);
            xOffset -= 5 + life.getWidth();
        }
    }

    /**
     * Draws a heart of index (0: empty, 1: half, 2: full) at the location, specified "x,y" is the top-left of the image
     *
     * @param g2d   Greenfoot overlay image
     * @param index Of the heart image in the hearts array
     * @param x     column to draw at
     * @param y     row to draw at
     */
    public void drawHeart(@NotNull GreenfootImage g2d, int index, int x, int y) {
        g2d.drawImage(hearts[index], x, y);
    }

    /**
     * Draw a image with the x,y being in the top left of that image
     *
     * @param g2d Greenfoot overlay image
     * @param img Greenfoot img to draw
     * @param x   column to draw at
     * @param y   row to draw at
     */
    public void drawFromTopLeft(GreenfootImage g2d, GreenfootImage img, int x, int y) {
        g2d.drawImage(img, x, y);
    }

    /**
     * Draw a image with the x,y being in the top right of that image
     *
     * @param g2d Greenfoot overlay image
     * @param img Greenfoot img to draw
     * @param x   column to draw at
     * @param y   row to draw at
     */
    public void drawFromTopRight(GreenfootImage g2d, GreenfootImage img, int x, int y) {
        g2d.drawImage(img, x - img.getWidth(), y);
    }

    /**
     * Draw a image with the x,y being in the bottom left of that image
     *
     * @param g2d Greenfoot overlay image
     * @param img Greenfoot img to draw
     * @param x   column to draw at
     * @param y   row to draw at
     */
    public void drawFromBottomLeft(GreenfootImage g2d, GreenfootImage img, int x, int y) {
        g2d.drawImage(img, x, y - img.getHeight());
    }

    /**
     * Draw a image with the x,y being in the bottom right of that image
     *
     * @param g2d Greenfoot overlay image
     * @param img Greenfoot img to draw
     * @param x   column to draw at
     * @param y   row to draw at
     */
    public void drawFromBottomRight(GreenfootImage g2d, GreenfootImage img, int x, int y) {
        g2d.drawImage(img, x - img.getWidth(), y - img.getHeight());
    }

    /**
     * Draw a image with the x,y being in the center of that image
     *
     * @param g2d Greenfoot overlay image
     * @param img Greenfoot img to draw
     * @param x   column to draw at
     * @param y   row to draw at
     */
    public void drawFromCenter(GreenfootImage g2d, GreenfootImage img, int x, int y) {
        g2d.drawImage(img, x - img.getWidth() / 2, y - img.getHeight() / 2);
    }

    @Override
    public void act() {
        tick++;
        this.draw(getImage());
    }
}
