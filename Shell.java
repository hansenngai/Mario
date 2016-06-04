import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;

/**
 * Write a description of class Shell here.
 * 
 * @author Nicholas Chan
 * @version June 2015
 */
public class Shell extends Enemies
{

    private int rotationValue = 20; //Rate at which the shell rotates at upon death
    private int actsIdle = 0; //Number of acts the shell is idle for (stationary)
    private int delay = 0;  //Delay variable for dealing wtih mario's lives

    private boolean isStationary = true;    //Determines if the shell is stationary
    private boolean marioDied = false;  //Determines if Mario is dead
    private boolean shellDeath = false; //Determines if the shell has "died"

    //Array containing shell movement sprites
    private String[] shellSprites = {"Shell5.png", "KoopaShell1.png", "KoopaShell2.png", "KoopaShell3.png", "KoopaShell4.png", "KoopaShell5.png"};

    /**
     * Creates an instance of Shell
     */
    public Shell()
    {
        this.startSpeed = 0;    //Setting the initial speed of the shell to zero
    }

    /**
     * Act - do whatever the Shell wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        //Runs methods if the Shell is still alive
        if (this.shellDeath == false) 
        {
            move(this.startSpeed);  //Move at the startSpeed
            checkFall();    //Checks if the shell is midair (not touching a platform). If it is, method will bring shell back to the ground
            checkLeftWalls();   //Checks for contact with walls to the left
            checkRightWalls();  //Checks for contact with walls to the right

            //If the shell is moving:
            if (getSpeed() != 0)
            { 
                checkMovement();    //Responsible for managing the movement animations
            }
            
            //If Mario is still alive, then check to see if the shell intersects with an instance of Mario
            if (marioDied == false){
                checkHitMario();
            }

            //If the shell is stationary, increment the actsIdle variable
            if (isStationary) {
                actsIdle++;            
            }

            //If actsIdle reaches 230, then spawn a green koopa
            if (actsIdle == 230)
            {
                spawnGreenKoopa();
            }
        }
        //If the shell is not alive, then run the death animation
        else 
        {      
            deathAnimation();
        }
        checkTouchChasm();
    }    

    /**
     * Loads and adds the shell sprites to the world
     */
    public void addedToWorld (World world)
    {
        //Creates an array, character, with a size equal to the amount of shell sprites
        character = new GreenfootImage [shellSprites.length];

        //Loading the sprites into the character array
        for (int i = 0; i < character.length; i++)
        {
            character[i] = new GreenfootImage(shellSprites[i]);
        } 
    }


    /**
     * Animates the shell to make it appear as if it were spinning to the left. Everytime this method is run, the next sprite in the sequence is displayed
     */
    protected void animateLeft()
    {
        //If you are currently on frame 1, the koopa's image is set to the image located in the character array with an index of 0
        if(frame == 1)
        {
            setImage(character[0]);
        }
        //Code is repeated for the rest of the frames
        else if(frame == 2)
        {
            setImage(character[1]);
        }
        else if(frame == 3)
        {
            setImage(character[2]);
        }
        else if(frame == 4)
        {
            setImage(character[3]);
        }
        //If frame 5 is reached:
        else if(frame == 5)
        {
            setImage(character[4]);
            frame = 1;  //Frame is reset to 1
            return; //Exits the method
        }
        frame++;      //Frame is incremented
    }

    /**
     * Animates the shell to make it appear as if it were spinning to the right. Everytime this method is run, the next sprite in the sequence is displayed
     */
    protected void animateRight()
    {
        //If you are currently on frame 1, the koopa's image is set to the image located in the character array with an index of 4
        if(frame == 1)
        {
            setImage(character[4]);
        }
        //Code is repeated for the rest of the frames
        else if(frame == 2)
        {
            setImage(character[3]);
        }
        else if(frame == 3)
        {
            setImage(character[2]);
        }
        else if(frame == 4)
        {
            setImage(character[1]);
        }
        else if(frame == 5)
        {
            setImage(character[0]);
            frame = 1;  //Frame is reset to 1
            return; //Exits the method
        }
        frame++;     //Frame is incremented
    }

    /**
     * Determines if the shell comes into contact with Mario. The shell behaves differently depending on where and when contact is made.
     */
    private void checkHitMario()
    {
        scanMario();        //Scans for an instance of mario in the world

        //If Mario touches the shell from the left side:
        if (getOneObjectAtOffset(-10, 1, Mario.class) != null)
        {  
            //If the shell was stationary before Mario touched it then the shell begins to move in the direction in which it was pushed (right)
            if (getSpeed() == 0)
            {
                Greenfoot.playSound("bump.wav");
                setSpeed(3);    //Setting the new speed of the shell
                this.isStationary = false;  //Shell is no longer stationary
                this.movingLeft = false;    //Shell is now moving to the right
            }
        }
        //If Mario touches the shell from the right side:
        else if (getOneObjectAtOffset(10, 1, Mario.class) != null)
        {
            //If the shell was stationary before Mario touched it, shell moves to the left
            if (getSpeed() == 0)
            {
                Greenfoot.playSound("bump.wav");
                setSpeed(-3);  //Setting the new speed of the shell
                this.isStationary = false;  //Shell is no longer stationary
                this.movingLeft = true;     //Shell is moving to the left
            }
        }
        //If Mario touches the shell while the shell is moving:
        else if (getOneObjectAtOffset(-5, 1, Mario.class) != null || getOneObjectAtOffset(5, 1, Mario.class) != null)
        {
            if (getSpeed() != 0)
            {
                //If Mario's lives is at 1
                if (mario.getLives() == 1)
                {
                    mario.loseALife(); //Mario loses a life
                    System.out.println(mario.getLives());
                    this.marioDied = true;  //Mario has been killed
                }
                //If Mario's total number of lives is greater than 1
                else {
                    mario.loseALife();  //Mario loses a life
                    System.out.println(mario.getLives());
                }
                return; //Exits the method
            }
        }
        //If Mario lands on top of the shell
        else if (getOneObjectAtOffset(0, -5, Mario.class) != null || getOneObjectAtOffset(0, -5, Mario.class) != null)
        {
            mario.jump();   //Mario will jump up from the shell

            //If the shell was moving when Mario jumped on it, then the shell is stopped
            if (getSpeed() != 0)
            {
                setSpeed(0);    //Setting the speed of the shell to 0
                this.isStationary = true;   //Shell becomes stationary
                this.actsIdle = 0;  //Resetting the actsIdle variable
            }
            //If the shell was stationary when Mario jumped on it, shell has been "killed"
            else {
                this.isStationary = true;
                this.shellDeath = true; //shellDeath is set to true to indicate that it will soon be removed from the world
                count = 1;  //Resetting the count variable
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
     * Animates the shell to make it appear as if it were "falling" off the screen
     */
    private void deathAnimation()
    {
        //If deathAnimation is true
        if (this.shellDeath == true)
        {
            setLocation(getX(), getY() + 5);    //Set shell's location to 5 pixels lower (vertically) than its original position
            setRotation(rotationValue * count); //Set shell's rotation to the rotatinValue mutlipied by the count variable
            count++;    //Increment count variable

            //If the shell reaches the edge of the world, remove it from the world
            if (getY() == 399)
            {
                getWorld().removeObject(this);
            }
        }
    }

    protected void checkFall()
    {
        if(onGround())
        {
            if (shellDeath == false)
            { 
                vSpeed = 0;
            }
        }
        else
        {
            fall();
        }
    }


    public void checkLeftWalls()
    {
        int spriteWidth = getImage().getWidth();
        int xDistance = (int)(spriteWidth/-2);
        Actor leftWall = getOneObjectAtOffset(xDistance, 0, Block.class);

        //If the shell comes into contact with a wall to its left:
        if(leftWall != null)
        {
            Greenfoot.playSound("bump.wav");
            setSpeed(-(getSpeed()));    //reverses the shell's speed
            this.movingLeft = false;    //reverses the shell's direction
        }
    }

    public void checkRightWalls()
    {
        int spriteWidth = getImage().getWidth();
        int xDistance = (int)(spriteWidth/2);
        Actor rightWall = getOneObjectAtOffset(xDistance, 0, Block.class);

        //If the shell comes into contact with a wall to its right
        if(rightWall != null)
        {
            Greenfoot.playSound("bump.wav");
            setSpeed(-(getSpeed()));    //reverses the shell's speed
            this.movingLeft = true;     //reverses the shell's direction
        }
    }

    /**
     * Spawns a new Green Koopa at the shell's location
     */
    private void spawnGreenKoopa()
    {
        getWorld().addObject(new GreenKoopa(), getX(), getY()); //Adds a new koopa to the world at the shell's location
        getWorld().removeObject(this); //Removes the shell from the world
    }

    /**
     * Sets the speed of the Shell
     * 
     * @param speed The speed at which the koopa moves at
     */
    public void setSpeed(int speed)
    {
        this.startSpeed = speed;  //Sets the current speed of the shell to the speed indicated in the parameter         
    }

    /**
     * Returns the speed at which the shell is currently moving at
     * 
     * @return int Speed that the shell is moving at.
     */
    public int getSpeed()
    {
        return this.startSpeed; //returns start speed
    }
}
