import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class level2Block here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class level2Block extends Block
{
    boolean pass =false;
    public level2Block(GreenfootImage image){
        this.setImage (image);

    }
    
    public level2Block (GreenfootImage image, boolean pass){
        this.setImage (image);
        this.pass = pass;
    }

    /**
     * Act - do whatever the level2Block wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
        getWorld().setPaintOrder (Shell.class, Block.class);
    } 

    public boolean getPass(){
        if (pass==true){
            return true;
        }
        return false;
    }
}
