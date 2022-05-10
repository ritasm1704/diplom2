package org.suai.model;

public class Weapon {
    private int damage;
    private int radius;

    public Weapon(int damage, int radius) {
        this.damage = damage;
        this.radius = radius;
    }

    public int getDamage() {
        return damage;
    }

    public int getRadius() {
        return radius;
    }
}
