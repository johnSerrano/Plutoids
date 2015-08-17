package johnserrano.tank.sprites;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;

public class Ship extends Mob {
    Matrix matrix;
    float theta;
    Point size;

    public Ship(Bitmap b, double x, double y, Point s)
    {
        speed = new Speed(0, 0, Speed.RIGHT, Speed.UP);
        bitmap = b;
        this.x = x;
        this.y = y;
        matrix = new Matrix();
        theta = 0;
        size = s;
    }

    @Override
    public void draw(Canvas c)
    {
        c.drawBitmap(bitmap, matrix, null);
    }

    public void setAngleOfRotation(float theta)
    {
        while(theta < 0) { theta += 360.0; }
        while(theta >= 360.0) { theta -= 360.0; }
        this.theta = 270 - theta;
    }

    public void update()
    {
        matrix.reset();

        //set this so we rotate around center
        matrix.postTranslate(-bitmap.getWidth()/2, -bitmap.getHeight()/2);

        matrix.postRotate(theta);

        //don't move off the screen
        if (((speed.getXDirection() > 0 && x >= size.x) ||
                speed.getXDirection() < 0 && x <= 0)) {
            speed.setVelocityX(0);
        }
        if (((speed.getYDirection() > 0 && y >= size.y) ||
                speed.getYDirection() < 0 && y <= 0)) {
            speed.setVelocityY(0);
        }

        x += speed.getVelocityX() *  speed.getXDirection();
        y += speed.getVelocityY() *  speed.getYDirection();

        matrix.postTranslate((float)x,(float) y);
    }

    @Override
    public void onBreak() {
        //AAAAAAAAAAAAAAAAAA
    }

    @Override
    public boolean isCircle() {
        return false;
    }
}