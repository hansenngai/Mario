// Imports
import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

/**
 * Write a description of class level1 here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Level2 extends DanielWorld
{
    Mario mario = new Mario();
    /**
     * Constructor for objects of class level1.
     * 
     */
    public Level2()
    {    
        super(2); 
        fileName = "level2.txt";
        loadWorld(fileName);
        addObject(new Inventory(mario),145,-23);
        addObject (mario, 250,200);
    }

    

    /**
     * Adds the basis blocks in the world.
     * The user has to specify the number of the block and its coordinates on the screen
     * @param number - the number of the block
     * @param x - the x-coordinate
     * @param y - the y-coordinate
     */
    public void addBlock(int number, int x, int y){

        switch (number){ // flip through all the possible answers
            case 1: // if it is the first block, then
            addObject (new level2Block (new GreenfootImage ("level1Block1.png")),x,y); // add the block (with its specific image, x and y coordinate) on the screen
            return;
            case 2:
            addObject (new level2Block (new GreenfootImage ("level1Block2.png")),x,y);
            return;
            case 3:
            addObject (new level2Block (new GreenfootImage ("level1Block3.png")),x,y);
            return;
            case 4:
            addObject (new level2Block (new GreenfootImage ("level1Block4.png")),x,y);
            return;
            case 5:
            addObject (new level2Block (new GreenfootImage ("level1Block5.png")),x,y);
            return;
            case 6:
            addObject (new level2Block (new GreenfootImage ("level1Block6.png")),x,y);
            return;
            case 7:
            addObject (new level2Block (new GreenfootImage ("level1Block7.png")),x,y);
            return;
            case 8:
            addObject (new level2Block (new GreenfootImage ("level1Block8.png")),x,y);
            return;
            case 9:
            addObject (new level2Block (new GreenfootImage ("level1Block9.png")),x,y);
            return;
            case 10:
            addObject (new level2Block (new GreenfootImage ("level1Block10.png")),x,y);
            return;
            case 11:
            addObject (new level2Block (new GreenfootImage ("level1Block11.png")),x,y);
            return;
            case 12:
            addObject (new level2Block (new GreenfootImage ("level1Block12.png")),x,y);
            return;
            case 13:
            addObject (new level2Block (new GreenfootImage ("level1Block13.png")),x,y);
            return;
            case 14:
            addObject (new level2Block (new GreenfootImage ("level1Block14.png")),x,y);
            return;
            case 15:
            addObject (new level2Block (new GreenfootImage ("level1Block15.png")),x,y);
            return;
            case 16:
            addObject (new level2Block (new GreenfootImage ("level1Block16.png")),x,y);
            return;
            case 17:
            addObject (new level2Block (new GreenfootImage ("level1Block17.png")),x,y);
            return;
        }
    }

}
