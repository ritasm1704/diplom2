package org.suai.model;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Player extends GameObject {

    int number;
    boolean isDead = false;
    private boolean isRunning = false;
    private int health;
    private int maxHealth;
    private int speed;
    private int minSpeed;
    private int maxSpeed;
    private Weapon weapon;

    int timeout = 100;
    long lastTime;


    public Player(int x, int y, int width, int height, int maxHealth, int minSpeed, int maxSpeed, int number) {

        super(x, y, width, height);
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.minSpeed = minSpeed;
        this.speed = minSpeed;
        this.maxSpeed = maxSpeed;
        this.weapon = new Weapon(10, 1);
        this.number = number;
        lastTime = System.currentTimeMillis();
    }

    public void reduceHealth(int a) {
        if (health - a < 0) {
            health = 0;
        }
        else {
            health -= a;
        }
    }

    public void enhanceHealth(int a){
        if (health + a > maxHealth) {
            health = maxHealth;
        }
        else {
            health += a;
        }
    }

    public void enhanceMaxHealth(int a){
        maxHealth += a;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void reduceSpeed(int a){
        if (speed - a < minSpeed) {
            speed = minSpeed;
        }
        else {
            speed -= a;
        }
    }

    public void enhanceSpeed(int a){
        if (speed + a > maxSpeed) {
            speed = maxSpeed;
        }
        else {
            speed += a;
        }
    }

    public int getSpeed() {
        return speed;
    }

    public void moveX(int a, int[][] arena) {
        if (getX() + a >= 0 && getX() + a < arena[0].length && arena[getY()][getX() + a] != -1 ) {
            setX(getX() + a * speed);
        }
    }

    public void moveY(int a, int[][] arena) {
        if (getY() + a >= 0 && getY() + a < arena.length && arena[getY() + a][getX()] != -1 ) {
            setY(getY() + a * speed);
        }
    }

    public void itIsRunning(boolean a) {
        isRunning = a;
    }

    public void update(InputComponent inputComponent, int[][] arena) {

        long delta = System.currentTimeMillis() - lastTime;
        if (delta > timeout) {
            if (inputComponent.rightPressed) {
                moveX(1, arena);
            }
            if (inputComponent.leftPressed) {
                moveX(-1, arena);
            }
            if (inputComponent.upPressed) {
                moveY(-1, arena);
            }
            if (inputComponent.downPressed) {
                moveY(1, arena);
            }
            lastTime = System.currentTimeMillis();
        }
    }

}
