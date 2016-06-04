import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Jumper here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Jumper extends ScrollObjects
{
    private String [] picNames = {"jumper1.png","jumper2.png","jumper3.png","jumper4.png", "jumper5.png"};  // stores the names of all the images
    private GreenfootImage character [];  // stores the images of the animation

    private int frame; // the current frame (sprite) the animation is on
    private boolean animation=false; // when to start the animation, true- start, false- do not perform any animation
    private boolean reverse=false; // reverse animatio, true- reverse, false- no reverse

    private int yCor=0; // the y-coordinate that the jumper rests on (graphical extent)
    /**
     * Act - do whatever the Jumper wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
        animation();
        checkHit();
    }  

    public void addedToWorld (World world){

        character = new GreenfootImage [picNames.length];
        for (int i=0; i< character.length; i++){
            character [i]= new GreenfootImage (picNames [i]);
        }
        yCor = getY()+ getImage().getHeight()/2;

    }
    
    private boolean stopAnimation=false; // perform the animation once, true- performed once, false- still undergoing the first animation
    /**
     * Performes the animation of the jumper (Only once)
     */
    private void animation(){
        if (animation ==true){  // if animation is on, then

            setImage (character[frame]); // set the image of the current frame
            // size of each frame is different
            // set the location of the image so that the jumper rests on the ground
            setLocation (getX(), yCor-getImage().getHeight()/2);

            if (reverse ==false){ // if no reversing of animation is on, then
                frame++;  // move to the next frame
            } else {  // otherwise,
                frame--;  // move to the previous frame
            }

            if (frame == character.length){ // if you reach the final frame,
                reverse =true; // reverse the animation 
                stopAnimation =true; // performed the animation once (still the reverse however to be perfomed)
                frame--; // move to the previous frame
            }
            if (frame < 0){  // if you are already performed the reverse animation (since frame is -1)
                reverse =false; // do not reverse the next animation
                frame++; // the next frame becomes the initial frame 
                if (stopAnimation =true){  // if animation was perfomed once, then
                    animation =false;  // stop the entire animation (stationary image)
                }
            }
        }
    }

    Mario mario; // stores the mario on the screen

    /**
     * Checks to see if Mario jumps on the jumper.
     * If he did, it makes Mario jump at a high speed and marks the begining of the  animation of the jumper.
     */
    private void checkHit(){

        mario = (Mario) getOneObjectAtOffset (0,-(getImage().getHeight()/2), Mario.class); // save the mario that is directly above the jumper

        if (mario != null){ // if mario does indeed exsist
            
            if (animation !=true){ // if there is no animation for the jumper (stationary mode), then
                mario.setSpeed(0); // set mario's previous speed to 0 - elastic force set speed to 0
                mario.jump (20); // jump with an intial speed of 20

                animation =true;  // perform animation
            }

        } 

    }
}
