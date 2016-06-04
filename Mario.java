import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.awt.image.ColorModel;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.List;

/**
 * Controls all the controls of Mario and controls how Mario behave to his environment.
 * 
 * @author (Ramy Elbakari, Daniel Chen, Issac) 
 * @version (June 11, 2015)
 */
public class Mario extends Characters
{
    private String[] picNames= {"Mario1.png","Mario2.PNG","Mario3.PNG","Mario4.png"}; // save all the name of Mario's images in a string array
    private int lives;  // lives
    private boolean jumping=false;  // indicates if mario is airbourne - true - he is, false - he is not
    private int animationCounter,frame;
    private int vSpeed = 0;  // vertical speed
    private int acceleration = 1; // acceleration - the world's gravity is 1 pixel per act
    private int speed=2; // horizontal speed - 2 pixels per act
    private int jumpStrength = 10; // jump strength is also the initial speed when he jumps (10 pixels per act)- gravity keeps decreasing the speed every act
    private boolean onTree=false; // indicates whether mario is on a tree - true - he is , false - he is not
    private boolean hasKey=false; // indicates whether mario has a key (to open the next door) - true - he has , false - he does not have

    private boolean spawned=false; // is mario spawned, true -yes
    private boolean isAlive = true;  // is mario alive, true -yes
    private boolean turnLockOff=false; // whether mario jump from the tree, true -yes
    private boolean invincibleAnimation=false; // is mario invincible animation on

    //added
    private boolean usingPower = false;  // is mario using one of the powerups
    private Status status;  // instance of process bars

    private boolean inFlight = false;    // is mario in flight
    private Wings wings;    // instance of the wings

    private boolean onFire = false;  // can mario shoot fire
    private boolean facingRight = false;  // is mario facing right

    private Chasm chasm;  // instance of the chasm mario controls
    private boolean chasmOpen = false;  // is the chasm open

    private boolean isInvincible = false;  // is mario undergoing the invincible ability

    // yes = ture, no= false

    GreenfootSound music = new GreenfootSound("invincibility.mp3");  // background music
    public Mario()
    { 
        lives = 5;  // mario starts with 5 lives
    }

    /**
     * determine if the world scroll or not
     */
    private boolean scroll()
    {
        DanielWorld dw = (DanielWorld)getWorld(); // instance of super world
        if(dw.getScroll())  // if the world is world is scolling, return true
            return true;  
        return false;
    }

    /**
     * Goes to the next level or mini level when on top of a tube
     */
    private void nextLevel()
    {
        if(getOneIntersectingObject(Transporter.class) != null)  // if mario touches the trasporter
        {
            if(getY() < getOneIntersectingObject(Transporter.class).getY() - 10)
            {
                DanielWorld dw = (DanielWorld)getWorld();
                dw.stopMusic();               
                if(dw.getLevel() == 1)
                {
                    Level2 l2 = new Level2();
                    getWorld().removeObject(this);
                    Greenfoot.setWorld(l2);
                }
                else if(dw.getLevel() == 3)
                {
                    Level4 l4 = new Level4();
                    getWorld().removeObject(this);
                    Greenfoot.setWorld(l4);
                }
                else if (dw.getLevel() == 5)
                {
                    DanielWorld finished = new DanielWorld();
                    Greenfoot.setWorld(finished);
                }
                else {
                    DanielWorld nextlvl = new DanielWorld(dw.getLevel()+1);
                    getWorld().removeObject(this);
                    Greenfoot.setWorld(nextlvl);    
                }
            }
        }
        else if (getOneIntersectingObject(MiniWorld.class)!= null)
        {
            if(getY() < getOneIntersectingObject(MiniWorld.class).getY() - 10)
            {               
                DanielWorld dw = (DanielWorld)getWorld();
                dw.stopMusic();
                MiniLevel ml = new MiniLevel(dw.getLevel());
                getWorld().removeObject(this);
                Greenfoot.setWorld(ml);    
            }
        }

    }

    /**
     * Runs when Mario is added in the world.
     * Saves all Mario's images in character array.
     */
    public void addedToWorld (World world){

        character = new GreenfootImage [picNames.length]; // initialize the character array (same length as the array that store's the names of Mario's images)
        for (int i=0; i< character.length; i++){ // loop through each element
            character [i]= new GreenfootImage (picNames [i]); // save an image of Mario in each element (4 images in total)
        }

    }

    /**
     * Act - do whatever the Mario wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if(scroll())
        {
            if(!spawned)
                spawn();
            else
                checkScroll();
        }
        checkFall();
        platformAbove();
        checkRightWalls();
        checkLeftWalls();
        carrying();
        checkKey();
        updateLives();

        checkHitEndOfWorld();
        nextLevel();

        invincibleAnimation();
        invinciblePowers();

        //new
        moveInFlight();
        fireActions();
        chasmActions();
        fallOutOfWorld();
        animationCounter++;
    } 

    /**
     * Checks if one of the arrow keys is pressed.
     * Performs the function of the pressed key.
     */
    private void checkKey()
    {

        if (!Greenfoot.isKeyDown("right") && !Greenfoot.isKeyDown("left") ){
            checkTree();
            if (lockMario==true){
                turnLockOff =true;
            }
        }
        if(Greenfoot.isKeyDown("right")) // if the user presses the right arrow
        {

            checkTree();
            if (lockMario==true){
            } else{

                moveRight(); // call the method that moves Mario to the right
            }
        }

        if(Greenfoot.isKeyDown("left")) // if the user presses the left arrow
        {
            checkTree();
            if (lockMario==true){
            } else{

                moveLeft(); // call the method that moves Mario to the right
            }

        }
        if(Greenfoot.isKeyDown("up") && jumping == false ) // if the user presses the up arrow and Mario is not already airbourne (to avoid him jumping while he is in the air)
        {
            if (onTree ==false){  // if Mario is not on a tree
                jump(jumpStrength); // call the method that makes Mario jump
            } else {  // however, if Mario is on a tree
                climbUp();  // call the method that makes Mario climb the tree
            }
        } 
        if (Greenfoot.isKeyDown("down") && onTree ==true){  // if the user presses the down arrow and Mario is on a tree
            climbDown();  // call the method that Makes Mario climb down
        }
    }
    private int climbFrame=1; // the climb animation has 2 frames (picture)- keeps track of which frame to display next
    /**
     * Moves mario up (on the tree). Distance he moves up is equal to his speed.
     * Performs climb animation (uses climbAimation).
     */
    private void climbUp (){
        setLocation (getX(), getY() - speed); // move mario up (distance he moves up is equal to his speed)
        climbAnimation(); // perform the climb animation
    }

    /**
     * Moves mario down (on the tree). Distance he moves down is equal to his speed.
     * Performs climb animation (uses climbAimation).
     */
    private void climbDown (){
        if (getOneObjectAtOffset (0, getImage().getHeight()/2, Block.class)== null){ // if there is no block underneath mario, then
            setLocation (getX(), getY() + speed);  // move mario down (distance he moves down is equal to his speed)
        }
        climbAnimation(); // perform the climb animation
    }

    /**
     * Perfoms the climb animation
     */
    private void climbAnimation(){
        // delay between each frame is 7 acts (smoother animation)
        if (climbFrame ==1){ // if counter is 1, then
            this.setImage (new GreenfootImage ("climb1.png")); // switch to first image
        } else if (climbFrame ==7){ // if counter is 7 , then
            this.setImage (new GreenfootImage ("climb2.png")); // switch to second image
        }
        climbFrame++; // increase climb frame
        if (climbFrame >7) { // if climb frame is more than 7
            climbFrame =1;  // return back to 1
        }
    }

    /**
     * Moves Mario to the right. The distance he covers is equal to his speed.
     * Performs the moving to the right animation (uses animateRight method).
     */
    private void moveRight()
    {
        setLocation(getX()+speed, getY()); // move mario to the right (distance he moves down is equal to his speed)

        if(animationCounter % 4 == 0&& onGround() == true) // delay between each frame is 4 acts (smoother animation)
        {
            if (onTree ==false){ // if Mario is not on the tree, then
                animateRight(); // perform the moving to the right animation
            }
        }
        facingRight = true;
    }

    /**
     * Performs the moving to the right animation.
     */
    private void animateRight()
    {
        // sets the image depending on the current frame
        // frame changes every four acts (delay)
        if(frame == 1) // if current frame is 1, then
        {
            setImage(character [0]); // set the first image in the array
        }
        else if(frame == 2) // if current frame is 2, then
        {
            setImage(character [1]); // set the second image in the array
        }
        else if(frame == 3) // if current frame is 3, then
        {
            setImage(character [2]); // set the third image in the array
        }
        else if(frame == 4) // if current frame is 4, then
        {
            setImage(character [3]); // set the fourth image in the array
            frame = 1; // set the frame back to 1 (start animation from begining)
            return; // exit the method
        }
        frame++; // increase the frame
    }

    /**
     * Moves Mario to the left. The distance he covers is equal to his speed.
     * Performs the moving to the left animation (uses animateLeft method)
     */
    private void moveLeft()
    {
        setLocation(getX()-speed, getY()); // move mario to the left (distance he moves down is equal to his speed)
        if(animationCounter % 4 == 0 && onGround() == true) // delay between each frame is 4 acts (smoother animation)
        {
            if (onTree ==false){ // if Mario is not on the tree, then
                animateLeft(); // perform the moving to the left animation
            }
        }
        facingRight = false;
    }

    /**
     * Performs the moving to the right animation.
     */
    private void animateLeft()
    {
        // sets the image depending on the current frame
        // frame changes every four acts (delay)
        if(frame == 1) // if current frame is 1, then
        {
            GreenfootImage temp= new GreenfootImage (character [0]); // create temporary image that has the first image of the array
            temp.mirrorHorizontally(); // flip the temporary image horizontally
            this.setImage (temp); // set Mario's image to the temp image

        }
        else if(frame == 2) // if current frame is 2, then
        {
            GreenfootImage temp= new GreenfootImage (character [1]); // create temporary image that has the second image of the array
            temp.mirrorHorizontally(); // flip the temporary image horizontally
            this.setImage (temp); // set Mario's image to the temp image

        }
        else if(frame == 3) // if current frame is 3, then
        {
            GreenfootImage temp= new GreenfootImage (character [2]); // create temporary image that has the third image of the array
            temp.mirrorHorizontally(); // flip the temporary image horizontally
            this.setImage (temp); // set Mario's image to the temp image

        }
        else if(frame == 4) // if current frame is 4, then
        {
            GreenfootImage temp= new GreenfootImage (character [3]); // create temporary image that has the fourth image of the array
            temp.mirrorHorizontally(); // flip the temporary image horizontally
            this.setImage (temp); // set Mario's image to the temp image
            frame = 1; // set the frame back to 1 (start animation from begining)
            return; // exit the method
        }
        frame++; // increase the frame
    }

    /**
     * Makes Mario jump. Increases his vertical (initial) speed and gravity slowly brings him down.
     * User specifies the initial speed
     * @param jumpStrength - initial speed (positive number)
     */
    public void jump(int jumpStrength)
    {
        if (inFlight == false)
        {
            Greenfoot.playSound("jump.wav");
            vSpeed = vSpeed - jumpStrength; // his speed becomes his previous speed (which is generally 0) combined with the new added speed

            jumping = true; // mario is airbourne
            speed=3; // horizontal speed increases - mario can cover bigger distance while in air
            fall(); // call gravity (make him free fall)
        }
    }

    /**
     * Makes Mario jump. Increases his vertical (initial) speed and gravity slowly brings him down.
     * User specifies the initial speed
     * @param jumpStrength - initial speed (positive number)
     */
    public void jump()
    {
        vSpeed = 0 - jumpStrength; // his speed becomes his previous speed (which is generally 0) combined with the new added speed
        if (inFlight == false) // if mario is not flying , then
        {
            Greenfoot.playSound("jump.wav");
            jumping = true; // mario is airbourne
            speed=3; // horizontal speed increases - mario can cover bigger distance while in air
            fall(); // call gravity (make him free fall)
        }
    }

    private void spawn()
    {
        DanielWorld mw = (DanielWorld)getWorld();
        int rightBoundary = mw.getWidth()/2;
        int leftBoundary = mw.getWidth()/2;
        int topBoundary = mw.getHeight()/2 - 100;
        int bottomBoundary = mw.getHeight()/2 + 100;
        if (getX() > rightBoundary)
        {
            int dx = rightBoundary - getX();
            moveScreen(dx,0);
            setLocation (getX() + dx, getY());
        }
        else if (getX() < leftBoundary)
        {
            int dx = leftBoundary - getX();
            moveScreen(dx,0);
            setLocation (getX() + dx, getY());
        }
        if (getY() > bottomBoundary)
        {
            int dy = bottomBoundary - getY();
            moveScreen(0,dy);
            setLocation (getX(), getY() + dy);
        }
        else if (getY() < topBoundary)
        {
            int dy = topBoundary - getY();
            moveScreen(0,dy);
            setLocation (getX(), getY() + dy);
        }
        spawned = true;
    }

    /**
     * Checks if any of the object that on the edge of the map is on the screen, if so stops scrolling in
     * different directions depending on the object.
     */
    private void checkScroll()
    {
        DanielWorld mw = (DanielWorld)getWorld();
        int rightBoundary = mw.getWidth()/2 + 100;
        int leftBoundary = mw.getWidth()/2 - 100;
        int topBoundary = mw.getHeight()/2 - 200;
        int bottomBoundary = mw.getHeight()/2 + 200;
        ScrollObjects HighestPoint = mw.getHighestObject();
        ScrollObjects LowestPoint = mw.getLowestObject();
        ScrollObjects RightMostPoint = mw.getRightMostObject();
        ScrollObjects LeftMostPoint = mw.getLeftMostObject();
        if (getX() > rightBoundary)
        {
            if(RightMostPoint.getX() >= mw.getWidth())
            {
                int dx = rightBoundary - getX();
                moveScreen(dx,0);
                setLocation (getX() + dx, getY());
            }
        }
        else if (getX() < leftBoundary)
        {
            if(LeftMostPoint.getX() <= 0)
            {
                int dx = leftBoundary - getX();
                moveScreen(dx,0);
                setLocation (getX() + dx, getY());
            }
        }
        if (getY() > bottomBoundary)
        {
            if(LowestPoint.getY() >= mw.getHeight()/2)
            {
                int dy = bottomBoundary - getY();
                moveScreen(0,-vSpeed);
                setLocation (getX(), getY() - vSpeed);
            }
        }
        else if (getY() < topBoundary)
        {
            if(HighestPoint.getY() <= 100)
            {
                int dy = topBoundary - getY();
                moveScreen(0,dy);
                setLocation (getX(), getY() + dy);
            }
        }
    }

    /**
     * Checks if Mario is at the end of the world.
     * Does not let Mario through.
     */
    private void checkHitEndOfWorld()
    {
        if(getX()<=0)
            setLocation(0,getY());
        else if (getX()>=getWorld().getWidth())
            setLocation(getWorld().getWidth(),getY());
        if (getY() <= 0)
            setLocation(getX(),0);

    }

    /**
     * Moves camera across the screen depending on Mario's location
     */
    private void moveScreen(int x, int y)
    {
        DanielWorld mw = (DanielWorld)getWorld();
        mw.cameraMove(x,y);
    }

    /**
     * Checks to see if Mario is falling and performs functions based on the resutls. 
     * If Mario is not on the ground (and not on a tree), performs gravity on Mario and makes him free fall.
     * If Mario is on the ground, sets his vertical speed to 0.
     * Uses the onGround and fall methods.
     */
    public void checkFall()
    {
        if (onTree == false){ // if mario is not on the tree
            if(onGround()) // if mario is on the ground, then
            {
                vSpeed = 0;  // set his vertical speed to 0
            }
            else // otherwise, that means mario is free falling and
            {
                fall(); // call gravity (make him free fall)
            }
        }
    }

    /**
     * Checks if Mario falls out of the screen.
     * Re-locates Mario at the begining of the level.
     */
    private void fallOutOfWorld()
    {
        try
        {
            if(getY() > getWorld().getHeight()) // if mario passes the bottom of the screen
            {

                DanielWorld dw = (DanielWorld)getWorld();  // initalize the super classs

                if(dw.getLevel() == 2)            // depending on which level Mario is on, re-initalize that level
                {
                    Level2 l2 = new Level2();
                    getWorld().removeObject(this);
                    Greenfoot.setWorld(l2);
                }
                else if(dw.getLevel() == 4)         // level 2 and 4 are uniquely (code wise) different from the rest of the levels, therefore, has its own block 
                {
                    Level4 l4 = new Level4();
                    getWorld().removeObject(this);
                    Greenfoot.setWorld(l4);
                }
                else {
                    DanielWorld samelvl = new DanielWorld(dw.getLevel());  // 1,3,5 are obstructed from the same class, therefore, one block with different value in the 
                    getWorld().removeObject(this);                          // parameter
                    Greenfoot.setWorld(samelvl);    
                }

            }
        }
        catch (Exception e)
        {
        }
    }

    /**
     * Checks to see if Mario is on the ground or not.
     * @return true - Mario is on the ground, false - Mario is not on the ground
     */
    public boolean onGround()
    {

        Actor ground = getOneObjectAtOffset(0, getImage().getHeight()/2, Block.class); // save the ground that is located right underneath mario

        if(ground == null ) // if there is no ground, then
        {

            jumping = true; // mario is airbourne
            return false;
        }
        else // otherwise, 
        {

            moveToGround(ground); // move mario to be exactly above the ground
            jumping = false; // mario is not airbourne
            speed=2; // set the speed back to 2 (was 3 - when jump can cover greater distance)
            return true;
        }
    }

    /**
     * Moves Mario to be located right above the ground. More visually appealing.
     * @param ground - the ground actor that Mario is on
     */
    public void moveToGround(Actor ground)
    {
        // when mario jumps and lands his final (landing) speed is high and therefore might not land exactly on the ground.
        // for example: ground is at y-coordinate of: 200 pixels, mario is still free falling and at y-coordinate of : 195 pixels (still not on the ground - continue to fall)
        // his speed is 7 and therefore goes to a y-coordinate of 202 pixels. This method sets mario's y-coordinate to 200.

        int groundHeight = ground.getImage().getHeight(); // save the height of the ground
        int newY = ground.getY() - (groundHeight + getImage().getHeight())/2 + 1; // the y-coordinate for mario to be exactly on the floor (graphically speaking) is the ground's y-coordinate minus half of its height minus half of mario's height
        setLocation(getX(), newY); // set mario's y-coordinate to be exactly on the ground

    }

    /**
     * Enacts gravity on mario and all the enemies.
     */
    public void fall()
    {
        setLocation(getX(), getY() + vSpeed); // set mario's location to either move up or down based on the vertical speed (positive - moves down, negative - moves up)
        if(vSpeed <=9) // as long as mario's speed is less than 9 pixels/act, continue to acclerate 1 pixel/act
        {
            vSpeed = vSpeed + acceleration; // his speed is decreased 1 pixel/act due to gravity
        }
        jumping = true; // mario is airbourne
    }

    /**
     * Checks to see if there is a platform above mario.
     * @return true - there is, false - there isn't
     */
    public boolean platformAbove()
    {
        int spriteHeight = getImage().getHeight(); // save mario's height
        int yDistance = (int)(spriteHeight/-2); // the distance (graphically speaking) between mario and any platform
        Actor ceiling = getOneObjectAtOffset(0, yDistance, Block.class); // save the ceiling that is located right above mario
        if(ceiling != null) // if a ceiling does exsist, then
        {
            vSpeed = 1; // vertical speed will be reduced to 1 (start falling from that point)
            bopHead(ceiling); // set mario's location right underneath the ceiling
            return true;
        }

        return false; 

    }

    /**
     * Moves Mario to be located right undernath the platform (he hit). More visually appealing.
     * @param celing - the ceiling actor that Mario hit
     */
    public void bopHead(Actor ceiling)
    {
        // when mario jumps, his speed is high and therefore might not  exactly hit the ceiling.
        // for example: ceiling is at y-coordinate of: 200 pixels, mario jump and at y-coordinate of : 205 pixels (still did not hit the ceiling - continues to move up)
        // his speed is 7 and therefore goes to a y-coordinate of 198 pixels. This method sets mario's y-coordinate to 200.

        int ceilingHeight = ceiling.getImage().getHeight(); // save the height of the ceiling
        int newY = ceiling.getY() + (ceilingHeight + getImage().getHeight())/2; // the y-coordinate for mario to be exactly below the ceiling (graphically speaking) is the ceiling's y-coordinate plus half of it's height plus half of mario's height
        setLocation(getX(), newY); // set mario's y-coordinate to be exactly below the ceiling
    }

    /**
     * Checks to see if there is wall to the right of Mario.
     * If there is, blocks Mario from passing.
     * Uses stopByRightWall method.
     * @return true - there is a wall to the right, false- there isn't a wall to the right
     */
    public boolean checkRightWalls()
    {
        int spriteWidth = getImage().getWidth(); // save mario's width
        int xDistance = (int)(spriteWidth/2); // the distance (graphically speaking) between mario and any wall (to the right)
        Block rightWall = (Block) getOneObjectAtOffset(xDistance, 0, Block.class); // save the wall that is located to the right of mario

        if(rightWall == null) // if a wall does not exsist, then
        {
            return false; // perform nothing
        }
        else  // otherwise, 
        {
            stopByRightWall(rightWall); // blocks mario from passing the wall
            return true;
        }

    }

    /**
     * Blocks Mario from passing the wall to the right of him.
     * @param rightWall - the wall to the right of Mario
     */
    public void stopByRightWall(Actor rightWall)
    {
        int wallWidth = rightWall.getImage().getWidth(); // save the width of the wall
        int newX = rightWall.getX() - (wallWidth + getImage().getWidth())/2; // the x-coordinate for mario to be exactly to the left of the wall (graphically speaking) is the wall's x-coordinate minus half of it's width minus half of mario's width
        setLocation(newX - 5, getY()); // set Mario's location to be exactly 5 pixels to the left of the wall

    }

    /**
     * Checks to see if there is wall to the left of Mario.
     * If there is, blocks Mario from passing.
     * Uses stopByLeftWall method.
     * @return true - there is a wall to the left, false- there isn't a wall to the left
     */
    public boolean checkLeftWalls()
    {
        int spriteWidth = getImage().getWidth(); // save mario's width
        int xDistance = (int)(spriteWidth/-2); // the distance (graphically speaking) between mario and any wall (to the left)
        Actor leftWall = getOneObjectAtOffset(xDistance, 0, Block.class); // save the wall that is located to the left of mario

        if(leftWall == null) // if a wall does not exsist, then
        {
            return false; // perform nothing
        }
        else // otherwise, 
        {
            stopByLeftWall(leftWall); // blocks mario from passing the wall
            return true;
        }

    }

    /**
     * Blocks Mario from passing the wall to the right of him.
     * @param rightWall - the wall to the right of Mario
     */
    public void stopByLeftWall(Actor leftWall)
    {
        int wallWidth = leftWall.getImage().getWidth(); // save the width of the wall
        int newX = leftWall.getX() + (wallWidth + getImage().getWidth())/2; // the x-coordinate for mario to be exactly to the left of the wall (graphically speaking) is the wall's x-coordinate plus half of it's width plus half of mario's width
        setLocation(newX + 5, getY()); // set Mario's location to be exactly 5 pixels to the right of the wall
    }

    private Teleporter teleporter; // saves the teleporter that mario is on
    /**
     * Moves Mario according the to teleporter's movement that Mario is on.
     */
    public void carrying (){
        teleporter = (Teleporter) getOneIntersectingObject(Teleporter.class); // save the teleporter that mario is on
        if (teleporter != null){ // if the teleporter exsists, then
            if(!teleporter.getNormal())
            {
                if (teleporter.movingXDirection()){ // if the teleporter is movingin the x-direction, then
                    setLocation (getX()+teleporter.getSpeed(), getY()); // move Mario in the x-direction (distance is equal to the teleporter's horizontal speed)
                } else if (teleporter.movingYDirection()){ // if the teleporter is movingin the y-direction, then
                    setLocation (getX(), getY()+teleporter.getSpeed()); // move Mario in the y-direction (distance is equal to the teleporter's vertical speed)
                }
            }
            else 
                setLocation (getX()+teleporter.getSpeed(), getY());
        }
    }

    private Tree tree; // saves the tree that mario is on

    private void checkTree(){
        getWorld().setPaintOrder(Mario.class); // mario appears over the tree
        onTree =false; // by default mario is currently not on the tree (will change if he is on)
        tree = (Tree) getOneObjectAtOffset(0, 0, Tree.class); // save the tree that mario is on
        if (tree != null){ // if the tree exists, then
            onTree=true;  // mario is on a tree
            jumping =false;  // mario is not airbourne
            if (this.getY()+getImage().getHeight()/2 != tree.getY()+tree.getImage().getHeight()/2){ // if mario is on the tree (excluding when he stands infront of the tree), then
                if (lockMario ==false){ // if mario jumps on the tree - change image
                    this.setImage (new GreenfootImage ("climb1.png")); 
                }
                if (turnLockOff ==false){
                    lockMario =true;
                } else{
                    lockMario =false;
                }
            }
            lockOnTree();
        } else {

            lockMario =false;
            turnLockOff =false;
        }

    }

    private boolean lockMario=false;  // can mario let go of the treen, true- he can, false- he can't
    /**
     * Lock Mario on the tree.
     */
    public void lockOnTree(){
        // if mario is on a tree and is suppose to be locked on it (usually when left/right arrow) pressed (to avoid glitches)
        if (onTree ==true && lockMario ==true){
            setLocation (tree.getX(), getY());  // mario maintains the same x-value throughout

        }

    }

    /**
     * Sets Mario's vertical speed.
     * Called by Jumper class.
     * @param vSpeed - the new vertical speed 
     */
    public void setSpeed (int vSpeed){
        this.vSpeed = vSpeed; // the current vertical speed becomes the new vertical speed
    }

    /**
     * Returns the current jump strength of Mario
     * @return - the value of the jump strength of Mario
     */
    public int getJumpStrength(){
        return this.jumpStrength;
    }

    /**
     * Gets the lives of Mario
     */
    public int getLives(){
        return lives;
    }

    /**
     * Mario loses a life. If it was his last life, he goes to the title page
     */
    public void loseALife(){
        Greenfoot.playSound("shrinking.wav");
        if (lives > 1)
            lives--;
        else 
        {
            DanielWorld dw = (DanielWorld)getWorld();
            dw.stopMusic();
            DanielWorld finished = new DanielWorld();
            Coin.setCoinNumber (0);
            Greenfoot.setWorld(finished);
        }
    }

    // variables that store the ARGB values at a given pixel
    private static int alpha;
    private static int red;
    private static int green; 
    private static int blue; 

    private BufferedImage temp;
    private int delay;
    /**
     * Animation runs to indicate Mario is under the Invincible efect.
     */
    private void invincibleAnimation (){
        if (invincibleAnimation == true){
            if (delay% 7==0){
                temp = this.getImage().getAwtImage();
                BufferedImage bi = deepCopy(temp);

                // Get image size to use in for loops
                int xSize = bi.getWidth();
                int ySize = bi.getHeight();
                // loop through every pixel in the given buffered image using size as limit
                for (int x = 0; x < xSize; x++)
                {
                    for (int y = 0; y < ySize; y++)
                    {

                        setRGBValues(bi, x,y);   // store the RGB values in that pixel in the red,blue, green variables

                        // assign temporary variables to store the original rgb colors for each pixel
                        int tempRed=red;  
                        int tempBlue=blue;
                        int tempGreen=green;

                        red= tempGreen;   // red gets green value
                        blue =tempRed;   // blue gets red value
                        green=tempBlue;  // green gets blue value

                        // this also works as a loop
                        // every time this method runs, each rgb value will be assigned a different value 
                        // eventually each rgb value will be assigned the each rgb value

                        // packs pixels to be placed into the buffered image that was initially used.
                        int newColour = packagePixel (red, green, blue, alpha);

                        // draws the pixels in the buffered image
                        bi.setRGB (x, y, newColour);
                    }
                }

                GreenfootImage newImage = createGreenfootImageFromBI (bi);
                setImage (newImage);
            }
            delay++;
        }
    }

    /**
     * Returns a new unrelated buffered image of the buffered image that was passed by reference.
     * Called by the addImage method.
     * 
     * @param bi    The BufferedImage (passed by reference) to change.
     * @return  BufferedImage Returns the BufferedImage that is used for undoing.
     */
    private static BufferedImage deepCopy(BufferedImage bi)
    {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultip = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultip, null);
    }

    /**
     * Stores the RGB values in RGB variables based on the given pixel.
     * 
     * @param bi - buffered image the brownish layer will be add on
     * @param x - x-coordinate of the pixel
     * @param y - y-coordinate of the pixel
     */
    private static void setRGBValues(BufferedImage bi, int x, int y){
        // Calls method in BufferedImage that returns R G B and alpha values
        // encoded together in an integer
        int rgb = bi.getRGB(x, y);

        // Call the unpackPixel method to retrieve the four integers for
        // R, G, B and alpha and assign them each to their own integer
        int[] rgbValues = unpackPixel (rgb);
        alpha = rgbValues[0];
        red = rgbValues[1];
        green = rgbValues[2];
        blue = rgbValues[3];    

    }

    /**
     * Takes in an rgb value - the kind that is returned from BufferedImage's
     * getRGB() method - and returns 4 integers for easy manipulation.
     * 
     * By Jordan Cohen
     * Version 0.2
     * 
     * @param rgbaValue The value of a single pixel as an integer, representing<br>
     *                  8 bits for red, green and blue and 8 bits for alpha:<br>
     *                  <pre>alpha   red     green   blue</pre>
     *                  <pre>00000000000000000000000000000000</pre>
     * @return int[4]   Array containing 4 shorter ints<br>
     *                  <pre>0       1       2       3</pre>
     *                  <pre>alpha   red     green   blue</pre>
     */
    public static int[] unpackPixel (int rgbaValue)
    {
        int[] unpackedValues = new int[4];
        // alpha
        unpackedValues[0] = (rgbaValue >> 24) & 0xFF;
        // red
        unpackedValues[1] = (rgbaValue >> 16) & 0xFF;
        // green
        unpackedValues[2] = (rgbaValue >>  8) & 0xFF;
        // blue
        unpackedValues[3] = (rgbaValue) & 0xFF;

        return unpackedValues;
    }

    /**
     * Takes in a red, green, blue and alpha integer and uses bit-shifting
     * to package all of the data into a single integer.
     * 
     * @param   int red value (0-255)
     * @param   int green value (0-255)
     * @param   int blue value (0-255)
     * @param   int alpha value (0-255)
     * 
     * @return int  Integer representing 32 bit integer pixel ready
     *              for BufferedImage
     */
    public static int packagePixel (int r, int g, int b, int a)
    {
        int newRGB = (a << 24) | (r << 16) | (g << 8) | b;
        return newRGB;
    }

    /**
     * Converts a buffered image to a greenfoot image.
     * 
     * @param newBi- buffered image to be converted
     * @return GreenfootImage - a greenfoot image of the buffered image
     */
    public static GreenfootImage createGreenfootImageFromBI (BufferedImage newBi)
    {
        GreenfootImage returnImage = new GreenfootImage (newBi.getWidth(), newBi.getHeight());
        BufferedImage backingImage = returnImage.getAwtImage();
        Graphics2D backingGraphics = (Graphics2D)backingImage.getGraphics();
        backingGraphics.drawImage(newBi, null, 0, 0);        

        return returnImage;
    }

    public void setInvincibleAnimationOn(){
        invincibleAnimation =true;
    }

    public void setInvincibleAnimationOff(){
        invincibleAnimation =false;
    }

    //new from here
    public InventoryObject checkHit(){
        InventoryObject actor = (InventoryObject) getOneObjectAtOffset(0,2,InventoryObject.class);   
        if (actor != null){
            return actor; 
        }
        return null;
    }

    public void fly()
    {
        usingPower = true;
        wings = new Wings(this);
        getWorld().addObject(wings,this.getX(),this.getY());
        status  = new Status(400,400,this,"Flight");
        getWorld().addObject(status,this.getX(),this.getY());
        acceleration = 0;
        inFlight = true;
    }

    /**
     * Only valid for flight
     */
    public void moveInFlight()
    {
        if (inFlight == true)
        {
            if (Greenfoot.isKeyDown("down"))
            {
                setLocation(getX(),getY()+2);
            }
            if (Greenfoot.isKeyDown("up"))
            {
                setLocation(getX(),getY()-2);
            }
            if (onGround() == false && Greenfoot.isKeyDown("left"))
            {
                GreenfootImage temp= new GreenfootImage (character [0]);
                temp.mirrorHorizontally();
                this.setImage (temp);
            }
            if (onGround() == false && Greenfoot.isKeyDown("right"))
            {
                GreenfootImage temp= new GreenfootImage (character [0]);
                this.setImage (temp);
            }
            if (status.getCurrentCountDown() == 0)
            {
                resetPowers();
            }
        }
    }

    public void fireAbility()
    {
        usingPower = true;
        onFire = true;
        status  = new Status(1000,1000,this,"Fire Power");
        getWorld().addObject(status,this.getX(),this.getY());
    }

    private void fireActions()
    {
        if (onFire == true)
        {
            if ("f".equals(Greenfoot.getKey()))
            {                
                if(facingRight == true)
                {
                    getWorld().addObject(new FireBall(true),getX(),getY());
                }
                else 
                {
                    getWorld().addObject(new FireBall(false),getX(),getY());
                }                 
            }            
            if (status.getCurrentCountDown() == 0)
            {
                resetPowers();
            }
        }
    }

    public void openChasm()
    {
        usingPower = true;
        chasmOpen = true;
        status  = new Status(500,500,this,"Black Hole");
        getWorld().addObject(status,this.getX(),this.getY());
        chasm = new Chasm(this);
        getWorld().addObject(chasm,getX(),getY() - 110);
    }

    private void chasmActions()
    {
        if (chasmOpen == true)
        {
            if (status.getCurrentCountDown() == 0)
            {
                resetPowers();
                chasm.removeChasm();
            }
        }
    }

    /**
     * Ramy this is the invinicbility method that I call from the inventory class
     */
    public void turnInvincible()
    {
        music.playLoop();  // play music       
        setInvincibleAnimationOn();      
        isInvincible = true;
        status  = new Status(600,600,this,"Invincibility");
        getWorld().addObject(status,this.getX(),this.getY());
    }

    public boolean isInvincible (){
        if (isInvincible ==true){
            return true;
        }
        return false;
    }

    /**
     * Ramy this method is a method that can be used to manipulate mario while he is invincible
     * it is also called int he act() method above, IT MUST BE HERE FOR THIS TO WORK
     */
    public void invinciblePowers()
    {
        if (isInvincible == true)
        {
            if (status.getCurrentCountDown() == 0)
            {
                resetPowers();                
            }
        }
    }

    public void resetPowers()
    {
        //for flight
        usingPower = false;
        acceleration = 1;
        if (inFlight == true)
        {
            getWorld().removeObject(wings);
        }
        inFlight = false;
        onFire = false;
        chasmOpen = false;
        if (isInvincible == true)
        {
            music.stop();            
        }
        isInvincible = false;
        setInvincibleAnimationOff();
    }

    public boolean getUsingPower()
    {
        return usingPower;
    }

    private void checkLives(){

    }
    private List numberOfLives;
    public void updateLives(){
        numberOfLives=  getWorld().getObjects (Heart.class);
        for (int i =0; i<numberOfLives.size();i++){
            Actor heart = (Actor) numberOfLives.get(i);
            getWorld().removeObject (heart);
        }
        if (this.lives ==1){
            getWorld().addObject (new Heart(), getWorld().getWidth()-30, 50);
        } else if (this.lives ==2){
            getWorld().addObject (new Heart(), getWorld().getWidth()-30, 50);
            getWorld().addObject (new Heart(), getWorld().getWidth()-60, 50);
        }else if (this.lives ==3){
            getWorld().addObject (new Heart(), getWorld().getWidth()-30, 50);
            getWorld().addObject (new Heart(), getWorld().getWidth()-60, 50);
            getWorld().addObject (new Heart(), getWorld().getWidth()-90, 50);
        }else if (this.lives ==4){
            getWorld().addObject (new Heart(), getWorld().getWidth()-30, 50);
            getWorld().addObject (new Heart(), getWorld().getWidth()-60, 50);
            getWorld().addObject (new Heart(), getWorld().getWidth()-90, 50);
            getWorld().addObject (new Heart(), getWorld().getWidth()-120, 50);
        }else if (this.lives ==5){
            getWorld().addObject (new Heart(), getWorld().getWidth()-30, 50);
            getWorld().addObject (new Heart(), getWorld().getWidth()-60, 50);
            getWorld().addObject (new Heart(), getWorld().getWidth()-90, 50);
            getWorld().addObject (new Heart(), getWorld().getWidth()-120, 50);
            getWorld().addObject (new Heart(), getWorld().getWidth()-150, 50);
        }

    }
}