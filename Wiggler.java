import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;

/**
 * Write a description of class Wiggler here.
 * 
 * @author Nicholas Chan
 * @version June 2015
 */
public class Wiggler extends Enemies
{

    private int aggravatedSpeed; //Speed wiggler moves at when aggravated

    private int delay = 0;  //Delay variable for dealing with lives
    private int actsAggravated = 0; //Number of acts at which the wiggler has been aggravated for

    private int rotationValue = 2; //Rate at which wiggler rotates at upon death

    private boolean isAlive = true; //Determines if wiggler is alive
    private boolean marioAlive = true; //Determines if mario is alive
    private boolean deathAnimation = false; //Determines if the death animation should be run (wiggler "falls" off the screen)
    private boolean isAggravated = false; //Determines if the wiggler is in its aggravated state

    //Array storing the wiggler sprites
    private String[] wigglerSprites = {"WigglerWalking1.png","WigglerWalking2.png","WigglerWalking3.png","WigglerWalking4.png","WigglerAggravated1.png","WigglerAggravated2.png","WigglerAggravated3.png","WigglerAggravated4.png"};

    /**
     * Creates an instance of Wiggler
     */
    public Wiggler()
    {
        this.startSpeed = -1;   //Setting the inital speed of the wiggler
        this.aggravatedSpeed = -2;  //Setting the aggravated speed of the wiggler
    }

    /**
     * Loads and adds the wiggler sprites to the world
     */
    public void addedToWorld (World world)
    {
        character = new GreenfootImage [wigglerSprites.length]; //Creates an array, character, with a size equal to the amount of wiggler sprites

        //Loading the sprites into the character array
        for (int i = 0; i < character.length; i++)
        {
            character[i] = new GreenfootImage(wigglerSprites[i]);
        }      
    }

    /**
     * Act - do whatever the Wiggler wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        //Runs methods if the Wiggler is still alive
        if (this.isAlive == true)
        {
            //If the Wiggler is not in its aggravated state
            if (this.isAggravated == false)
            {
                move(this.startSpeed);  //Move at its normal speed
            }
            //If the Wiggler is aggravated
            else 
            {
                move(this.aggravatedSpeed); //Moves at its aggravated speed
                this.actsAggravated++;  //Increments actsAggravated

                //If it has been aggravated for 300 acts, Wiggler returns back to its passive state
                if (this.actsAggravated == 300)
                {
                    this.isAggravated = false;
                    this.actsAggravated = 0;    //Resets actsAggravated
                }
            }

            checkHitItems();    //Checks if Wiggler comes into contact with an item
            checkHitFireBall();

            checkFall();    //Checks if the wiggler is falling (if it is in midair, the wiggler will fall. If the wiggler is not in midair, it does not need to fall)

            checkMovement(); //Responsible for the movement animation. If it is moving left, wiggler sprites will make the wiggler look like it's moving left, and vice versa for moving towards the right
            checkLeftWalls(); //Checks for contact with walls to the left
            checkRightWalls();  //Checks for contact with walls to the right

            if (this.startSpeed >0){
                this.aggravatedSpeed= Math.abs(aggravatedSpeed);
            } else {
                this.aggravatedSpeed= -Math.abs(aggravatedSpeed);
            }

            //If mario is alive, then check to see if Mario comes into contact with the Wiggler
            if (this.marioAlive == true)
            {
                checkHitMario(); 
            }         
        }
        deathAnimation();   //If deathAnimation is true, death animation will be displayed
        checkTouchChasm();
    }   

    /**
     * Determines which Wiggler sprites to display based on the direction it is moving.
     */
    protected void checkMovement()
    {
        //Setting up a delay

        //If the count variable is equal to zero, then animate the Wiggler
        if (count == 0)
        {
            //If the Wiggler is moving left:
            if (this.movingLeft == true)
            {
                //And if it is not aggravated then run the walking left animation
                if (this.isAggravated == false)
                {
                    walkingLeft();
                }
                //Otherwise, if it is aggravated, run the running left animation
                else 
                {
                    runningLeft();
                }
            }
            //If the Wiggler moving right
            else 
            {
                //And if it is not aggravated then run the walking right animation
                if (this.isAggravated == false)
                {
                    walkingRight();
                }
                //Otherwise, if it is aggravated, run the running right animation
                else
                {
                    runningRight();
                }
            }
            count++;   //increment the count variable
        }
        //If count reaches 10, reset its value to 0
        else if (count == 10)
        {
            count = 0;
        }
        //If the aforementioned conditions have not been met, increment the count variable
        else {
            count++;
        }    
    }

    /**
     * Animates the wiggler to make it appear as if it were walking to the left. Everytime this method is run, the next sprite in the sequence is displayed
     */
    private void walkingLeft()
    {
        //If you are currently on frame 1, the wiggler's image is set to the image located in the character array with an index of 0
        if(frame == 1)
        {
            setImage(character[0]); //Sets wiggler's image to the image with an index of 0
        }
        //Code repeats for the rest of the frames
        else if(frame == 2)
        {
            setImage(character[1]);
        }
        else if(frame == 3)
        {
            setImage(character[2]);
        }
        //If frame 4 is reached:
        else if(frame == 4)
        {
            setImage(character[3]);
            frame = 1;  //Frame is reset to 1
            return; //Exits the method
        }
        frame++;      //Frame is incremented     
    }

    /**
     * Animates the wiggler to make it appear as if it were walking to the right. Everytime this method is run, the next sprite in the sequence is displayed
     */
    private void walkingRight()
    {
        //If you are currently on frame 1, the wiggler's image is set to the image located in the character array with an index of 0
        if(frame == 1)
        {
            GreenfootImage temp = new GreenfootImage (character[0]);    //Stores the sprite with an index of 0 in the character array into a temp Greenfoot Image
            temp.mirrorHorizontally();  //Flip the temp image horizontally
            this.setImage (temp);   //Set the wiggler's image as that of the temp image
        }
        //Code repeats through the rest of the frames
        else if(frame == 2)
        {
            GreenfootImage temp = new GreenfootImage (character[1]);
            temp.mirrorHorizontally();
            this.setImage (temp);
        }
        else if(frame == 3)
        {
            GreenfootImage temp = new GreenfootImage (character[2]);
            temp.mirrorHorizontally();
            this.setImage (temp);
        }
        else if(frame == 4)
        {
            GreenfootImage temp = new GreenfootImage (character[3]);
            temp.mirrorHorizontally();
            this.setImage (temp);
            frame = 1;  //Frame is reset to 1
            return; //Exits the method
        }
        frame++;    //Increment frame
    }

    /**
     * Animates the wiggler to make it appear as if it were running to the left. Everytime this method is run, the next sprite in the sequence is displayed
     */
    private void runningLeft()
    {
        //If you are currently on frame 1, the wiggler's image is set to the image located in the character array with an index of 4
        if(frame == 1)
        {
            setImage(character[4]); //Sets wiggler's image to the image with an index of 4
        }
        //Code repeats for the rest of the frames
        else if(frame == 2)
        {
            setImage(character[5]);
        }
        else if(frame == 3)
        {
            setImage(character[6]);
        }
        //If frame 4 is reached:
        else if(frame == 4)
        {
            setImage(character[7]);
            frame = 1;  //Frame is reset to 1
            return; //Exits the method
        }
        frame++;      //Frame is incremented     
    }

    /**
     * Animates the wiggler to make it appear as if it were running to the right. Everytime this method is run, the next sprite in the sequence is displayed
     */
    private void runningRight()
    {
        //If you are currently on frame 1, the wiggler's image is set to the image located in the character array with an index of 4
        if(frame == 1)
        {
            GreenfootImage temp = new GreenfootImage (character[4]);    //Stores the sprite with an index of 4 in the character array into a temp Greenfoot Image
            temp.mirrorHorizontally();  //Flip the temp image horizontally
            this.setImage (temp);   //Set the wiggler's image as that of the temp image
        }
        //Code repeats through the rest of the frames
        else if(frame == 2)
        {
            GreenfootImage temp = new GreenfootImage (character[5]);
            temp.mirrorHorizontally();
            this.setImage (temp);
        }
        else if(frame == 3)
        {
            GreenfootImage temp = new GreenfootImage (character[6]);
            temp.mirrorHorizontally();
            this.setImage (temp);
        }
        else if(frame == 4)
        {
            GreenfootImage temp = new GreenfootImage (character[7]);
            temp.mirrorHorizontally();
            this.setImage (temp);
            frame = 1;  //Frame is reset to 1
            return; //Exits the method
        }
        frame++;    //Increment frame
    }

    private void checkHitMario()
    {             
        scanMario();    //Scans for an instance of Mario on the screen

        //If Mario jumps ontop of the Wiggler, Mario "bounces" off of it and the Wiggler becomes aggravated
        if ((mario.getY() >= getY()-30 && mario.getX() >= getX() - 35 && mario.getX() <= getX() + 35) && isTouching(Mario.class))
        {
            mario.jump();   //Mario jumps
            this.isAggravated = true;   //Wiggler becomes aggravated
            frame=1;
            this.actsAggravated = 0;    //Resets the actsAggravated variable
        }

        //If Mario touches the Wiggler from the side, he loses a life
        if (getOneObjectAtOffset(-40, 0, Mario.class) != null || getOneObjectAtOffset(40, 0, Mario.class) != null)
        {
            if (mario.isInvincible()){
                this.isAlive = false; //die 
                deathAnimation = true;//squish
            } else{
                mario.loseALife();
            }
        }
    }

    /**
     * Scans for instances of Mario on the screen and adds it to a List of Marios. Mario can then be identified as there is only one instance of mario on the screen at all times
     */
    private void scanMario()
    {
        List marios = getWorld().getObjects(Mario.class);   //Loads a List with all instances of the Mario class
        mario = (Mario) marios.get(0);  //The private object mario becomes the mario located at an index of 0 in the List
    }

    /**
     * Determines if the wiggler comes into contact with an item
     */
    private void checkHitItems()
    {
        //If a shell hits the wiggler from the side, the wiggler will be instantly killed and will "fall" off the screen
        if (getOneObjectAtOffset(0, 5, Shell.class) != null && this.isAlive == true)
        {
            Shell shell = (Shell) getOneObjectAtOffset(0, 5, Shell.class);  //Retrieve the shell that the wiggler comes into contact with

            //If the shell's speed is not equal to zero, in other words, is moving:
            if (shell.getSpeed() != 0)
            {
                this.isAlive = false;    //wiggler is no longer alive
                this.deathAnimation = true; //deathAnimation is set to true
            }
        }

    }

    /**
     * Checks if a fire ball hits the koopa.
     */
    private void checkHitFireBall(){
        if (getOneIntersectingObject (FireBall.class)!= null){
            this.isAlive = false; //die 
            deathAnimation = true;//kill
        }

    }

    /**
     * Animates the wiggler to make it appear as if it were "falling" off the screen
     */
    private void deathAnimation()
    {
        //If deathAnimation is true
        if (deathAnimation == true)
        {
            setLocation(getX(), getY() + 5);    //Set wiggler's location to 5 pixels lower (vertically) than its original position
            setRotation(rotationValue * count); //Set wiggler's rotation to the rotatinValue mutlipied by the count variable
            count++;    //Increment count variable

            //If the wiggler reaches the edge of the world, remove it from the world
            if (getY() == 399)
            {
                Greenfoot.playSound("enemygetshit.wav");
                getWorld().removeObject(this);
            }
        }
    }

    public void checkLeftWalls()
    {
        int spriteWidth = getImage().getWidth();
        int xDistance = (int)(spriteWidth/-2);
        Actor leftWall = getOneObjectAtOffset(xDistance, 0, Block.class);

        Wiggler wiggler = new Wiggler();    //Creates a new instance of Wiggler
        //getWorld().addObject(wiggler, getX(), getY());  //Adds this new Wiggler to the world at the current Wiggler's location
        // wiggler.move(-1);   //Move the new Wiggler to the left
        //wiggler.checkFall();    //Check to see if the new Wiggler falls

        //If the current Wiggler hits a wall to its left or if the new Wiggler falls off the platform then:
        if(leftWall != null || wiggler.checkFalling() == true)
        {
            if (this.getSpeed()>0){
                setLocation (leftWall.getX() +(leftWall.getImage().getWidth()/2) + (getImage().getWidth()/2)+10, getY());
            } else {
                setSpeed(-(getSpeed()));    //Reverse the Wiggler's speed
                frame =1;
                this.movingLeft = false;    //Reverse the Wiggler's direction
            }
        }
        // getWorld().removeObject(wiggler);   //Remove the new Wiggler from the world
    }

    public void checkRightWalls()
    {
        int spriteWidth = getImage().getWidth();
        int xDistance = (int)(spriteWidth/2);
        Actor rightWall = getOneObjectAtOffset(xDistance, 0, Block.class);

        Wiggler wiggler = new Wiggler();    //Creates a new instance of Wiggler
        //getWorld().addObject(wiggler, getX(), getY());  //Adds this new Wiggler to the world at the current Wiggler's location
        //wiggler.move(1);   //Move the new Wiggler to the left
        //wiggler.checkFall();    //Check to see if the new Wiggler falls

        //If the current Wiggler hits a wall to its right or if the new Wiggler falls off the platform then:
        if(rightWall != null || wiggler.checkFalling() == true)
        {
            if (this.getSpeed()<0){
                setLocation (rightWall.getX() -(rightWall.getImage().getWidth()/2) - (getImage().getWidth()/2)-10, getY());
            } else {
                setSpeed(-(getSpeed()));    //Reverse the Wiggler's speed
                frame =1;
                this.movingLeft = true;     //Reverse the Wiggler's direction
            }
        }
        //getWorld().removeObject(wiggler);   //Remove the new Wiggler from the world
    }

    /**
     * Sets the speed of the wiggler
     * 
     * @param speed The speed at which the wiggler moves at
     */
    public void setSpeed(int speed)
    {
        this.startSpeed = speed;     //Sets the current speed of the wiggler to the speed indicated in the parameter  
    }

    /**
     * Returns the speed at which the wiggler is currently moving at
     * 
     * @return int Speed that the wiggler is moving at.
     */
    public int getSpeed()
    {
        return this.startSpeed; //returns start speed
    }

    public boolean checkFalling()
    {
        if (isAirborn == true)
        {
            return true;
        }
        return false;
    }
}
