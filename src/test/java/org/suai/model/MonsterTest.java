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
    }

    @Test
    void algorithm2() {
    }

    @Test
    void reduceHealth() {

    }
}