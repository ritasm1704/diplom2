package org.suai.model;

import java.io.Serializable;

public class Flower extends GameObject implements Serializable {

    private int healing;
    public boolean isDead = false;
    int timeout = 100000;
    long lastTime;


    public Flower(int x, int y, int width, int height, int healing) {
        super(x, y, width, height);
        this.healing = healing;
    }

    public int pick() {
        isDead = true;
        lastTime = System.currentTimeMillis();
        return healing;
    }

    public void checkTime() {
        if (System.currentTimeMillis() - lastTime >= timeout) {
            isDead = false;
        }
    }
}
