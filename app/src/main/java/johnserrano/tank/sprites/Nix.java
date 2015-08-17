package johnserrano.tank.sprites;

import android.graphics.Bitmap;

class Nix extends Asteroid
{
    public Nix(Asteroids a, Bitmap b, double x, double y, double velocityx, double velocityy)
    {
        super(a, b, x , y, velocityx, velocityy);
    }

    @Override
    void doBreak(int x, int y, double xVelocity, double yVelocity) {
        //do nothing
    }

    public void onBreak()
    {
        parentList.score += 4;
        //gone forever
        destroy = true;
    }

    @Override
    int getValue() {
        return 1;
    }

    @Override
    public boolean isCircle() {
        return false;
    }
}
