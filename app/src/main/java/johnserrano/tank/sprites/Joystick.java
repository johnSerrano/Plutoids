package johnserrano.tank.sprites;

/**
 * This class handles the joystick, including its effect on the ship class
 */

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

public class Joystick implements Touchable
{
    Ship ship;
    Paint paint;
    Paint indicatorPaint;
    Point size;

    private double ytoset;
    private double xtoset;
    private double xvelocitytoset;
    private double yvelocitytoset;

    private int lastx;
    private int lasty;

    private double theta;
    private double thetaget;

    private boolean touched;
    private boolean setdirection;

    public Joystick(Ship ship, Point size)
    {
        this.ship = ship;
        this.size = size;

        setdirection = false;
        touched  = false;

        setupPaint();

        lastx = 150;
        lasty = size.y - 150;

        //these values are initialized so the projectiles go the correct direction
        //before the joystick is touched
        //if you want to change the starting direction of the ship,
        //be sure to change this as well
        theta = Math.PI / 2.0;
        thetaget = Math.PI;
    }

    //these two functions are not good code
    //the XY or YX refers to the order in which
    //parameters are passed to atan2
    //This is the result of trial and error to get
    //the various projectiles, ships, etc. to rotate
    //correctly
    public double getThetaXY()
    {
        return theta;
    }

    public double getThetaYX()
    {
        return thetaget;
    }

    public void setupPaint()
    {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.GRAY);
        paint.setAntiAlias(true);
        indicatorPaint = new Paint();
        indicatorPaint.setStyle(Paint.Style.FILL);
        indicatorPaint.setColor(Color.BLUE);
        indicatorPaint.setAntiAlias(true);
    }

    public void update()
    {
        if (setdirection) {
            ship.getSpeed().setXDirection((int) xtoset);
            ship.getSpeed().setYDirection((int) ytoset);
            ship.getSpeed().setVelocityX(xvelocitytoset);
            ship.getSpeed().setVelocityY(yvelocitytoset);
            setdirection = false;
        }
    }

    public void handleActionDown(int x, int y)
    {
        //if touched in circle, begin moving tank
        if (Math.abs(150 - x) < 100 && Math.abs((size.y -150) - y) < 100) {
            //indicate tank should be moved
            setdirection = true;
            touched = true;
            ytoset = 0-(size.y - 150 - y);
            xtoset = 150 - x;

            xvelocitytoset = 2.0 * 150.0 / (150.0 - Math.abs(150.0 - lastx));
            yvelocitytoset = 2.0 * 150.0 / (150.0 - Math.abs((size.y - 150.0) - lasty));

            //snap onto a straight line
            if (Math.abs(150.0 - lastx) < 10)
                xvelocitytoset = 0;
            if (Math.abs((size.y - 150.0) - lasty) < 10)
                yvelocitytoset = 0;

            lastx = x;
            lasty = y;

            //get angle to dot
            try {
                //atan2 is not atan squared, its a better atan
                theta = Math.atan2((150 - x), (size.y - 150 - y));
                //see annotation above getTheta functions
                thetaget = Math.atan2((size.y - 150 - y), (150 - x));
            } catch (ArithmeticException e) {
                //at just the wrong angle we get a divide by zero exception
                theta = 0;
            }


            //converting to degrees and posting to tank
            ship.setAngleOfRotation((float) (theta * 180 / Math.PI));
        }
    }

    public void handleActionMove(int x, int y)
    {
        //if touch is inside circle (pythagorean)
        if(Math.abs((150-x)*(150-x)) + Math.abs((size.y - 150 - y)*(size.y - 150 - y))  <= 10000) {
            //indicate tank should be moved
            setdirection = true;
            ytoset = 0-(size.y - 150 - y);
            xtoset = 150 - x;

            xvelocitytoset = 2.0 * 150.0 / (150.0 - Math.abs(150.0 - lastx));
            yvelocitytoset = 2.0 * 150.0 / (150.0 - Math.abs((size.y - 150.0) - lasty));

            //snap onto a straight line
            if (Math.abs(150.0 - lastx) < 10)
                xvelocitytoset = 0;
            if (Math.abs((size.y - 150.0) - lasty) < 10)
                yvelocitytoset = 0;

            lastx = x;
            lasty = y;

            //get angle to dot
            try {
                //atan2 is not atan squared, its a better atan
                theta = Math.atan2((150 - x), (size.y - 150 - y));
                //see annotation above getTheta functions
                thetaget = Math.atan2((size.y - 150 - y), (150 - x));
            } catch (ArithmeticException e) {
                //at just the wrong angle we get a divide by zero exception
                theta = 0;
            }


            //converting to degrees and posting to tank
            ship.setAngleOfRotation((float) (theta * 180 / Math.PI));

        } else if (touched) {
            setdirection = true;
            ytoset = 0-(size.y - 150 - y);
            xtoset = 150 - x;

            //get angle to dot
            try {
                //atan2 is not atan squared, its a better atan
                theta = Math.atan2((150 - x), (size.y - 150 - y));
                //see annotation above getTheta functions
                thetaget = Math.atan2((size.y - 150 - y), (150 - x));
            } catch (ArithmeticException e) {
                //at just the wrong angle we get a divide by zero exception
                theta = 0;
            }

            lastx = 150 - (int) (100 * Math.sin(theta));
            lasty = size.y - 150 - (int) (100 * Math.cos(theta));

            xvelocitytoset = 2.0 * 150.0 / (150.0 - Math.abs(150.0 - lastx));
            yvelocitytoset = 2.0 * 150.0 / (150.0 - Math.abs((size.y - 150.0) - lasty));

            //snap onto a straight line
            if (Math.abs(150.0 - lastx) < 10)
                xvelocitytoset = 0;
            if (Math.abs((size.y - 150.0) - lasty) < 10)
                yvelocitytoset = 0;

            //rotate tank //does not work //need to learn matrix
            ship.setAngleOfRotation((float) (theta * 180 / Math.PI));
        }
    }

    public void handleActionUp()
    {
       if (touched){
           touched = false;
           setdirection = true;
           xtoset = 0;
           ytoset = 0;
           xvelocitytoset = 0;
           yvelocitytoset = 0;
           lastx = 150;
           lasty = size.y - 150;
       }
    }

    public void draw(Canvas c)
    {
        //draw back circle
        c.drawCircle(150, size.y - 150, 100, paint);

        //draw indicator
        c.drawCircle(lastx, lasty, 25, indicatorPaint);
    }
}