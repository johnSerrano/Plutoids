package johnserrano.tank;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import johnserrano.tank.sprites.Asteroids;
import johnserrano.tank.sprites.Joystick;
import johnserrano.tank.sprites.PauseButton;
import johnserrano.tank.sprites.Projectiles;
import johnserrano.tank.sprites.Ship;
import johnserrano.tank.sprites.Touchable;
import johnserrano.tank.sprites.Trigger;

//TODO: Make menus pretty
//TODO: Debug pixel collisions (Colliding when pixels are far from each other)
//      - particularly with nixes and hydras
//TODO: polish game over dialog
//      - Opens multiple times... not a functionality issue but performance might be impacted


public class Game extends Activity
{
    GamePanel panel;
    GameOverDialog gameOverDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        gameOverDialog = null;

        panel = new GamePanel(this);
        setContentView(panel);
        hideStatusBar();

        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(
                new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        //if status bar becomes visible, hide it after delay

                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    hideStatusBar();
                                }
                            }, 2500);
                        }
                    }
                }
        );
    }

    public void hideStatusBar()
    {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    public void pause()
    {
        panel.loop.onPause();
    }

    public void resume()
    {
        /*
        if we pause while touching a touchable,
        we need to call handleactionup here, or
        it never gets the action-up command
        so it sticks in place until it is touched
        */
        panel.joystick.handleActionUp();
        panel.trigger.handleActionUp();


        panel.loop.onResume();
    }

    public void returnToMenu()
    {
        Intent openMenu = new Intent(this, MainMenu.class);
        startActivity(openMenu);
    }

    public void gameOver(final int score)
    {
        //this method is called when there is a collision


        final Context context = this;
        this.runOnUiThread(new Runnable() {
            public void run() {
                //need these checks to guarantee we only create one dialog
                //multithreading issues may cause this method to be called multiple times
                if (gameOverDialog == null)
                    gameOverDialog = new GameOverDialog(context, score);
                if (!gameOverDialog.isShowing()){
                    gameOverDialog.show();
                    Stats.logHighScore(context, score);
                }
                pause();
            }
        });
    }
}

class MainLoop extends Thread
{
    boolean running;
    boolean paused;
    boolean gameOver;
    GamePanel gamePanel;
    SurfaceHolder surfaceHolder;
    Object pauseLock;

    public MainLoop(GamePanel g, SurfaceHolder s)
    {
        super();
        gamePanel = g;
        surfaceHolder = s;

        paused = false;
        gameOver = false;

        pauseLock = new Object();
    }

    public void onPause()
    {
        paused = true;
    }

    public void onResume()
    {
        paused = false;
        synchronized (pauseLock){
            pauseLock.notifyAll();
        }
    }

    public void setRunning(boolean running)
    {
        this.running = running;
    }

    @SuppressLint("WrongCall")
    public void run()
    {
        Canvas c;

        while (running) {

            c = null;
            try {

                synchronized (surfaceHolder) {
                    c = this.surfaceHolder.lockCanvas();
                    gamePanel.onDraw(c);
                    gamePanel.update();
                }
            } catch (NullPointerException e) {
                //happens when application closed
                running = false;
            }finally {
                if (c!=null) surfaceHolder.unlockCanvasAndPost(c);
            }

            synchronized (pauseLock) {
                while (paused) {
                    try {
                        pauseLock.wait();
                    } catch (InterruptedException e) {}
                }
            }


        }
    }
}

class GamePanel extends SurfaceView implements SurfaceHolder.Callback
{
    MainLoop loop;
    Point size;
    Context context;

    //array of touches
    Pointer[] pointers;

    Projectiles projectiles;
    Asteroids asteroids;
    Collisions collisions;

    //Graphics
    Ship ship;
    Joystick joystick;
    Trigger trigger;
    PauseButton pauseButton;

    GamePanel(Context context)
    {
        super(context);
        this.context = context;

        getHolder().addCallback(this);
        setFocusable(true);
        loop = new MainLoop(this, getHolder());

        //get screen dimens (size.x, size.y)
        size = new Point();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getSize(size);
        //if this were a little less backward compatible (API 17 instead of 16) we could do:
        //getDisplay().getSize(size);

        pointers = new Pointer[10];


        ship = new Ship(BitmapFactory.decodeResource(getResources(), R.drawable.space_ship2), size.x / 2, size.y / 2, size);

        asteroids = new Asteroids(
                BitmapFactory.decodeResource(getResources(), R.drawable.nix),
                BitmapFactory.decodeResource(getResources(), R.drawable.hydra),
                BitmapFactory.decodeResource(getResources(), R.drawable.charon),
                BitmapFactory.decodeResource(getResources(), R.drawable.pluto),
                ship,
                size,
                PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()).getInt("HighScore0", 0)
        );

        joystick = new Joystick(ship, size);

        projectiles = new Projectiles(BitmapFactory.decodeResource(getResources(), R.drawable.projectile), ship, joystick);

        trigger = new Trigger(size, ship, projectiles);

        collisions = new Collisions(context, ship, asteroids, projectiles);

        pauseButton = new PauseButton(context, BitmapFactory.decodeResource(getResources(), R.drawable.pause));
    }

    public void update()
    {
        //update game
        joystick.update();
        ship.update();
        projectiles.update();
        asteroids.update();
        collisions.update();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        if (loop.getState() == Thread.State.TERMINATED) {
            loop = new MainLoop(this, getHolder());

            //resume in pause menu
            loop.onPause();
            if (!pauseButton.dialogShowing())
                pauseButton.handleActionDown(0, 0);

            //trigger systemuichange on resume
            //otherwise status bar doesn't hide after resuming
            ((Game) context).hideStatusBar();
        }
        loop.setRunning(true);
        loop.start();

    }

    //satisfies implementation of SurfaceHolder.Callback
    //do nothing
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        loop.onResume();
       while (true) {
            try {
                loop.join();
                break;
            } catch (InterruptedException e) {
                //try again
            }
        }
    }

    private Touchable getTouchableByCoords(int x, int y)
    {
        //returns null if no touchable at touch location
        Touchable t = null;

        if(Math.abs((150-x)*(150-x)) + Math.abs((size.y - 150 - y)*(size.y - 150 - y))  <= 10000){
            //in joystick
            t = joystick;
        }
        if(Math.abs((size.x - 150-x)*(size.x - 150-x)) + Math.abs((size.y - 150 - y)*(size.y - 150 - y))  <= 10000) {
            //in trigger
            t = trigger;
        }

        //check pause button
        if (x <= 155 &&  y <= 155 && x >= 75 && y >= 75){
            t = pauseButton;
        }

        return t;
    }
    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
        int action = MotionEventCompat.getActionMasked(e);
        int index = (e.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        int pointerId;

        pointerId = e.getPointerId(index);

        if (pointers[pointerId] == null) {
            pointers[pointerId] = new Pointer(getTouchableByCoords((int) e.getX(index), (int) e.getY(index)));
        }

        if (pointers[pointerId].getTouchable() != null) {
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN:
                    pointers[pointerId].getTouchable().handleActionDown((int) e.getX(index), (int) e.getY(index));
                    break;
                case MotionEvent.ACTION_MOVE:
                    for (int i = 0; i < e.getPointerCount(); ++i) {
                        index = i;
                        pointerId = e.getPointerId(index);
                        if (pointerId == 0) {
                            pointers[pointerId].getTouchable().handleActionMove((int) e.getX(index), (int) e.getY(index));
                        }
                        if (pointerId == 1) {
                            pointers[pointerId].getTouchable().handleActionMove((int) e.getX(index), (int) e.getY(index));
                        }
                    }
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_UP:
                    pointers[pointerId].getTouchable().handleActionUp();
                    pointers[pointerId] = null;
                    break;
            }
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas c)
    {
        c.drawColor(Color.BLACK);

        projectiles.draw(c);
        asteroids.draw(c);
        trigger.draw(c);
        joystick.draw(c);
        ship.draw(c);
        pauseButton.draw(c);

    }
}