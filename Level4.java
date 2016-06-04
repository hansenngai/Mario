import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Write a description of class level2 here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Level4 extends DanielWorld
{
    Mario mario = new Mario();
    /**
     * Creates the world starting from level 1.
     */
    public Level4()
    {
        super(4); 
        fileName = "level4.txt";
        loadWorld(fileName);
        addObject(new Inventory(mario),145,-23);
        addObject (mario, 250,200);
    }


    public void addBlock(int number, int x, int y){
        switch (number){
            case 1:
            addObject (new level2Block (new GreenfootImage ("block1.png")),x,y);
            return;
            case 2:
            addObject (new level2Block (new GreenfootImage ("block2.png")),x,y);
            return;
            case 3:
            addObject (new level2Block (new GreenfootImage ("block3.png")),x,y);
            return;
            case 4:
            addObject (new level2Block (new GreenfootImage ("block4.png")),x,y);
            return;
            case 5:
            addObject (new level2Block (new GreenfootImage ("block5.png")),x,y);
            return;
            case 6:
            addObject (new level2Block (new GreenfootImage ("block6.png")),x,y);
            return;
            case 7:
            addObject (new level2Block (new GreenfootImage ("block7.png")),x,y);
            return;
            case 8:
            addObject (new level2Block (new GreenfootImage ("block8.png")),x,y);
            return;
            case 9:
            addObject (new level2Block (new GreenfootImage ("block9.png")),x,y);
            return;
            case 10:
            addObject (new level2Block (new GreenfootImage ("block10.png")),x,y);
            return;
            case 11:
            addObject (new level2Block (new GreenfootImage ("block11.png")),x,y);
            return;
            case 12:
            addObject (new level2Block (new GreenfootImage ("block12.png")),x,y);
            return;
            case 13:
            addObject (new level2Block (new GreenfootImage ("block13.png")),x,y);
            return;
            case 14:
            addObject (new level2Block (new GreenfootImage ("block14.png")),x,y);
            return;
            case 15:
            addObject (new level2Block (new GreenfootImage ("block15.png")),x,y);
            return;
            case 16:
            addObject (new level2Block (new GreenfootImage ("block16.png")),x,y);
            return;
            case 17:
            addObject (new level2Block (new GreenfootImage ("block17.png")),x,y);
            return;
            case 18:
            addObject (new level2Block (new GreenfootImage ("block18.png")),x,y);
            return;
        }
    }


}
