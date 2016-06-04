import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class InventoryObject here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
abstract class InventoryObject extends ScrollObjects
{
    protected int typeIndex;

    /**
     * Act - do whatever the InventoryObject wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
    }    

    public int getTypeIndex()
    {
        return typeIndex;
    }
}
