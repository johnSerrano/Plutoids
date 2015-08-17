package johnserrano.tank.sprites;

import android.graphics.Bitmap;

import java.util.Random;

class Pluto extends Asteroid
{
    int xDirection;
    int yDirection;
    public Pluto(Asteroids a, Bitmap b, double x, double y, double velocityx, double velocityy)
    {
        super(a, b, x , y, velocityx, velocityy);
    }

    @Override
    void doBreak(int x, int y, double xVelocity, double yVelocity) {
        parentList.newCharon(this.x, this.y, xVelocity * xDirection * 4, yVelocity * yDirection * 4);
        parentList.newCharon(this.x, this.y, xVelocity * xDirection * -4, yVelocity * yDirection * -4);
    }

    public void onBreak()
    {
        parentList.score += 1;
        //make two charons
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

        broken  = true;

        destroy = true;
    }

    @Override
    int getValue() {
        return 15;
    }

    @Override
    public boolean isCircle() {
        return true;
    }
}
