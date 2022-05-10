package org.suai.model;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ArenaModel {

    private int widthOfArena;
    private int heightOfArena;
    private int[][] arenaAsMas;

    ArrayList<Monster> monsters = new ArrayList<>();
    ArrayList<Player> players = new ArrayList<>();

    public ArenaModel(String nameOfMap, int numberOfMonsters) {

        /*arenaAsMas = new int[heightOfArena][widthOfArena];
        for (int i = 0; i < heightOfArena; i++) {
            for (int j = 0; j < widthOfArena; j++) {
                if (i == 0 || i == heightOfArena - 1 || j == 0 || j == widthOfArena - 1) {
                    arenaAsMas[i][j] = -1;
                }
                else {
                    arenaAsMas[i][j] = 0;
                }
            }
        }*/
        readArena(nameOfMap);
        players.add(new Player(1,1, 10, 10, 100, 1, 1, 1));
        for (int i = 0; i < numberOfMonsters; i++) {
            monsters.add(new Monster(widthOfArena - 2,heightOfArena - 2, 10,10,100, 1, 1000, new Weapon(1,1)));
        }

    }

    public void update(InputComponent inputComponent) {
        //System.out.println("update arena");
        for (int i = 0; i < monsters.size(); i++) {
            monsters.get(i).update(arenaAsMas, players);
        }
        for (int i = 0; i < players.size(); i++) {
            players.get(i).update(inputComponent, arenaAsMas);
        }
    }

    public ArrayList<Player> getPlayers() {
        return players;
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
