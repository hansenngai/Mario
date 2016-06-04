import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Transporter here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Transporter extends Tube
{
    public Transporter()
    {
        getImage().scale(getImage().getWidth(), getImage().getHeight() + 10);
    }

    /**
     * Act - do whatever the Transporter wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        nextLevel();
    }    

    private void nextLevel()
    {
        if (getOneIntersectingObject(Mario.class) != null)
        {
            Mario mario = (Mario)getOneIntersectingObject(Mario.class);
            if (Greenfoot.isKeyDown("down") && mario.getY() < getY())
            {
                mario.setLocation(getX(),mario.getY());
                for (; mario.getY() < getY() + 10;)
                    mario.setLocation(getX(),mario.getY() + 1);
                DanielWorld dw = (DanielWorld)getWorld();
                DanielWorld nextlvl = new DanielWorld(dw.getLevel()+1);
                Greenfoot.setWorld(nextlvl);    
            }
        }

    }
}
