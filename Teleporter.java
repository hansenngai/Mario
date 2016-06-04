import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class teleporter here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Teleporter extends Block
{
    int speed = 2;
    private boolean normal;
    Characters actor;
    int x1, y1, x2,  y2;
    public Teleporter(int x2, int y2){

        this.x2=x2;
        this.y2=y2;
        normal = false;
    }

    public Teleporter()
    {
        normal = true;
    }

    public void addedToWorld(World world){
        this.x1= getX();
        this.y1= getY();

        this.x2= x2 +getX();
        this.y2= y2 +getY();

    }

    /**
     * Act - do whatever the teleporter wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {

        if (normal)
        {
            checkWalls();
            setLocation (getX()+speed, getY());
        }
        else
            direction (x1, y1, x2, y2);
    }  

    private boolean xCheck =false;
    private boolean yCheck =false;
    private boolean reverse =false;
    public void direction (int x1, int y1, int x2, int y2){
        if (reverse ==true){
            int tempX1 = x1;
            x1=x2;
            x2= tempX1;

            int tempY1 = y1;
            y1=y2;
            y2= tempY1;
        }

        if (getX() > x2){
            speed =-Math.abs(speed);
            move (speed);
        } else if (getX() < x2){
            speed=Math.abs(speed);
            move (speed);
        } else {
            xCheck =true;
        }

        if (getY() > y2){
            speed =-Math.abs(speed);
            setLocation (getX(), getY()+speed);
        } else if (getY() < y2){
            speed=Math.abs(speed);
            setLocation (getX(), getY()+speed);
        } else {
            yCheck =true;
        }

        if (xCheck ==true && yCheck ==true){
            if (reverse ==true){
                reverse =false;
            } else {
                reverse =true;
            }
            xCheck=false;
            yCheck=false;
        }
    }

    private void checkWalls()
    {
        Actor Wall = getOneIntersectingObject(Block.class);
        if(Wall != null)
            speed *= -1;
    }

    public int getX2(){
        return x2-getX();
    }

    public int getY2(){
        return y2-getY();
    }

    public void setX2(int x2){
        this.x2= getX()+x2;
    }

    public void setY2(int y2){
        this.y2= getY()+y2;
    }

    public int getSpeed(){
        return speed;
    }
    
    public boolean getNormal()
    {
        return normal;
    }

    public boolean movingYDirection(){
        if (getX() == x2){
            return true;
        }
        return false;
    }

    public boolean movingXDirection(){
        if (getY() == y2){
            return true;
        }
        return false;
    }

}
