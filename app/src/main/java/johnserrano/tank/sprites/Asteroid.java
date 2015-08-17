package johnserrano.tank.sprites;

import android.graphics.Bitmap;

abstract class Asteroid extends Mob {
    Asteroids parentList;
    boolean starting;
    boolean destroy;
    boolean broken;
    double starty;
    double startx;
    double velocityXbreak;
    double velocityYbreak;

    public Asteroid(Asteroids a, Bitmap b, double x, double y, double velocityx, double velocityy)
    {
        parentList = a;
        destroy = false;
        broken = false;

        bitmap = b;
        this.x = x;
        this.y = y;
        starty = y;
        startx = x;
        starting = true;

        velocityXbreak = 0;
        velocityYbreak = 0;

        speed = new Speed(velocityx, velocityy);
    }

    public void update()
    {
        //for some reason all asteroids are moving at the same velocity
        x += speed.getVelocityX() * speed.getXDirection();
        y += speed.getVelocityY() * speed.getYDirection();

        if (Math.abs(startx - x) > bitmap.getWidth() || Math.abs(starty - y) > bitmap.getHeight())
            notStarting();

        if (broken) {
            doBreak((int) x, (int) y, velocityXbreak, velocityYbreak);
        }

    }

    abstract void doBreak(int x, int y, double xVelocity, double yVelocity);

    abstract int getValue();
    /**
     * implement this method to return the
     * value of the asteroid
     */

    public boolean getStarting()
    {
        return starting;
    }

    public void notStarting()
    {
        starting = false;
    }

    public boolean getDestroy()
    {
        return destroy;
    }

    public void setDestroy(boolean d)
    {
        destroy = d;
    }
}