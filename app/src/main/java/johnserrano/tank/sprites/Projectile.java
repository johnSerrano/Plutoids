package johnserrano.tank.sprites;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class Projectile extends Mob {
    Matrix matrix;
    float theta;
    double startx;
    double starty;
    double x;
    double y;
    Speed speed;
    boolean destroy;

    public Projectile(Bitmap b, double x, double y, float theta, float theta2)
    {
        this.x = x;
        this.y = y;
        starty = y;
        startx = x;
        this.theta = theta;
        matrix = new Matrix();
        bitmap = b;
        matrix.postRotate(theta);

        destroy = false;

        double velocityx = -10 * Math.cos(theta);
        double velocityy = -10 * Math.sin(theta);

        speed = new Speed(velocityx, velocityy);

        setAngleOfRotation((float) (theta2 * 180 / Math.PI));
    }

    public void setAngleOfRotation(float theta)
    {
        while(theta < 0) { theta += 360.0; }
        while(theta >= 360.0) { theta -= 360.0; }
        this.theta = 270 - theta;
    }

    public void update()
    {
        x += speed.getVelocityX();
        y += speed.getVelocityY();

        //this happens off screen, keeps list of projectiles from inflating forever
        if(Math.abs((x - startx)*(x - startx)) + Math.abs((y - starty)*(y - starty))  >= 4000000) {
            destroy = true;
        }

        matrix.reset();
        //rotate around center
        matrix.postTranslate(-bitmap.getWidth() / 2, -bitmap.getHeight() / 2);
        matrix.postRotate(theta);

        //move to position
        matrix.postTranslate((float) x, (float) y);
    }

    public boolean getDestroy()
    {
        return destroy;
    }

    @Override
    public void draw(Canvas c)
    {
        c.drawBitmap(bitmap, matrix, null);
    }

    @Override
    public void onBreak() {
        destroy = true;
    }

    @Override
    public boolean isCircle() {
        return false;
    }

    @Override
    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }
}