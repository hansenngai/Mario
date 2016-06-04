import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class wings here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Wings extends Actor
{
    private String [] picNames = {"wings1.fw.png","wings2.fw.png","wings3.fw.png"};
    private GreenfootImage character [];
    private boolean animation=true;
    private int delay;
    private int counter;
    
    private Mario target;

    public Wings(Mario mario)
    {
        target = mario;
    }
    
    /**
     * Act - do whatever the wings wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        animation();
        this.setLocation(target.getX(),target.getY());
    }           

    public void addedToWorld (World world){
        Greenfoot.playSound("readytofly.wav");
        character = new GreenfootImage [picNames.length];
        for (int i=0; i< character.length; i++){
            character [i]= new GreenfootImage (picNames [i]);
        }
    }

    public void animation(){
        if (animation ==true){
            delay++;
            if (delay%8==0){

                setImage (character[counter]);
                counter++;
            }
            if (counter == character.length){
                counter =0;
            }
        }
    }
}
