package johnserrano.tank;

import android.graphics.Bitmap;

/**
 * Created by john on 7/25/15.
 */
public interface Collidable {
    double getX();
    /**
     *  Get X value of object
     */

    double getY();
    /**
     * get Y value of object;
     */

    Bitmap getBitmap();
    /**
     * get bitmap of object;
     */

    void onBreak();
    /**
     * action on collision
     */

    boolean isCircle();
    /**
     * returns true if object is a circle
     */
}
