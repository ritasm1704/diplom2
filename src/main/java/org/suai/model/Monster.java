package org.suai.model;

import java.util.ArrayList;
import java.util.List;

public class Monster extends GameObject{

    private boolean isRunning = false;
    private int health;
    private int speed;
    private Weapon weapon;
    private int radiusVisibility;
    int numberOfPlayer = -1;
    int[][] copyOfArena;

    int timeout = 200;
    //int timeout = 5;
    long lastTime;

    public Monster(int x, int y, int width, int height, int health, int speed, int radiusVisibility, Weapon weapon) {
        super(x, y, width, height);
        this.health = health;
        this.speed = speed;
        this.radiusVisibility = radiusVisibility;
        this.weapon = weapon;
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

        long delta = System.currentTimeMillis() - lastTime;

        if (delta > timeout) {
            //поиск кратчайшего пути до игрока (волновой алгоритм (алгоритм Ли))
            if (nearest != null) {
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

                int maxPath = copyOfArena.length * copyOfArena[0].length;
                boolean flag;
                copyOfArena[yStart][xStart] = 1;
                for (int k = 1; k < maxPath; k++) {
                    //System.out.println(k);
                    flag = true;
                    for (int i = 0; i < copyOfArena.length; i++) {
                        for (int j = 0; j < copyOfArena[0].length; j++) {
                            if (copyOfArena[i][j] == 0) {
                                flag = false;
                            }
                            if (copyOfArena[yFinish][xFinish] != 0) {
                                if (copyOfArena[i][j] == 0) {
                                    copyOfArena[i][j] = Integer.MAX_VALUE;
                                }
                            } else {
                                if (copyOfArena[i][j] == k) {
                                    algorithmLee(j, i, k + 1);
                                }
                            }

                        }
                    }
                    if (flag) {
                        break;
                    }
                }

                /*for (int i = 0; i < copyOfArena.length; i++) {
                    for (int j = 0; j < copyOfArena[0].length; j++) {
                        System.out.print(copyOfArena[i][j] + " ");
                    }
                    System.out.println();
                }*/

                int[] masPrev = new int[]{xFinish, yFinish};
                int[] mas = new int[]{xFinish, yFinish};

                while (!(mas[0] == xStart && mas[1] == yStart)) {
                    //System.out.println(mas[0] + " " + mas[1]);
                    masPrev = mas;
                    mas = getNextStep(mas[0], mas[1]);
                }
                setX(masPrev[0]);
                setY(masPrev[1]);
            }
            lastTime = System.currentTimeMillis();
        }
    }

    public void algorithmLee(int x, int y, int serialNumber) {

        //разметка карты
        if (y + 1 < copyOfArena.length) {
            if (copyOfArena[y + 1][x] == 0) {
                copyOfArena[y + 1][x] = serialNumber;
            }
            if (x + 1 < copyOfArena[0].length) {
                if (copyOfArena[y + 1][x + 1] == 0) {
                    copyOfArena[y + 1][x + 1] = serialNumber;
                }
            }
            if (x - 1 >= 0) {
                if (copyOfArena[y + 1][x - 1] == 0) {
                    copyOfArena[y + 1][x - 1] = serialNumber;
                }
            }
        }
        if (y - 1 >= 0) {
            if (copyOfArena[y - 1][x] == 0) {
                copyOfArena[y - 1][x] = serialNumber;
            }
            if (x + 1 < copyOfArena[0].length) {
                if (copyOfArena[y - 1][x + 1] == 0) {
                    copyOfArena[y - 1][x + 1] = serialNumber;
                }
            }
            if (x - 1 >= 0) {
                if (copyOfArena[y - 1][x - 1] == 0) {
                    copyOfArena[y - 1][x - 1] = serialNumber;
                }
            }
        }
        if (x + 1 < copyOfArena[0].length) {
            if (copyOfArena[y][x + 1] == 0) {
                copyOfArena[y][x + 1] = serialNumber;
            }
        }
        if (x - 1 >= 0) {
            if (copyOfArena[y][x - 1] == 0) {
                copyOfArena[y][x - 1] = serialNumber;
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
}
