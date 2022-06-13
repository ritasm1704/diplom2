package org.suai.model;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

public class ArenaModel implements Serializable {

    private int widthOfArena;
    private int heightOfArena;
    private int[][] arenaAsMas;

    private ArrayList<Monster> monsters = new ArrayList<>();
    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Flower> flowers = new ArrayList<>();

    public ArenaModel(String nameOfMap, int numberOfMonsters, boolean alg2) {

        readArena(nameOfMap);
        if (numberOfMonsters != 0) {
            for (int m = 0; m < numberOfMonsters; m++) {

                int randY = 0;
                int randX = 0;
                while (arenaAsMas[randY][randX] == -1 || randY < 10 && randX < 10) {
                    randY = 1 + (int) (Math.random() * heightOfArena - 2);
                    randX = 1 + (int) (Math.random() * widthOfArena - 2);
                }
                monsters.add(new Monster(randX, randY, 10,10,100, 1, 5,
                        new Weapon(10,2, 2000), alg2));
            }

            int numberOfFlowers = 50;

            for (int m = 0; m < numberOfFlowers; m++) {

                int randY = 0;
                int randX = 0;
                while (arenaAsMas[randY][randX] == -1) {
                    randY = 2 + (int) (Math.random() * heightOfArena - 3);
                    randX = 2 + (int) (Math.random() * widthOfArena - 3);
                }
                flowers.add(new Flower(randX, randY, 10,10, 10));
            }
        }
    }

    public void update(InputComponent inputComponent) {
        //System.out.println("update arena");
        for (int i = 0; i < monsters.size(); i++) {
            if (!monsters.get(i).isDead) {
                monsters.get(i).update(arenaAsMas, players);
            }
        }

        for (int i = 0; i < flowers.size(); i++) {
            if (flowers.get(i).isDead) {
                flowers.get(i).checkTime();
            }
        }

        for (int i = 0; i < players.size(); i++) {
            //System.out.println(players.get(i).getX() + " " + players.get(i).getY());
            //System.out.println(players.get(i).number + " " + inputComponent.numberOfPlayer);
            if (players.get(i).number == inputComponent.numberOfPlayer) {
                players.get(i).update(inputComponent, arenaAsMas, monsters, flowers);
                //System.out.println(inputComponent.rightPressed);
                //System.out.println( "--" + players.get(i).getX() + " " + players.get(i).getY());
                break;
            }
        }
    }

    public void addPlayer(int number) {
        players.add(new Player(1,1, 10, 10, 100, 1, 1, number));
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ArrayList<Flower> getFlowers() {
        return flowers;
    }

    public ArrayList<Monster> getMonsters() {
        return monsters;
    }

    public int getHeightOfArena() {
        return heightOfArena;
    }

    public int getWidthOfArena() {
        return widthOfArena;
    }

    public int[][] getArenaAsMas() {
        return arenaAsMas;
    }

    public void readArena(String nameOfMap) {
        File doc = new File(nameOfMap);
        try(Scanner scanner = new Scanner(doc))
        {
            int count = 0;
            int count_i = 0;
            int count_j = 0;
            String[] str;
            while (scanner.hasNextLine()) {
                str = scanner.nextLine().split(" ");
                if (count == 0) {
                    heightOfArena = Integer.parseInt(str[0]);
                    widthOfArena = Integer.parseInt(str[1]);
                    arenaAsMas = new int[heightOfArena][widthOfArena];
                } else {
                    for (int i = 0; i < str.length; i++) {
                        arenaAsMas[count_i][count_j] = Integer.parseInt(str[i]);
                        count_j ++;
                        if (count_j == widthOfArena) {
                            count_j = 0;
                            count_i++;
                        }
                    }
                }
                count++;
            }
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
    }

}
