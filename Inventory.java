import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Inventory here.
 * 
 * @author Isaac Chang 
 * @version 0.0.1
 */
public class Inventory extends Actor
{
    //instance variables
    private int moveSpeed = 0;
    private boolean checkOpen = false;
    private int[] objects;
    private GreenfootImage[] objectPics;
    private Mario target;
    private GreenfootImage basePic;
    private int timerTime;

    private int space;

    public Inventory(Mario target)
    {
        objects = new int[5];
        this.target = target;
        space = 0;
        objectPics = new GreenfootImage[5];

        basePic = new GreenfootImage("inventorynew.fw.png");

        for (int i=0 ; i<5 ; i++)
        {
            objects[i] = 0;
        }
    }

    /**
     * Act - do whatever the Inventory wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        setLocation(getX(), getY() + moveSpeed);      

        checkClicked();
        checkStop();    

        checkTouchObject();
        useObject();
    } 

    private void checkTouchObject()
    {
        InventoryObject testObj = target.checkHit();
        //to prevent over running
        if (testObj != null)
        {           
            for (int i=0 ; i<5 ; i++)
            {
                //whichever open space comes first will be filled
                if(objects[i] == 0)
                {
                    Greenfoot.playSound("1up.wav");
                    objects[i] = testObj.getTypeIndex();
                    objectPics[i] = testObj.getImage();                   
                    getWorld().removeObject(testObj);
                    drawObjects();
                    return;
                }                
            }       

        }
    }

    private void drawObjects()
    {
        space = 0;
        basePic = new GreenfootImage("inventorynew.fw.png");
        for (int i=0 ; i<5 ; i++)
        {
            try
            {               
                basePic.drawImage(objectPics[i],40+space,(basePic.getHeight()/4)+5);
            }
            catch (NullPointerException e)
            {
            }
            space = space + 44;
        }
        this.setImage(basePic);
    }

    private void removeObject(int index)
    {
        //call mario method that corresponsds to index        
        Greenfoot.playSound("powerup.wav");
        switch (objects[index])
        {
            case 1:
            target.fireAbility();
            break;

            case 2:
            target.turnInvincible();
            break;

            case 3:
            target.fly();
            break;
            
            case 4:
            target.openChasm();
            break;
        }
        objects[index] = 0;
        objectPics[index] = null;
        drawObjects();
    }

    private void useObject()
    {
        if (target.getUsingPower() == false)
        {
            if (Greenfoot.isKeyDown("1"))
            {
                removeObject(0);
            }
            if (Greenfoot.isKeyDown("2"))
            {
                removeObject(1);
            }
            if (Greenfoot.isKeyDown("3"))
            {
                removeObject(2);
            }
            if (Greenfoot.isKeyDown("4"))
            {
                removeObject(3);
            }
            if (Greenfoot.isKeyDown("5"))
            {
                removeObject(4);
            }
        }
    }

    private void open()
    {      
        moveSpeed = 4;
    }

    private void close()
    {
        moveSpeed = -4;
    }

    private void checkStop()
    {
        if (getY() >= 67 && checkOpen == false)
        {
            moveSpeed = 0;
            checkOpen = true;
        }
        if (getY() <= -23 && checkOpen == true)
        {
            moveSpeed = 0;
            checkOpen = false;
        }
    }

    private void checkClicked()
    {
        if (Greenfoot.isKeyDown("i") && checkOpen == false)
        {
            open();
        }
        else if (Greenfoot.isKeyDown("i") && checkOpen == true)
        {
            close();
        }
    }

}
