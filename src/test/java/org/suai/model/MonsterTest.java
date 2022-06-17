package org.suai.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MonsterTest {

    ArenaModel arenaModel = new ArenaModel("mapDemo.txt", 20, true);
    Monster monster = arenaModel.getMonsters().get(0);

    @Test
    void update() {

        arenaModel.addPlayer(0);
        monster.update(arenaModel.getArenaAsMas(), arenaModel.getPlayers());
        assertEquals(-1, monster.numberOfPlayer);

        monster.setX(3);
        monster.setY(3);

        monster.update(arenaModel.getArenaAsMas(), arenaModel.getPlayers());
        assertEquals(0, monster.numberOfPlayer);

        long delta = System.currentTimeMillis() - monster.lastTime;
        while (delta <= monster.timeout) {
            delta = System.currentTimeMillis() - monster.lastTime;
        }

        monster.update(arenaModel.getArenaAsMas(), arenaModel.getPlayers());
        assertEquals(2, monster.getX());
        assertEquals(2, monster.getY());
    }
}