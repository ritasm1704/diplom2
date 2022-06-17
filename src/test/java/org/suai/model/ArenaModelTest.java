package org.suai.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArenaModelTest {

    ArenaModel arenaModel = new ArenaModel("mapDemo.txt", 20, true);

    @Test
    void update() {
        arenaModel.addPlayer(0);
        arenaModel.addPlayer(1);

        InputComponent inputComponent = new InputComponent(0);
        inputComponent.rightPressed = true;
        inputComponent.downPressed = true;

        Player player1 = arenaModel.getPlayers().get(0);
        Player player2 = arenaModel.getPlayers().get(1);

        long delta = System.currentTimeMillis() - player1.lastTime;
        while (delta <= player1.timeout) {
            delta = System.currentTimeMillis() - player1.lastTime;
        }

        arenaModel.update(inputComponent);

        assertEquals(2, player1.getX());
        assertEquals(2, player1.getY());

        assertEquals(1, player2.getX());
        assertEquals(1, player2.getY());
    }
}