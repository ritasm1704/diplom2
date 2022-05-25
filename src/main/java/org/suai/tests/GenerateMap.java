package org.suai.tests;

import java.io.FileWriter;
import java.io.IOException;

public class GenerateMap {

    public static void main(String[] args) {

        int widthOfArena = 50;
        int heightOfArena = 35;
        int[][] arenaAsMas = new int[heightOfArena][widthOfArena];

        for (int i = 0; i < heightOfArena; i++) {
            for (int j = 0; j < widthOfArena; j++) {
                if (i == 0 || i == heightOfArena - 1 || j == 0 || j == widthOfArena - 1) {
                    arenaAsMas[i][j] = -1;
                }
                else {
                    arenaAsMas[i][j] = 0;
                }
            }
        }

        int[][] arenaAsMas2 = new int[heightOfArena][widthOfArena];
        double p = 0.5;

        for (int i = 0; i < heightOfArena; i++) {
            for (int j = 0; j < widthOfArena; j++) {
                if (i == 0 || i == heightOfArena - 1 || j == 0 || j == widthOfArena - 1) {
                    arenaAsMas2[i][j] = -1;
                }
                else if (i == 1 && j == 1 || i == heightOfArena - 2 && j == widthOfArena - 2){
                    arenaAsMas2[i][j] = 0;
                } else {
                    if (Math.random() < p) {
                        arenaAsMas2[i][j] = -1;
                    } else {
                        arenaAsMas2[i][j] = 0;
                    }
                }
            }
        }

        /*try(FileWriter writer = new FileWriter("map1.txt", false))
        {
            writer.write(heightOfArena + " " + widthOfArena + "\n");

            String str;
            for (int i = 0; i < heightOfArena; i++) {
                str = "";
                for (int j = 0; j < widthOfArena; j++) {
                    str += arenaAsMas[i][j];
                    str += " ";
                }
                str += "\n";
                writer.write(str);
            }
            writer.flush();
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }*/

        /*try(FileWriter writer = new FileWriter("map2.txt", false))
        {
            writer.write(heightOfArena + " " + widthOfArena + "\n");

            String str;
            for (int i = 0; i < heightOfArena; i++) {
                str = "";
                for (int j = 0; j < widthOfArena; j++) {
                    str += arenaAsMas2[i][j];
                    str += " ";
                }
                str += "\n";
                writer.write(str);
            }
            writer.flush();
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }*/

        widthOfArena = 100;
        heightOfArena = 70;
        int[][] arenaAsMas3 = new int[heightOfArena][widthOfArena];

        for (int i = 0; i < heightOfArena; i++) {
            for (int j = 0; j < widthOfArena; j++) {
                if (i == 0 || i == heightOfArena - 1 || j == 0 || j == widthOfArena - 1) {
                    arenaAsMas3[i][j] = -1;
                }
                else {
                    arenaAsMas3[i][j] = 0;
                }
            }
        }

        int[][] arenaAsMas4 = new int[heightOfArena][widthOfArena];

        for (int i = 0; i < heightOfArena; i++) {
            for (int j = 0; j < widthOfArena; j++) {
                if (i == 0 || i == heightOfArena - 1 || j == 0 || j == widthOfArena - 1) {
                    arenaAsMas4[i][j] = -1;
                }
                else if (i == 1 && j == 1 || i == heightOfArena - 2 && j == widthOfArena - 2){
                    arenaAsMas4[i][j] = 0;
                } else {
                    if (Math.random() < p) {
                        arenaAsMas4[i][j] = -1;
                    } else {
                        arenaAsMas4[i][j] = 0;
                    }
                }
            }
        }

        /*try(FileWriter writer = new FileWriter("map5.txt", false))
        {
            writer.write(heightOfArena + " " + widthOfArena + "\n");

            String str;
            for (int i = 0; i < heightOfArena; i++) {
                str = "";
                for (int j = 0; j < widthOfArena; j++) {
                    str += arenaAsMas3[i][j];
                    str += " ";
                }
                str += "\n";
                writer.write(str);
            }
            writer.flush();
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }*/

        try(FileWriter writer = new FileWriter("map6.txt", false))
        {
            writer.write(heightOfArena + " " + widthOfArena + "\n");

            String str;
            for (int i = 0; i < heightOfArena; i++) {
                str = "";
                for (int j = 0; j < widthOfArena; j++) {
                    str += arenaAsMas4[i][j];
                    str += " ";
                }
                str += "\n";
                writer.write(str);
            }
            writer.flush();
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
    }
}
