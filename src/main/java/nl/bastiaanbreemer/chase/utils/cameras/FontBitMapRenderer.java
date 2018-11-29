package nl.bastiaanbreemer.chase.utils.cameras;

import greenfoot.GreenfootImage;

public class FontBitMapRenderer {

    public final int letterWidth;
    public final int letterHeight;
    private GreenfootImage[] fontbitmap = new GreenfootImage[96];

    public FontBitMapRenderer() {
        for (int i = 0; i < fontbitmap.length; i++) {
            fontbitmap[i] = new GreenfootImage("overlay/font/fontbitmap_" + String.format("%02d", i + 1) + ".png");
        }
        letterWidth = fontbitmap[0].getWidth();
        letterHeight = fontbitmap[0].getHeight();
    }

    public void drawTextMiddle(GreenfootImage g2d, String text, int y, int width) {
        int totalWidth = (text.length() * this.letterWidth);
        this.drawText(g2d, text, (width / 2 - totalWidth / 2), y);
    }

    public void drawText(GreenfootImage g2d, String text, int x, int y) {
        if (fontbitmap == null)
            return;
        int px = 0;
        int py = 0;
        int offset = 32;
        for (int i = 0; i < text.length(); i++) {
            int c = text.charAt(i);
            if (c == (int) '\n') {
                py += letterHeight + 2;
                px = 0;
                continue;
            } else if (c == (int) '\r') {
                continue;
            }
            if (c - offset >= 0 && c - offset <= fontbitmap.length) {
                GreenfootImage letter = fontbitmap[c - offset];
                g2d.drawImage(letter, (px + x), (py + y + 1));
                px += letterWidth;
            }
        }
    }

}
