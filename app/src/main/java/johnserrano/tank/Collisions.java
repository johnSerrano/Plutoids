package johnserrano.tank;

import android.graphics.Color;

import johnserrano.tank.sprites.Asteroids;
import johnserrano.tank.sprites.Projectiles;
import johnserrano.tank.sprites.Ship;

public class Collisions {
    Ship ship;
    Asteroids asteroids;
    Projectiles projectiles;

    public Collisions(Ship s, Asteroids a, Projectiles p)
    {
        ship = s;
        asteroids = a;
        projectiles = p;
    }

    public void update()
    {
        //if(asteroids.getList().isEmpty() || projectiles.getList().isEmpty()) return;

        for (Collidable a : asteroids.getList()) {
            for (Collidable p : projectiles.getList()) {
                //for circular objects use circular collision detection
                if (a.isCircle()) {
                    if (circleCollision(p, a)) {
                        a.onBreak();
                        p.onBreak();
                    }
                }
                //pixel collision for irregular shapes
                //possibly a little excessive
                else if (pixelCollision(p, a)) {
                    a.onBreak();
                    p.onBreak();
                }
            }
            //pixel perfect detection for ship
            if (pixelCollision(a, ship)) {
                //AAAAAAA!!!!!!!!
                //GAME OVER
                asteroids.resetScore();
                ship.onBreak();
            }
        }
    }

    private boolean bitmapCollision(Collidable a, Collidable b)
    {
        //if bitmaps touch, return true

        if(a.getX() > b.getX()) {
            if (a.getY() > b.getY()) {
                //a.x > b.x; a.y>b.y
                //if collision return true


                if ((((int)a.getX() - a.getBitmap().getWidth()/2) - ((int)b.getX() + b.getBitmap().getWidth()/2) < 0) &&
                        (((int)a.getY() - a.getBitmap().getHeight()/2) - ((int)b.getY() + b.getBitmap().getHeight()/2)) < 0)
                    return true;
                else
                    return false;
            } else {
                //a.x > b.x; a.y<b.y
                //if collision return true
                if ((((int)a.getX() - a.getBitmap().getWidth()/2) - ((int)b.getX() + b.getBitmap().getWidth()/2) < 0) &&
                        (((int)b.getY() - b.getBitmap().getHeight()/2) - ((int)a.getY() + a.getBitmap().getHeight()/2)) < 0)
                    return true;
                else
                    return false;
            }
        } else {
            if (a.getY() > b.getY()) {
                //a.x < b.x; a.y>b.y
                //if collision return true
                if ((((int)b.getX() - b.getBitmap().getWidth()/2) - ((int)a.getX() + a.getBitmap().getWidth()/2) < 0) &&
                        (((int)a.getY() - a.getBitmap().getHeight()/2) - ((int)b.getY() + b.getBitmap().getHeight()/2)) < 0)
                    return true;
                else
                    return false;
            } else {
                //a.x < b.x; a.y<b.y
                //if collision return true
                if ((((int)b.getX() - b.getBitmap().getWidth()/2) - ((int)a.getX() + a.getBitmap().getWidth()/2) < 0) &&
                        (((int)b.getY() - b.getBitmap().getHeight()/2) - ((int)a.getY() + a.getBitmap().getHeight()/2)) < 0)
                    return true;
                else
                    return false;
            }
        }
    }

    private boolean circleCollision(Collidable a, Collidable b)
    {
        //collision boundary for b is circle from center, with radius equal to height
        if(a.getX() > b.getX()) {
            if (a.getY() > b.getY()) {

                //These if statements are actually a lot simpler than they look
                if (((a.getX() - a.getBitmap().getWidth()/2) -b.getX())*((a.getX() - a.getBitmap().getWidth()/2) - b.getX()) +
                        ((a.getY() - a.getBitmap().getHeight()/2) - b.getY())*((a.getY() - a.getBitmap().getHeight()/2) - b.getY()) <
                        (b.getBitmap().getHeight()/2)*( b.getBitmap().getHeight()/2))
                    return true;
                else
                    return false;
            } else {
                //a.x > b.x; a.y<b.y
                //if collision return true
                if (((a.getX() - a.getBitmap().getWidth()/2) -b.getX())*((a.getX() - a.getBitmap().getWidth()/2) - b.getX()) +
                        (b.getY() - (a.getY() + a.getBitmap().getHeight()/2))*(b.getY() - (a.getY() + a.getBitmap().getHeight()/2)) <
                        (b.getBitmap().getHeight()/2)*( b.getBitmap().getHeight()/2))
                    return true;
                else
                    return false;
            }
        } else {
            if (a.getY() > b.getY()) {
                //a.x < b.x; a.y>b.y
                //if collision return true
                if ((b.getX() - (a.getX() + a.getBitmap().getWidth()/2))*(b.getX() - (a.getX() + a.getBitmap().getWidth()/2)) +
                        ((a.getY() - a.getBitmap().getHeight()/2) - b.getY())*((a.getY() - a.getBitmap().getHeight()/2) - b.getY()) <
                        (b.getBitmap().getHeight()/2)*( b.getBitmap().getHeight()/2))
                    return true;
                else
                    return false;
            } else {
                //a.x < b.x; a.y<b.y
                //if collision return true
                if ((b.getX() - (a.getX() + a.getBitmap().getWidth()/2))*(b.getX() - (a.getX() + a.getBitmap().getWidth()/2)) +
                        (b.getY() - (a.getY() + a.getBitmap().getHeight()/2))*(b.getY() - (a.getY() + a.getBitmap().getHeight()/2)) <
                        (b.getBitmap().getHeight()/2)*( b.getBitmap().getHeight()/2))
                    return true;
                else
                    return false;
            }
        }
    }

    private boolean pixelCollision(Collidable a, Collidable b) {
        //check for collision by pixel
        //ONLY IF NECESSARY! RESOURCE INTENSIVE

        //if bitmaps don't intersect, return false
        if (a.getX() > b.getX()) {
            if (a.getY() > b.getY()) {
                //a.x > b.x; a.y>b.y
                //if collision return true


                if (!((((int) a.getX() - a.getBitmap().getWidth() / 2) - ((int) b.getX() + b.getBitmap().getWidth() / 2) < 0) &&
                        (((int) a.getY() - a.getBitmap().getHeight() / 2) - ((int) b.getY() + b.getBitmap().getHeight() / 2)) < 0))
                    return false;
            } else {
                //a.x > b.x; a.y<b.y
                //if collision return true
                if (!((((int) a.getX() - a.getBitmap().getWidth() / 2) - ((int) b.getX() + b.getBitmap().getWidth() / 2) < 0) &&
                        (((int) b.getY() - b.getBitmap().getHeight() / 2) - ((int) a.getY() + a.getBitmap().getHeight() / 2)) < 0))
                    return false;
            }
        } else {
            if (a.getY() > b.getY()) {
                //a.x < b.x; a.y>b.y
                //if collision return true
                if (!((((int) b.getX() - b.getBitmap().getWidth() / 2) - ((int) a.getX() + a.getBitmap().getWidth() / 2) < 0) &&
                        (((int) a.getY() - a.getBitmap().getHeight() / 2) - ((int) b.getY() + b.getBitmap().getHeight() / 2)) < 0))
                    return false;
            } else {
                //a.x < b.x; a.y<b.y
                //if collision return true
                if (!((((int) b.getX() - b.getBitmap().getWidth() / 2) - ((int) a.getX() + a.getBitmap().getWidth() / 2) < 0) &&
                        (((int) b.getY() - b.getBitmap().getHeight() / 2) - ((int) a.getY() + a.getBitmap().getHeight() / 2)) < 0))
                    return false;
            }
        }

        //calculate overlap area
        int aLeftBound = ((int) a.getX() - a.getBitmap().getWidth()/2);
        int aRightBound = ((int) a.getX() + a.getBitmap().getWidth()/2);
        int aTopBound = ((int) a.getY() - a.getBitmap().getHeight()/2);
        int aBottomBound = ((int) a.getY() + a.getBitmap().getHeight()/2);

        int bLeftBound = ((int) b.getX() - b.getBitmap().getWidth()/2);
        int bRightBound = ((int) b.getX() + b.getBitmap().getWidth()/2);
        int bTopBound = ((int) b.getY() - b.getBitmap().getHeight()/2);
        int bBottomBound = ((int) b.getY() + b.getBitmap().getHeight()/2);

        int leftBound = Math.max(aLeftBound, bLeftBound);
        int rightBound = Math.min(aRightBound, bRightBound);
        int topBound = Math.max(aTopBound, bTopBound);
        int bottomBound = Math.min(aBottomBound, bBottomBound);

        for(int i = leftBound; i<rightBound; i++) {
            for (int j = topBound; j < bottomBound; j++) {
                int aColor = a.getBitmap().getPixel(i - (int) a.getX() + a.getBitmap().getWidth()/2, j - (int) a.getY() + a.getBitmap().getHeight()/2);
                int bColor = b.getBitmap().getPixel(i - (int) b.getX() + b.getBitmap().getWidth()/2, j - (int) b.getY() + b.getBitmap().getHeight()/2);
                if (isSet(aColor) && isSet(bColor)) return true;

            }
        }

        return false;
    }

    private static boolean isSet(int color) {
        return color != Color.TRANSPARENT;
    }
}
