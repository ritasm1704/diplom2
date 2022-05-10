package org.suai.tests;

import org.suai.model.ArenaModel;
import org.suai.view.Game;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Test {

    public static void main(String[] args) {

        int numberOfMonsters = 0;
        int delta = 0;

        File doc = new File("forTest.txt");
        try(Scanner scanner = new Scanner(doc))
        {
            while (scanner.hasNextLine()) {
                String[] str = scanner.nextLine().split(" ");
                numberOfMonsters = Integer.parseInt(str[0]);
                delta = Integer.parseInt(str[1]);
                break;
            }
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }

        try(FileWriter writer = new FileWriter("monsters.txt", false))
        {
            for (int monsters = 1; monsters <= numberOfMonsters; monsters += delta) {
                writer.write(monsters + "\n");
            }
            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }

        /*Game game = new Game("map1.txt", 1);
        game.loop();*/
        try(FileWriter writer = new FileWriter("testMap1.txt", false))
        {
            for (int monsters = 1; monsters <= numberOfMonsters; monsters += delta) {
                Game game = new Game("map1.txt", monsters);
                game.loop();
                writer.write(game.getSum() + "\n");
            }
            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }

        try(FileWriter writer = new FileWriter("testMap2.txt", false))
        {
            for (int monsters = 1; monsters <= numberOfMonsters; monsters += delta) {
                Game game = new Game("map2.txt", monsters);
                game.loop();
                writer.write(game.getSum() + "\n");
            }
            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }

        try(FileWriter writer = new FileWriter("testMap3.txt", false))
        {
            for (int monsters = 1; monsters <= numberOfMonsters; monsters += delta) {
                Game game = new Game("map3.txt", monsters);
                game.loop();
                writer.write(game.getSum() + "\n");
            }
            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }

        try(FileWriter writer = new FileWriter("testMap4.txt", false))
        {
            for (int monsters = 1; monsters <= numberOfMonsters; monsters += delta) {
                Game game = new Game("map4.txt", monsters);
                game.loop();
                writer.write(game.getSum() + "\n");
            }
            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }

    }
}
