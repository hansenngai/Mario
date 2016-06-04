import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
import java.util.List;
import java.lang.Object;
/**
 * The status class is one of the most important game feature in modern gaming society. 
 * Status is in charge of the special effects that takes place for the abilities that a game character may have or apply to another character.
 * Some feature of this status include different status that can be applied to the target such as "Invincibility, Fire Power, Flight and Black Hole", each with a unique color assigned to it.
 * <p>
 * - Once an actor is set for the status to attach, the status would run as regularly, as the status would appear on top of the target and follow the actor until the status duration runs out and the status gets removed.
 * <p>
 * - When there is no target assigned to the status, the status would be for testing purpose only. During testing, we can create input for the update method to change the duration of the status bar.
 * 
 * Status is very useful to keep track of interations through a duration of time.
 * 
 * @author (Daniel Chen) 
 * @version (Feburary 23rd, 2015)
 */
public class Status extends Actor
{

    //  From this Assignment I learned that having a constructor that can override allows the ability to create the same instance of an object that does something different. 
    //  For the Status bar for example, normally, in a game where it is interacting with other actors, it would only show up on top of another object and decreases its duration until it is gone and then deleted.
    //  But with more than one constructors, I can also create a instance of Status without and actor where then I can add keyboard inputs in world class to adjust it and allow to adjustment of small details.
    //   
    // 
    //  Through the testing of this widget, I found out that stand alone codes like the one we created are very, very useful in coding. It doesn't have to need other classes to function, and while it is running it wouldn't affect the interation of other classes. 
    //  Additionally, I also find that documenting code are alot more easy to use codes rather than having to read the other person's code and try to figure out how everything works. I'd just have to read the instructions and follow it.
    //  Overall, this assignment was very helpful in my understanding of stand alone widgets and helps me to more clearly organize my code in different areas on my classes, and not just put random codes everywhere.
    
    
    private GreenfootImage myImage;         //Create a Greenfoot Image called myImage.
    private GreenfootImage bar;             //Create a Greenfoot Image called bar.
    private GreenfootImage blank;           //Create a Greenfoot Image called blank.

    private final int OFFSET = 40;         
    private final int MY_IMAGE_WIDTH = 100;
    private final int MY_IMAGE_HEIGHT = 50;
    private final int BAR_HEIGHT = 5;

    private int barWidth = 98;
    private int currBarWidth;               //Keep track of current bar width;

    private int maxCountDown;               //Keep track of maximum countdown of status duration.
    private double percentCountdown;        //A double variable to keep track of percent countdown of status duration.
    private int currCountDown;              //Keep track of current countdown of status duration.

    private int percentDisplay;             //Keep track of percent countdown of status duration as a interger.
    private String percentCounter;          

    Color barColor;                         

    private boolean selection = true;

    private String status;

    private Actor target;
    /**
     * Create 3 Greenfoot image, myImage (Which is where the status is displayed on), bar (Which is the count down bar for status duration), blank(Which is when status duration is done) 
     * Set the image for Actor as myImage.
     */
    public Status ()

    {
        myImage = new GreenfootImage(MY_IMAGE_WIDTH,MY_IMAGE_HEIGHT);           // Initialize the height and width of myImage
        currBarWidth = barWidth;                                                // set barWidth as current bar Width
        bar = new GreenfootImage(currBarWidth,BAR_HEIGHT);                      // Initialize the height and width of bar
        blank = new GreenfootImage(1,1);                                        // Initialize the height and width of blank 
        this.setImage(myImage);                                                 // set the image of this Actor class as myImage
    }

    /**
     * Set the status attribute
     * 
     * @param
     *   stts status(available string words are : "Flight", "Fire Power", "Black Hole", "Invincibility".
     */
    public Status (String stts)

    {
        this();
        this.status = stts;                                     // initialize the status
        update();                                               // draws the status on myImage
    }

    /**
     * Initialize the current countdown duration, and max count down with the status.
     * @param
     *          maxCountDown Maximum countdown duration.
     *          currCountDown Current countdown duration
     *          stts status(available string words are : "Flight", "Fire Power", "Black Hole", "Invincibility".
     */
    public Status (int maxCountDown, int currCountDown, String stts)

    {
        this(stts);
        this.maxCountDown = maxCountDown;               //Initialize maximum countdown duration
        this.currCountDown = currCountDown;             //Initialize current countdown duration
        update();                                       // draws the status on myImage
    }

    /**
     * Initialize the target for the Status to follow
     * @param
     *          maxCountDown Maximum countdown duration.
     *          currCountDown Current countdown duration
     *          target The target status is attached to
     *          stts Status(available string words are : "Flight", "Fire Power", "Black Hole", "Invincibility".
     */
    public Status (int maxCountDown, int currCountDown, Actor target, String stts)

    {
        this(maxCountDown,currCountDown,stts);
        this.target = target;                           // set the target that the status is attached to
        this.selection = false;                         // Make the status an actual countdown
        update();                                       // draws the status on myImage
    }

    /**
     * Draws status bar for Flight
     * 
     */
    private void drawFire()

    {
        myImage.setColor(Color.BLUE);                   //Set the color of myImage red
        myImage.drawString("Flight", 10,30);          //display text "Flight" at a location on myImage
        bar.setColor(Color.BLUE);                       //set the color of the image bar as red
        bar.fill();                                    //fill the image bar with red
    }

    /**
     * Draws status bar for Fire Power
     * 
     */
    private void drawIce()

    {   myImage.setColor(Color.RED);                   //Set the color of myImage blue
        myImage.drawString("Fire Power", 10,30);            //display text "Fire Power" at a location on myImage
        bar.setColor(Color.RED);                       //set the color of the image bar as blue
        bar.fill();                                     //fill the image bar with blue
    }

    /**
     * Draws status bar for Invincibility
     * 
     */
    private void drawStun()

    {
        myImage.setColor(Color.YELLOW);
        myImage.drawString("Invincibility", 10,30);
        bar.setColor(Color.YELLOW);
        bar.fill();
    }

    /**
     * Draws status bar for Black Hole
     * 
     */
    private void drawPoison()

    {
        myImage.setColor(Color.BLACK);
        myImage.drawString("Black Hole", 10,30);
        bar.setColor(Color.BLACK);
        bar.fill();
    }

    /**
     * depending on the status from attribute, displays the current status.
     * 
     */
    public void update()

    {
        if (this.status == "Flight")
            drawFire();                             //draw the image for Flight
        if (this.status == "Fire Power")
            drawIce();                              //draw the image for Fire Power
        if (this.status == "Invincibility")
            drawStun();                             //draw the image for Invincibility
        if (this.status == "Black Hole")  
            drawPoison();                           //draw the image for Black Hole
        drawBar();                                  // draw the bar image on myImage
    }

    /**
     * Draws the count down bar for status duration
     * 
     */
    private void drawBar()

    {
        myImage.drawImage(bar,0 , 40) ;         // draws the bar image on myImage
    }

    /**
     * Decreases the count down value of status duration, bar width and displays the percentate of status duration left
     * If there is a target, When status duration reaches zero, the status disappears.
     * @param
     *      change The Change in status duration value each Act
     */
    public void update(int change)

    {
        if (currCountDown > 0)
        {
            currCountDown -= change;                                            // decreases the current countdown by the change value
            percentCountdown = (double) currCountDown/maxCountDown;             // set a double value of percent countdown by dividing current countdown by maximum countdown
            currBarWidth = (int) (percentCountdown * barWidth);                 // set a interger value of current bar width by multiplying percent countdown and bar width and casting them into a interger value
            percentDisplay = (int) (percentCountdown * 100);                    // set a interget value of percentDisplay by multiplying percentCountdown by 100.
            percentCounter = Integer.toString(percentDisplay);                  // cast the interger percentDisplay into a string and store it in percentCouner
            myImage.clear();                                                    // clears the image
            myImage.drawString(percentCounter + "%", 70,30);                    // draws the percentCounter on myImage
            if(currBarWidth > 1)
                bar.scale(currBarWidth,BAR_HEIGHT);                             // rescale the bar size
            if (selection == true)
                if (currCountDown == 1)
                    currCountDown += 1;
            update();                                                           // redraw the image
        }
        if (selection == false)                                         
            if (currCountDown == 0)                                     // if current countdown is 0
                removeStatus();                                         // remove status
    }

    /**
     * Removes the status
     * 
     */
    private void removeStatus()
    {
        this.setImage(blank);                                               // remove image
    }

    /**
     * If a target exist, status would appear above the target until the count down duration is 0.
     * If target is removed the status is removed as well.
     * 
     */

    public void act() 
    {
        if(target != null)                                                      // if a target is assigned to a status
        {
            if (target.getWorld() != null)                                      
            {
                setLocation (target.getX(), target.getY() - OFFSET);            // offset myImage above the actor 
                update(1);                                                      // update the image with the actual countdown(not controlled by keyboard)
            }
            else
                removeStatus();                                                  //remove status along with actor
        }
    }    

    /**
     * @return int  Returns current countdown value
     * 
     */
    public int getCurrentCountDown()
    {
        return currCountDown;                   //return current countdown value
    }

    /**
     * @return int Returns max countdown value
     * 
     */
    public int getMaxCountDown()
    {
        return maxCountDown;                    // return maximum countdown value
    }

}
