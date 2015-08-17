package johnserrano.tank.sprites;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

/**
 * this class creates the trigger button, and handles its behavior
 */
public class Trigger implements Touchable
{
    boolean touched;
    Point size;
    Paint paint;
    Paint indicatorPaint;
    int indicatorSize;
    Ship ship;
    Projectiles projectiles;

    public Trigger(Point size, Ship ship, Projectiles projectiles)
    {
        this.size = size;
        this.ship = ship;
        this.projectiles = projectiles;

        touched = false;

        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.GRAY);
        paint.setAntiAlias(true);

        indicatorPaint = new Paint();
        indicatorPaint.setStyle(Paint.Style.FILL);
        indicatorPaint.setColor(Color.RED);
        indicatorPaint.setAntiAlias(true);
        indicatorPaint.setStrokeWidth(10f);

        indicateTouched();
    }

    private void indicateTouched()
    {
        //set indicator to red boder around trigger circle
        if (touched) {
            indicatorSize = 100;
            indicatorPaint.setStyle(Paint.Style.STROKE);

        } else {
            indicatorSize = 25;
            indicatorPaint.setStyle(Paint.Style.FILL);
        }
    }

    @Override
    public void handleActionDown(int x, int y)
    {
        //if we got touched, set flag
        if(Math.abs((size.x - 150-x)*(size.x - 150-x)) + Math.abs((size.y - 150 - y)*(size.y - 150 - y))  <= 10000) {
            touched = true;
            indicateTouched();
            projectiles.launchProjectile();
        }
    }

    @Override
    public void handleActionMove(int x, int y) {
        //do nothing, we don't care about this
    }

    @Override
    public void handleActionUp()
    {
        if (touched) {
            touched = false;
            indicateTouched();
        }
    }

    public void draw(Canvas c)
    {
        c.drawCircle(size.x - 150, size.y - 150, 100, paint);
        c.drawCircle(size.x - 150, size.y - 150, indicatorSize, indicatorPaint);
    }
}