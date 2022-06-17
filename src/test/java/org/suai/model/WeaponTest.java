package org.suai.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WeaponTest {

    Weapon weapon = new Weapon(10, 2, 0);

    @Test
    void getDamage() {
        assertEquals(10, weapon.getDamage());
    }

    @Test
    void getRadius() {
        assertEquals(2, weapon.getRadius());
    }

    @Test
    void doAttack() {
        Player player = new Player(1, 1, 10, 10, 100, 1, 1, 0);
        weapon.doAttack(player);
        assertEquals(90, player.getHealth());
    }

    @Test
    void testDoAttack() {
        Monster monster = new Monster(2, 2, 10,10,100, 1, 5,
                new Weapon(10,2, 2000), true);
        weapon.doAttack(monster);
        assertEquals(90, monster.getHealth());
    }
}