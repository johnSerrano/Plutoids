package johnserrano.tank.sprites;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.LinkedList;

public class Projectiles
{
    //this class handles all the projectiles
    //and reinvents the wheel a little b/c LinkedList is being shitty
    Ship ship;
    Bitmap bitmap;
    Joystick joystick;

    LinkedList<Projectile> projectileList;

    boolean projectileToAdd;

    //
    Projectile projectile;

    public Projectiles(Bitmap b, Ship s, Joystick j)
    {
        projectileList = new LinkedList<>();
        bitmap = b;
        ship = s;
        joystick = j;
    }

    public LinkedList<Projectile> getList()
    {
        return projectileList;
    }

    public void launchProjectile()
    {
        projectileToAdd = true;
    }

    private void addProjectile(float theta, float theta2)
    {
        projectile = new Projectile(bitmap, ship.getX(), ship.getY(), theta, theta2);
        projectileList.add(projectile);
    }

    public void update()
    {
        if (projectileToAdd) {
            projectileToAdd = false;
            addProjectile((float) joystick.getThetaYX(), (float) joystick.getThetaXY());
        }

        if (!projectileList.isEmpty())

            //removing by index is faster,
            //foreach causes concurrent modification failure
            for (int i = 0; i<projectileList.size(); i++)
            {
                projectileList.get(i).update();
                if (projectileList.get(i).getDestroy()) projectileList.remove(i);
            }
    }

    public void draw(Canvas c)
    {
        if (!projectileList.isEmpty())

            for (int i = 0; i<projectileList.size(); i++) {
                projectileList.get(i).draw(c);
            }
    }
}