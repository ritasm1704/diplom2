package org.suai.model;

import java.io.Serializable;
import java.util.*;

public class Monster extends GameObject implements Serializable {

    private boolean isRunning = false;
    public boolean isDead = false;
    private int health;
    private int maxHealth;
    private int speed;
    private Weapon weapon;
    private int radiusVisibility;
    int numberOfPlayer = -1;
    int[][] copyOfArena;
    //int[][] copyOfArena2;

    int timeout = 200;
    long lastTime;

    boolean alg2;

    public Monster(int x, int y, int width, int height, int health, int speed, int radiusVisibility, Weapon weapon, boolean alg2) {
        super(x, y, width, height);
        this.health = health;
        this.maxHealth = health;
        this.speed = speed;
        this.radiusVisibility = radiusVisibility;
        this.weapon = weapon;
        this.alg2 = alg2;
    }

    public void itIsRunning(boolean a) {
        isRunning = a;
    }

    public void update(int[][] arena, ArrayList<Player> players) {

        Player nearest = null;
        //поиск игрока, которого атаковал в прошлый раз
        if (numberOfPlayer != -1) {
            for (int i = 0; i < players.size(); i++) {
                Player player = players.get(i);
                if (player.number == numberOfPlayer) {
                    if (player.isDead) {
                        numberOfPlayer = -1;
                    }
                    else {
                        if ((getX() + radiusVisibility > player.getX() && getX() - radiusVisibility < player.getX()) &&
                                (getY() + radiusVisibility > player.getY() && getY() - radiusVisibility < player.getY())) {
                            nearest = player;
                        } else {
                            numberOfPlayer = -1;
                        }
                    }
                }
            }
        }
        //поиск нового игрока
        if (numberOfPlayer == -1) {
            int tmpNearest = Math.abs(getX() - players.get(0).getX()) + Math.abs(getY() - players.get(0).getY());
            int tmp = 0;
            for (int i = 0; i < players.size(); i++) {
                Player player = players.get(i);
                if (!player.isDead) {
                    if ((getX() + radiusVisibility > player.getX() && getX() - radiusVisibility < player.getX()) &&
                            (getY() + radiusVisibility > player.getY() && getY() - radiusVisibility < player.getY())) {
                        tmp = Math.abs(getX() - player.getX()) + Math.abs(getY() - player.getY());
                        if (tmp <= tmpNearest) {
                            nearest = player;
                            numberOfPlayer = player.number;
                            tmpNearest = tmp;
                        }
                    }
                }
            }
        }

        if (nearest != null) {
            if (getX() + weapon.getRadius() > nearest.getX() && getX() - weapon.getRadius() < nearest.getX() &&
                    getY() + weapon.getRadius() > nearest.getY() && getY() - weapon.getRadius() < nearest.getY()) {
                weapon.doAttack(nearest);
            } else {
                long delta = System.currentTimeMillis() - lastTime;
                if (delta > timeout) {
                    int xStart = getX();
                    int yStart = getY();
                    int xFinish = nearest.getX();
                    int yFinish = nearest.getY();

                    copyOfArena = new int[arena.length][arena[0].length];

                    for (int i = 0; i < arena.length; i++) {
                        for (int j = 0; j < arena[0].length; j++) {
                            if (arena[i][j] == -1) {
                                copyOfArena[i][j] = Integer.MAX_VALUE;
                            }
                            else {
                                copyOfArena[i][j] = 0;
                            }
                        }
                    }

                    /*copyOfArena2 = new int[arena.length][arena[0].length];

                    for (int i = 0; i < arena.length; i++) {
                        for (int j = 0; j < arena[0].length; j++) {
                            copyOfArena2[i][j] = 0;
                        }
                    }*/

                    if (!alg2) {
                        //поиск кратчайшего пути до игрока (волновой алгоритм (алгоритм Ли))
                        algorithm1(yStart, xStart, yFinish, xFinish);
                    } else {
                        //поиск кратчайшего пути до игрока (алгоритм A*)
                        algorithm2(yStart, xStart, yFinish, xFinish);
                    }
                    lastTime = System.currentTimeMillis();
                }
            }
        }
    }

    PriorityQueue<Node> queue2;

    public void algorithm2(int yStart, int xStart, int yFinish, int xFinish) {
        //поиск кратчайшего пути до игрока (алгоритм A*)
        queue2 = new PriorityQueue<>(Node::compare);
        queue2.add(new Node(xStart, yStart, 0, 1, null));

        copyOfArena[yStart][xStart] = 1;
        //copyOfArena2[yStart][xStart] = 1;
        while (true) {

            Node current = queue2.poll();

            if (current == null) {
                break;
            }
            int x = current.x;
            int y = current.y;
            if (x == xFinish && y == yFinish) {
                searchForCoordinates(current, yStart, xStart);
                break;
            }

            searchForNeighbors2(x + 1, y, current, yFinish, xFinish);
            searchForNeighbors2(x, y + 1, current, yFinish, xFinish);
            searchForNeighbors2(x + 1, y + 1, current, yFinish, xFinish);
            searchForNeighbors2(x - 1, y, current, yFinish, xFinish);
            searchForNeighbors2(x, y - 1, current, yFinish, xFinish);
            searchForNeighbors2(x - 1, y - 1, current, yFinish, xFinish);
            searchForNeighbors2(x + 1, y - 1, current, yFinish, xFinish);
            searchForNeighbors2(x - 1, y + 1, current, yFinish, xFinish);
        }
    }

    public void searchForNeighbors2(int x, int y, Node node, int yFinish, int xFinish) {
        if (y < copyOfArena.length && y >= 0) {
            if (x < copyOfArena[0].length && x >= 0) {
                if (copyOfArena[y][x] != Integer.MAX_VALUE) {
                    int newCost = node.costSoFar + 1;
                    int priority = newCost + Math.abs(x - xFinish) + Math.abs(y - yFinish);
                    if (newCost < copyOfArena[y][x] || copyOfArena[y][x] == 0) {
                        copyOfArena[y][x] = newCost;
                        queue2.add(new Node(x, y, priority, newCost, node));
                        //copyOfArena2[y][x] = 1;
                    }
                }
            }
        }
    }

    LinkedList<Node> queue;

    public void algorithm1(int yStart, int xStart, int yFinish, int xFinish) {
        //поиск кратчайшего пути до игрока (волновой алгоритм (алгоритм Ли))
        queue = new LinkedList<>();
        queue.add(new Node(xStart, yStart, 0, 1, null));

        copyOfArena[yStart][xStart] = 1;
        //copyOfArena2[yStart][xStart] = 1;
        while (true) {

            Node current = queue.poll();

            if (current == null) {
                break;
            }
            int x = current.x;
            int y = current.y;
            if (x == xFinish && y == yFinish) {
                searchForCoordinates(current, yStart, xStart);
                break;
            }

            searchForNeighbors(x + 1, y, current);
            searchForNeighbors(x, y + 1, current);
            searchForNeighbors(x + 1, y + 1, current);
            searchForNeighbors(x - 1, y, current);
            searchForNeighbors(x, y - 1, current);
            searchForNeighbors(x - 1, y - 1, current);
            searchForNeighbors(x + 1, y - 1, current);
            searchForNeighbors(x - 1, y + 1, current);
        }
    }

    public void searchForCoordinates(Node node, int yStart, int xStart) {



        for (int i = 0; i < copyOfArena.length; i++) {
            for (int j = 0; j < copyOfArena[0].length; j++) {
                if (copyOfArena[i][j] == 0) {
                    copyOfArena[i][j] = Integer.MAX_VALUE;
                }
            }
        }

        int[] masPrev = new int[]{node.x, node.y};
        int[] mas = new int[]{node.x, node.y};

        while (!(mas[0] == xStart && mas[1] == yStart)) {
            //System.out.println(mas[0] + " " + mas[1]);
            masPrev = mas;
            //copyOfArena2[mas[1]][mas[0]] = 2;
            mas = getNextStep(mas[0], mas[1]);
        }

        /*int count = 0;
        for (int i = 0; i < copyOfArena2.length; i++) {
            for (int j = 0; j < copyOfArena2[0].length; j++) {
                System.out.print(copyOfArena2[i][j]);
                count += copyOfArena2[i][j];
            }
            System.out.println();
        }
        System.out.println(count);*/

        setX(masPrev[0]);
        setY(masPrev[1]);
    }

    public void searchForNeighbors(int x, int y, Node node) {
        if (y < copyOfArena.length && y >= 0) {
            if (x < copyOfArena[0].length && x >= 0) {
                if (copyOfArena[y][x] != Integer.MAX_VALUE) {
                    int newCost = node.costSoFar + 1;
                    if (newCost < copyOfArena[y][x] || copyOfArena[y][x] == 0) {
                        copyOfArena[y][x] = newCost;
                        queue.add(new Node(x, y, 0, newCost, node));
                        //copyOfArena2[y][x] = 1;
                    }
                }
            }
        }
    }

    public int[] getNextStep(int x, int y) {
        //поиск кратчайшего пути по размеченной карте
        int minX = x;
        int minY = y;
        if (x + 1 < copyOfArena[0].length) {
            if (copyOfArena[y][x + 1] < copyOfArena[minY][minX]) {
                minY = y;
                minX = x + 1;
            }
        }
        if (x - 1 >= 0) {
            if (copyOfArena[y][x - 1] < copyOfArena[minY][minX]) {
                minY = y;
                minX = x - 1;
            }
        }
        if (y + 1 < copyOfArena.length) {
            if (copyOfArena[y + 1][x] < copyOfArena[minY][minX]) {
                minY = y + 1;
                minX = x;
            }
        }
        if (y - 1 >= 0) {
            if (copyOfArena[y - 1][x] < copyOfArena[minY][minX]) {
                minY = y - 1;
                minX = x;
            }
        }
        if (y + 1 < copyOfArena.length) {
            if (x + 1 < copyOfArena[0].length) {
                if (copyOfArena[y + 1][x + 1] < copyOfArena[minY][minX]) {
                    minY = y + 1;
                    minX = x + 1;
                }
            }
            if (x - 1 >= 0) {
                if (copyOfArena[y + 1][x - 1] < copyOfArena[minY][minX]) {
                    minY = y + 1;
                    minX = x - 1;
                }
            }
        }
        if (y - 1 >= 0) {
            if (x + 1 < copyOfArena[0].length) {
                if (copyOfArena[y - 1][x + 1] < copyOfArena[minY][minX]) {
                    minY = y - 1;
                    minX = x + 1;
                }
            }
            if (x - 1 >= 0) {
                if (copyOfArena[y - 1][x - 1] < copyOfArena[minY][minX]) {
                    minY = y - 1;
                    minX = x - 1;
                }
            }
        }
        if (minX == x && minY == y) {
            if (x + 1 < copyOfArena[0].length) {
                if (copyOfArena[y][x + 1] <= copyOfArena[minY][minX]) {
                    minY = y;
                    minX = x + 1;
                }
            }
            if (x - 1 >= 0) {
                if (copyOfArena[y][x - 1] <= copyOfArena[minY][minX]) {
                    minY = y;
                    minX = x - 1;
                }
            }
            if (y + 1 < copyOfArena.length) {
                if (copyOfArena[y + 1][x] <= copyOfArena[minY][minX]) {
                    minY = y + 1;
                    minX = x;
                }
            }
            if (y - 1 >= 0) {
                if (copyOfArena[y - 1][x] <= copyOfArena[minY][minX]) {
                    minY = y - 1;
                    minX = x;
                }
            }
            if (y + 1 < copyOfArena.length) {
                if (x + 1 < copyOfArena[0].length) {
                    if (copyOfArena[y + 1][x + 1] <= copyOfArena[minY][minX]) {
                        minY = y + 1;
                        minX = x + 1;
                    }
                }
                if (x - 1 >= 0) {
                    if (copyOfArena[y + 1][x - 1] <= copyOfArena[minY][minX]) {
                        minY = y + 1;
                        minX = x - 1;
                    }
                }
            }
            if (y - 1 >= 0) {
                if (x + 1 < copyOfArena[0].length) {
                    if (copyOfArena[y - 1][x + 1] <= copyOfArena[minY][minX]) {
                        minY = y - 1;
                        minX = x + 1;
                    }
                }
                if (x - 1 >= 0) {
                    if (copyOfArena[y - 1][x - 1] <= copyOfArena[minY][minX]) {
                        minY = y - 1;
                        minX = x - 1;
                    }
                }
            }
        }
        return new int[] {minX, minY};
    }

    public void reduceHealth(int a) {
        //System.out.println("monster: reduce = " + a);
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

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }
}
