package johnserrano.tank;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class GameOverDialog extends Dialog implements View.OnTouchListener
{
    Context context;
    Button resumeButton;
    Button quitButton;

    public GameOverDialog(Context context)
    {
        super(context);
        this.context = context;

        setCancelable(false);

        final GameOverDialog t = this; //for ondismisslistener to get context

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ((Game) t.context).resume();
            }
        });
    }

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.game_over_dialog);

        resumeButton = (Button) findViewById(R.id.restart_button);
        quitButton = (Button) findViewById(R.id.quit_button);

        resumeButton.setOnTouchListener(this);
        quitButton.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        switch (v.getId()){
            case R.id.restart_button:
                //TODO: restart
                //dismiss();
                break;
            case R.id.quit_button:
                //go to main menu
                ((Game) context).returnToMenu();
                break;
        }
        return false;
    }
}