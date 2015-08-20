package johnserrano.tank;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class GameOverDialog extends Dialog implements View.OnTouchListener
{
    Context context;
    Button resumeButton;
    Button quitButton;
    int score;

    public GameOverDialog(Context context, int score)
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

        this.score = score;
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

        TextView scoreView = (TextView) findViewById(R.id.scoreText);
        scoreView.setText("" + score);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        switch (v.getId()){
            case R.id.restart_button:
                ((Activity) context).recreate();
                dismiss();
                break;
            case R.id.quit_button:
                //go to main menu
                ((Game) context).returnToMenu();
                break;
        }
        return false;
    }
}