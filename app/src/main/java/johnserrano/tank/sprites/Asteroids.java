package johnserrano.tank.sprites;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.LinkedList;
import java.util.Random;

import johnserrano.tank.Stats;

public class Asteroids
{
    LinkedList<Asteroid> asteroids;
    Bitmap nixmap;
    Bitmap hydramap;
    Bitmap charonmap;
    Bitmap plutomap;
    Ship ship;
    Point size;
    Random random;
    Paint paint;
    int totalValue;
    int score;
    int highScore;


    public Asteroids(Bitmap nix, Bitmap hydra, Bitmap charon, Bitmap pluto, Ship s, Point size, int highScore)
    {


        //paint for score
        paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(24);

        totalValue = 0;
        score = 0;
        this.highScore = highScore;


        nixmap = nix;
        hydramap = hydra;
        charonmap = charon;
        plutomap = pluto;
        ship = s;
        asteroids = new LinkedList<>();
        this.size = size;
        random = new Random();
    }

   public LinkedList<Asteroid> getList()
   {
       return asteroids;
   }

    //to create an asteroid, all you need to do is call one of these methods
    public void newNix(double x, double y, double velocityx, double velocityy)
    {
        asteroids.add(new Nix(this, nixmap, x, y, velocityx, velocityy));
    }

    public void newHydra(double x, double y, double velocityx, double velocityy)
    {
        asteroids.add(new Hydra(this, hydramap, x, y, velocityx, velocityy));
    }

    public void newCharon(double x, double y, double velocityx, double velocityy)
    {
        asteroids.add(new Charon(this, charonmap, x, y, velocityx, velocityy));
    }

    public void newPluto(double x, double y, double velocityx, double velocityy)
    {
        asteroids.add(new Pluto(this, plutomap, x, y, velocityx, velocityy));
    }

    private void generateAsteroid()
    {
        //if the total value of all asteroids in list <= 25 spawn a new asteroid
        //int totalValue = 0;

        totalValue = 0;


        int xVelCoefficient = 1;
        int yVelCoefficient = 1;
        double xVel = .5;
        double yVel = .5;
        double x = 0;
        double y = 0;
        int percentage;

        for (int i = 0; i<asteroids.size(); i++)
        {
            totalValue += asteroids.get(i).getValue();
        }

        if (totalValue < 25) {

            //choose velocity:
            double tmpVel = random.nextDouble();

            //no sharp angles
            if (tmpVel < 0.25) xVel = 0.25;
            else if (tmpVel > 0.75) xVel = 0.75;
            //tmpvel is assigned to either xvel or yvel depending on angle of entry
            //this way we can guarantee we don't go off at sharp angles from any side



            //choose side/position to enter from and direction at random
            switch(random.nextInt(4)) {
                case 0:
                    //come in from top
                    //y velocity must be positive
                    //no need to specify positive assignment

                    //pick x and y
                    //use plutomap because its the biggest
                    y = 0 - plutomap.getHeight()/2; //top
                    x = random.nextInt(size.x);

                    if (x < size.x/2) xVelCoefficient = -1;

                    xVel = tmpVel;
                    yVel = Math.sqrt(1 - (xVel * xVel));

                    break;
                case 1:
                    //come in from right
                    //x velocity must be negative
                   // xVelCoefficient = -1;

                    //pick x and y
                    y = random.nextInt(size.y); //top
                    x = size.x + plutomap.getWidth()/2;

                    if (y > size.y/2) yVelCoefficient = -1;

                    yVel = tmpVel;
                    xVel = Math.sqrt(1 - (yVel * yVel));

                    break;
                case 2:
                    //come in from bottom
                    //y velocity must be negative
                    yVelCoefficient = -1;

                    //pick x and y;
                    y = size.y + plutomap.getHeight()/2;
                    x = random.nextInt(size.x);

                    if (x < size.x/2) xVelCoefficient = -1;

                    xVel = tmpVel;
                    yVel = Math.sqrt(1 - (xVel * xVel));

                    break;
                case 3:
                    //come in from left
                    //x velocity must be positive
                    xVelCoefficient = -1;

                    //pick x and y
                    y = random.nextInt(size.y);
                    x = 0 - plutomap.getWidth()/2;

                    if (y > size.y/2) yVelCoefficient = -1;

                    yVel = tmpVel;
                    xVel = Math.sqrt(1 - (yVel * yVel));

                    break;
            }



            //choose size of asteroid at random (weighted)
            //nix : 50%
            //hydra : 25%
            //charon : 15%
            //pluto : 10%

            percentage = random.nextInt(100);
            if (percentage < 50) {
                //newNix
                newNix(x, y, xVel * 8, yVel * 8);
            } else if (percentage < 75) {
                //newHydra
                newHydra(x, y, xVel * 6, yVel * 6);
            } else if (percentage < 90) {
                //newCharon
                newCharon(x, y, xVel * 4, yVel * 4);
            } else {
                //newPluto
                newPluto(x, y, xVel * 2, yVel * 2);
            }
            asteroids.getLast().speed.setYDirection(yVelCoefficient);
            asteroids.getLast().speed.setXDirection(xVelCoefficient);

        }
    }

    public void update()
    {
        generateAsteroid();

        for (int i = 0; i<asteroids.size(); i++)
        {
            asteroids.get(i).update();

            if (asteroids.get(i).getDestroy()) {
                asteroids.remove(i);
                i--;
                continue;
            }

            //remove asteroids who move off screen
            if (asteroids.get(i).getX() > size.x + asteroids.get(i).getBitmap().getWidth()/2 ||
                    asteroids.get(i).getX() < 0 - asteroids.get(i).getBitmap().getWidth()/2 ||
                    asteroids.get(i).getY() > size.y + asteroids.get(i).getBitmap().getHeight()/2 ||
                    asteroids.get(i).getY() < 0 - asteroids.get(i).getBitmap().getHeight()/2) {
                    if (!asteroids.get(i).getStarting()) { //checking to ensure we don't delete asteroids as they spawn
                        asteroids.remove(i);
                        i--; //because we removed an element, we don't want to skip the next one
                    }
            } else {
                asteroids.get(i).notStarting();
            }

            if (score > highScore)
                highScore = score;
        }
    }

    public void resetScore()
    {
        score = 0;
    }

    public int getScore()
    {
        return score;
    }

    public void draw(Canvas c)
    {
        for (int i = 0; i<asteroids.size(); i++)
        {
            asteroids.get(i).draw(c);
        }

        c.drawText("SCORE: " + score + "\n\nHIGHSCORE: " + highScore, 200, 100, paint);

    }
}
