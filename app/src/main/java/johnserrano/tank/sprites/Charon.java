package johnserrano.tank.sprites;

import android.graphics.Bitmap;

import java.util.Random;

class Charon extends Asteroid
{
    int xDirection = 1;
    int yDirection = 1;

    public Charon(Asteroids a, Bitmap b, double x, double y, double velocityx, double velocityy)
    {
        super(a, b, x, y, velocityx, velocityy);

    }

    @Override
    void doBreak(int x, int y, double xVelocity, double yVelocity) {
        parentList.newHydra(this.x, this.y, xVelocity * xDirection * 6, yVelocity * yDirection * 6);
        parentList.newHydra(this.x, this.y, xVelocity * xDirection * -6, yVelocity * yDirection * -6);
    }

    public void onBreak()
    {
        parentList.score += 2;
        //create two hydras
        xDirection = 1;
        yDirection = 1;

        Random random = new Random();

        //generate direction as random
        switch(random.nextInt(2)){
            case 0:
                xDirection = -1;
        }

        switch(random.nextInt(2)) {
            case 0:
                yDirection = -1;
        }

        velocityXbreak = random.nextDouble();
        velocityYbreak = 1 - velocityXbreak;

        broken = true;

        destroy = true;
    }

    @Override
    int getValue() {
        return 7;
    }

    @Override
    public boolean isCircle() {
        return true;
    }
}