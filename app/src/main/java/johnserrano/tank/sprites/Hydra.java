package johnserrano.tank.sprites;

import android.graphics.Bitmap;

import java.util.Random;

class Hydra extends Asteroid
{
    int xDirection;
    int yDirection;
    public Hydra(Asteroids a, Bitmap b, double x, double y, double velocityx, double velocityy)
    {
        super(a, b, x , y, velocityx, velocityy);
    }

    @Override
    void doBreak(int x, int y, double xVelocity, double yVelocity) {
        parentList.newNix(this.x, this.y, xVelocity * xDirection * 8, yVelocity * yDirection * 8);
        parentList.newNix(this.x, this.y, xVelocity * xDirection * -8, yVelocity * yDirection * -8);
    }

    public void onBreak()
    {
        parentList.score += 3;
        //create two nixes
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
        return 3;
    }

    @Override
    public boolean isCircle() {
        return false;
    }
}
