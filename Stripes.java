import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Stripes here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Stripes extends Actor
{
    /**
     * Act - do whatever the Stripes wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
        checkHitRight();
        checkHitLeft();
    }    

    Mario mario;
    public void checkHitLeft(){

        mario = (Mario) getOneObjectAtOffset (-getImage().getWidth()/2, 0, Mario.class);

        if (mario!= null){

            getWorld().removeObject (mario);
        }

    }

    public void checkHitRight(){

        mario = (Mario) getOneObjectAtOffset (getImage().getWidth()/2, 0, Mario.class);

        if (mario!= null){

            getWorld().removeObject (mario);
        }

    }
}
