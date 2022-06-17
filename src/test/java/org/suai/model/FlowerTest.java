package org.suai.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FlowerTest {

    Flower flower = new Flower(1, 1, 10, 10, 10);

    @Test
    void pick() {

        int h = flower.pick();
        assertEquals(10, h);
        assertTrue(flower.isDead);
    }

    @Test
    void checkTime() {

        flower.pick();
        flower.checkTime();
        assertTrue(flower.isDead);

        long delta = System.currentTimeMillis() - flower.lastTime;
        while (delta <= flower.timeout) {
            delta = System.currentTimeMillis() - flower.lastTime;
        }
        flower.checkTime();
        assertFalse(flower.isDead);
    }
}