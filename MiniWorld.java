import greenfoot.*;

/**
 * Write a description of class MiniWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MiniWorld extends Tube
{
    public MiniWorld()
    {
        getImage().scale(getImage().getWidth(), getImage().getHeight() + 10);
    }

    /**
     * Act - do whatever the Transporter wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        nextMiniLevel();
    }    

    private void nextMiniLevel()
    {
        if (isTouching(Mario.class) && getOneIntersectingObject(Mario.class).getY() < getY() - getImage().getHeight()/2)
        {
            Mario mario = (Mario)getOneIntersectingObject(Mario.class);
            if (Greenfoot.isKeyDown("down"))
            {
                mario.setLocation(getX(),mario.getY());
                for (; mario.getY() < getY() + 10;)
                    mario.setLocation(getX(),mario.getY() + 1);
                DanielWorld dw = (DanielWorld)getWorld();
                dw.stopMusic();
                System.out.println("got here");
                MiniLevel ml = new MiniLevel(dw.getLevel());
                Greenfoot.setWorld(ml);    
            }
        }

    }


}
