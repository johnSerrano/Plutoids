package johnserrano.tank.sprites;

class Speed
{
    public static final int RIGHT = 1;
    public static final int LEFT = -1;
    public static final int UP = -1;
    public static final int DOWN = 1;

    private double velocityX;
    private double velocityY;

    private int xDirection = RIGHT;
    private int yDirection = DOWN;

    public Speed(double velocityX, double velocityY)
    {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    public Speed(double velocityX, double velocityY, int xDirection, int yDirection)
    {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        setXDirection(xDirection);
        setYDirection(yDirection);
    }


    public double getVelocityX()
    {
        return velocityX;
    }

    public double getVelocityY()
    {
        return velocityY;
    }

    public void setVelocityX(double x)
    {
        velocityX = x;
    }

    public void setVelocityY(double y)
    {
        velocityY = y;
    }

    public int getYDirection()
    {
        return yDirection;
    }

    public int getXDirection()
    {
        return xDirection;
    }

    public void setYDirection(int y)
    {
        //no action on y == 0
        if (y > 0) yDirection = DOWN;
        else if (y < 0) yDirection = UP;
    }

    public void setXDirection(int x)
    {
        //no action on y == 0
        if (x > 0) xDirection = LEFT;
        else if (x < 0) xDirection = RIGHT;
    }
}