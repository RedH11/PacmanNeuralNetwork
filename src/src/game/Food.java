package game;

import java.awt.*;

public class Food {
    public double SIZE = 5;
    public double POWER_SIZE = 15;
    public int SCORE = 100;
    public int x;
    public int y;
    public boolean isActive;
    public double timer = 30000;
    public double width;
    public double length;

    java.awt.Color pelletColor = java.awt.Color.yellow;

    /**
     * Use graphics context fillOval
     * @param x x position
     * @param y y position
     */
    public void Pellet(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = SIZE;
        this.length = SIZE;
    }
    public void PowerPellet(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = POWER_SIZE;
        this.length = POWER_SIZE;
    }

    public Color getColor() {
        return pelletColor;
    }

}

