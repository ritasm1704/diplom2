package org.suai.model;

import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Player extends GameObject implements Serializable {

    int number;
    public boolean isDead = false;
    private boolean isRunning = false;
    private int health;
    private int maxHealth;
    private int speed;
    private int minSpeed;
    private int maxSpeed;
    private Weapon weapon;

    int timeout = 100;
    long lastTime;
    int numberOfMonster = -1;


    public Player(int x, int y, int width, int height, int maxHealth, int minSpeed, int maxSpeed, int number) {

        super(x, y, width, height);
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.minSpeed = minSpeed;
        this.speed = minSpeed;
        this.maxSpeed = maxSpeed;
        this.weapon = new Weapon(10, 2, 0);
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
        if (health == 0) {
            isDead = true;
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

    public void searchForMonster(ArrayList<Monster> monsters) {
        if (numberOfMonster != -1) {
            for (int i = 0; i < monsters.size(); i++) {
                Monster monster = monsters.get(i);
                if (i == numberOfMonster) {
                    if (monster.isDead) {
                        numberOfMonster = -1;
                    }
                    else {
                        if ((getX() + weapon.getRadius() > monster.getX() && getX() - weapon.getRadius() < monster.getX()) &&
                                (getY() + weapon.getRadius() > monster.getY() && getY() - weapon.getRadius() < monster.getY())) {
                            numberOfMonster = i;
                        } else {
                            numberOfMonster = -1;
                        }
                    }
                }
            }
        }
        //поиск нового игрока
        if (numberOfMonster == -1) {
            int tmpNearest = Math.abs(getX() - monsters.get(0).getX()) + Math.abs(getY() - monsters.get(0).getY());
            int tmp = 0;
            for (int i = 0; i < monsters.size(); i++) {
                Monster monster = monsters.get(i);
                if (!monster.isDead) {
                    if ((getX() + weapon.getRadius() > monster.getX() && getX() - weapon.getRadius() < monster.getX()) &&
                            (getY() + weapon.getRadius() > monster.getY() && getY() - weapon.getRadius() < monster.getY())) {
                        tmp = Math.abs(getX() - monster.getX()) + Math.abs(getY() - monster.getY());
                        if (tmp <= tmpNearest) {
                            numberOfMonster = i;
                            tmpNearest = tmp;
                        }
                    }
                }
            }
        }
    }

    public void update(InputComponent inputComponent, int[][] arena, ArrayList<Monster> monsters) {

        if (inputComponent.space) {
            searchForMonster(monsters);
            if (numberOfMonster != -1) {
                weapon.doAttack(monsters.get(numberOfMonster));
            }
            inputComponent.space = false;
        }

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
