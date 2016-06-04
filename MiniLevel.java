import greenfoot.*;

/**
 * Write a description of class UnderGround here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MiniLevel extends DanielWorld
{

    /**
     * Constructor for objects of class UnderGround.
     * 
     */
    public MiniLevel(int level)
    {
        super(680,460,"MiniLevel" + Integer.toString(level), false);
        this.currLevel = level;
        if(level > 0)
        setBackground("Castle.gif");
        else setBackground("Outside6.png");
    }
}
