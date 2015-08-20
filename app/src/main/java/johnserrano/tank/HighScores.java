package johnserrano.tank;

import android.app.Activity;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class HighScores extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        for (int i = 0; i < 10; i++){
            int score = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt("HighScore" + i, 0);
            String identifier = "high_score_" + i;
            int resourceID = getResources().getIdentifier(identifier, "id", "johnserrano.tank");
            TextView tv = (TextView) findViewById(resourceID);
            tv.setText("" + (i+1) + ": " + score);
        }


    }

}
