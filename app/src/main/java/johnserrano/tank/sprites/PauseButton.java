package johnserrano.tank.sprites;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import johnserrano.tank.Game;
import johnserrano.tank.PauseDialog;

public class PauseButton implements Touchable
{
    Context context;
    Bitmap bitmap;
    PauseDialog pauseDialog;

    public PauseButton(Context context, Bitmap b)
    {
        this.context = context;
        bitmap = b;
        pauseDialog = new PauseDialog(context);
    }

    public void dismissDialog()
    {
        pauseDialog.dismiss();
    }

    public boolean dialogShowing()
    {
        return pauseDialog.isShowing();
    }

    @Override
    public void handleActionDown(int x, int y)
    {
        //open pause dialog
        pauseDialog = new PauseDialog(context);
        pauseDialog.show();
        ((Game) context).pause();
    }

    @Override
    public void handleActionMove(int x, int y)
    {
        //do nothing
    }

    @Override
    public void handleActionUp()
    {
        //do nothing
    }

    public void draw(Canvas c)
    {
        //draw pause button
        c.drawBitmap(bitmap, 75, 75, null);
    }
}