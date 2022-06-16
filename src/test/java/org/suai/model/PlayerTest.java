package org.suai.model;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    Player player = new Player(1, 1, 10, 10, 100, 1, 1, 0);
    ArenaModel arenaModel = new ArenaModel("mapDemo.txt", 20, true);

    @org.junit.jupiter.api.Test
    void reduceHealth() {

        player.reduceHealth(100);
        assertTrue(player.isDead);
    }

    @org.junit.jupiter.api.Test
    void enhanceHealth() {
        player.reduceHealth(20);
        player.enhanceHealth(10);
        assertEquals(90, player.getHealth());
    }

    @org.junit.jupiter.api.Test
    void getHealth() {
        assertEquals(100, player.getHealth());
    }

    @org.junit.jupiter.api.Test
    void moveX() {

        int[][] arena = arenaModel.getArenaAsMas();

        player.moveX(-1, arena);
        assertEquals(1, player.getX());
        player.moveX(1, arena);
        assertEquals(2, player.getX());
    }

    @org.junit.jupiter.api.Test
    void moveY() {

        int[][] arena = arenaModel.getArenaAsMas();

        player.moveY(-1, arena);
        assertEquals(1, player.getY());
        player.moveY(1, arena);
        assertEquals(2, player.getY());
    }

    @org.junit.jupiter.api.Test
    void searchForMonster() {
        player.searchForMonster(arenaModel.getMonsters());
        assertEquals(-1, player.numberOfMonster);
    }

    @org.junit.jupiter.api.Test
    void update() {

        InputComponent inputComponent = new InputComponent(0);
        inputComponent.rightPressed = true;
        inputComponent.downPressed = true;
        assertEquals(1, player.getX());
        assertEquals(1, player.getY());

        long delta = System.currentTimeMillis() - player.lastTime;
        while (delta <= player.timeout) {
            delta = System.currentTimeMillis() - player.lastTime;
        }
        player.update(inputComponent, arenaModel.getArenaAsMas(), arenaModel.getMonsters(), arenaModel.getFlowers());

        assertEquals(2, player.getX());
        assertEquals(2, player.getY());
    }
}