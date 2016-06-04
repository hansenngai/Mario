import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
/**
 * Write a description of class Key here.
 * 
 * @author (Ramy Elbakari) 
 * @version (June 12, 2015)
 */
public class Key extends Collectables
{
    /**
     * Act - do whatever the Key wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
        checkHit();  // check if mario collects the key
    }    

    private List doors; // store all the doors in the world
    private double distance = 0; // stores the distance between the key and any door
    private double closestDistance =999999;  // stores the closest distance between a key and a door (starts high, keeps getting lower)
    private Door closestDoor = new Door(); // store the closest door to the key

    
    /**
     * If Mario touches the key, it opens the closest door to this key.
     */
    public void checkHit(){
        
        
        if (getOneIntersectingObject (Mario.class)!= null){ // if mario intersects the key
            
            nearestDoor(); // locate nearest door
            closestDoor.open(); // open nearest door
            getWorld().removeObject (this); // remove this key
        }

    }

    /**
     * Saves the closest closed door to the key in the closestDoor variable.
     */
    public void nearestDoor(){
        doors = getWorld().getObjects (Door.class); // save all the doors in the world

        for (int i =0; i<doors.size();i++){ // loop through all the doors
            Door door = (Door) doors.get(i); // store the current door that the loop is on
            if (door.checkOpen()){  // if the door is already open,
                doors.remove (i); // remove the door from the lise (only looking for closed doors)
            }

        }

        for (int i =0; i<doors.size();i++){  // loop through all the closed doors
            Door door = (Door) doors.get(i);  // store the current door that the loop is on
            distance = calculateNearestActor (door); // calculate the distance between this key the current door

            if (distance< closestDistance){  // if the current door's distance is less than the smallest known distance, then
                closestDistance = distance;  // store the closest distance
                closestDoor = door;  // store the closest door
            }
        }

    }

    /**
     * Calculates the distance between the specified actor and this unit.
     * Gets called by every class for numerous reasons.
     * @param actor- the unit in which the distance between it and this unit will be measured
     */
    private double calculateNearestActor(Actor actor){
        return Math.hypot(actor.getX() - getX(), actor.getY() - getY());  // calculate the nearest ("specified") actor to this unit using pythagreom theory
    }
}
