package johnserrano.tank.sprites;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import johnserrano.tank.Collidable;

abstract class Mob implements Collidable {
    protected Bitmap bitmap;
    protected double x;
    protected double y;

    protected Speed speed;

    public Speed getSpeed()
    {
        return speed;
    }

    public Bitmap getBitmap()
    {
        return bitmap;
    }

    public void setBitmap(Bitmap b)
    {
        bitmap = b;
    }

    public double getX()
    {
        return x;
    }

    public void setX(double x)
    {
        this.x = x;
    }

    public double getY()
    {
        return y;
    }

    public void setY(double y)
    {
        this.y = y;
    }

    public void draw(Canvas c)
    {
        c.drawBitmap(bitmap, (float) (x - (bitmap.getWidth()/2)), (float) (y - (bitmap.getWidth()/2)), null);
    }
}