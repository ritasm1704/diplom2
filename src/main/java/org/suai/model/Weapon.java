package org.suai.model;

import java.io.Serializable;

public class Weapon implements Serializable {
    private int damage;
    private int radius;
    private int timeoutForAttack;
    private long lastTime;

    public Weapon(int damage, int radius, int timeoutForAttack) {
        this.damage = damage;
        this.radius = radius;
        this.lastTime = System.currentTimeMillis();
        this.timeoutForAttack = timeoutForAttack;
    }

    public int getDamage() {
        return damage;
    }

    public int getRadius() {
        return radius;
    }

    public void doAttack(Player player) {
        long delta = System.currentTimeMillis() - lastTime;
        if (delta > timeoutForAttack) {
            player.reduceHealth(damage);
            lastTime = System.currentTimeMillis();
        }
    }

    public void doAttack(Monster monster) {
        long delta = System.currentTimeMillis() - lastTime;
        if (delta > timeoutForAttack) {
            monster.reduceHealth(damage);
            lastTime = System.currentTimeMillis();
        }
    }
}
