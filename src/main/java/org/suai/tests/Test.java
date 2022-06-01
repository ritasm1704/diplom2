package org.suai.tests;

import org.suai.model.ArenaModel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Test {

    int startNumberOfMonsters = 0;
    int numberOfMonsters = 0;
    int delta = 0;
    int numberOfTests = 0;

    public Test(String fileName) {
        File doc = new File(fileName);
        try(Scanner scanner = new Scanner(doc))
        {
            while (scanner.hasNextLine()) {
                String[] str = scanner.nextLine().split(" ");
                startNumberOfMonsters = Integer.parseInt(str[0]);
                numberOfMonsters = Integer.parseInt(str[1]);
                delta = Integer.parseInt(str[2]);
                numberOfTests = Integer.parseInt(str[3]);
                break;
            }
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }

        System.out.println("start Number Of Monsters: " + startNumberOfMonsters);
        System.out.println("number Of Monsters: " + numberOfMonsters);
        System.out.println("delta: " + delta);
        System.out.println("number Of Tests: " + numberOfTests);

        try(FileWriter writer = new FileWriter("monsters.txt", false))
        {
            for (int monsters = startNumberOfMonsters; monsters <= numberOfMonsters; monsters += delta) {
                writer.write(monsters + "\n");
            }
            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    public void test(String nameOfMap, String nameOfTest, boolean log2) {
        try(FileWriter writer = new FileWriter(nameOfTest, false))
        {
            for (int monsters = startNumberOfMonsters; monsters <= numberOfMonsters; monsters += delta) {
                int number = 10000;
                long sum = 0;

                for (int i = 0; i < number; i++) {
                    boolean isOver = false;
                    ArenaModel arenaModel = new ArenaModel(nameOfMap, monsters, log2);

                    int count = 0;
                    while (!isOver) {

                        count++;
                        long lastTime = System.currentTimeMillis();
                        arenaModel.update(null);
                        long deltaT = System.currentTimeMillis() - lastTime;
                        sum += deltaT;
                        if (count == numberOfTests) {
                            isOver = true;
                        }
                    }
                }
                //System.out.println(deltaT / numberOfTests);
                writer.write(sum/number + "\n");
            }
            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void main(String[] args) {

        Test test = new Test("forTest.txt");
        System.out.println("test1");
        test.test("map1.txt", "testMap1.txt", false);
        System.out.println("test2");
        test.test("map2.txt", "testMap2.txt", false);
        /*System.out.println("test3");
        test.test("map3.txt", "testMap3.txt", false);
        System.out.println("test4");
        test.test("map4.txt", "testMap4.txt", false);*/

        System.out.println("test5");
        test.test("map1.txt", "testMap5.txt", true);
        System.out.println("test6");
        test.test("map2.txt", "testMap6.txt", true);
        /*System.out.println("test7");
        test.test("map3.txt", "testMap7.txt", true);
        System.out.println("test8");
        test.test("map4.txt", "testMap8.txt", true);*/

        /*System.out.println("test9");
        test.test("map5.txt", "testMap9.txt", false);
        System.out.println("test10");
        test.test("map6.txt", "testMap10.txt", false);
        System.out.println("test11");
        test.test("map5.txt", "testMap11.txt", true);
        System.out.println("test12");
        test.test("map6.txt", "testMap12.txt", true);*/
    }
}
