import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Stripes here.
 * 
 * @author (Ramy Elbakari) 
 * @version (June 11, 2015)
 */
public class Spikes extends ScrollObjects
{
    private int delay=30;
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

    private Mario mario;
    /**
     * If spikes are to the left of Mario and touches him, Mario looses a life.
     * Gives Mario a delay of 30 pixels before inflicting more
     */
    public void checkHitLeft(){

        mario = (Mario) getOneObjectAtOffset (-getImage().getWidth()/2, 0, Mario.class);

        if (mario!= null){

            if (mario.isInvincible()){ // if mario is not invincible, then
            } else if (delay%30==0){  // allow for 30 acts to escape
                delay++;
                mario.loseALife();  // mario loses a life
            }
        }
    }

     /**
     * If spikes are to the right of Mario and touches him, Mario looses a life.
     * Gives Mario a delay of 30 pixels before inflicting more
     */
    public void checkHitRight(){
        mario = (Mario) getOneObjectAtOffset (getImage().getWidth()/2, 0, Mario.class);  // similar concept as the one above
        if (mario!= null){

            if (mario.isInvincible()){
            } else if (delay%30==0){
                delay++;
                mario.loseALife();
            }
        }

    }
}
