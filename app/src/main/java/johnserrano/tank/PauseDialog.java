package johnserrano.tank;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class PauseDialog extends Dialog implements View.OnTouchListener
{
    Context context;
    Button resumeButton;
    Button quitButton;

    public PauseDialog(Context context)
    {
        super(context);
        this.context = context;

        setCancelable(false);

        final PauseDialog t = this; //for ondismisslistener to get context

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
        setContentView(R.layout.pause_dialog);

        resumeButton = (Button) findViewById(R.id.resume_button);
        quitButton = (Button) findViewById(R.id.quit_button);

        resumeButton.setOnTouchListener(this);
        quitButton.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        switch (v.getId()){
            case R.id.resume_button:
                dismiss();
                break;
            case R.id.quit_button:
                //go to main menu
                //TODO: ontouch quit
                break;
        }
        return false;
    }
}