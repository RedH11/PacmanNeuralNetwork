package game;

import java.awt.*;

public class Food {
    private double SIZE = 5;
    private double POWER_SIZE = 15;
    public int SCORE = 100;
    private int x;
    private int y;
    public boolean isActive;
    public double timer = 30000;
    private double width;
    private double length;

    java.awt.Color pelletColor = java.awt.Color.yellow;

    /**
     * Use graphics context fillOval
     * @param x x position
     * @param y y position
     */
    public void pellet(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = SIZE;
        this.length = SIZE;
    }
    public void powerPellet(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = POWER_SIZE;
        this.length = POWER_SIZE;
    }

    public Color getColor() {
        return pelletColor;
    }

}

