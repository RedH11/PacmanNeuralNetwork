package game;

import java.awt.*;

public class Food {
    public int SIZE = 5;
    public int POWER_SIZE = 15;
    public int SCORE = 100;
    public int x;
    public int y;
    public boolean isActive;
    public int timer = 30000;

    java.awt.Color pelletColor = java.awt.Color.yellow;

    public void Pellets(int x, int y, int SIZE) {
        this.x = x;
        this.y = y;
        this.SIZE = SIZE;
    }
    public void PowerPellets(int x, int y, int SIZE) {
        this.x = x;
        this.y = y;
        this.SIZE = SIZE;
    }

    public Color getColor() {
        return pelletColor;
    }

}

