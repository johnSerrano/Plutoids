package johnserrano.tank;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Stats {

    static void logHighScore(Context context, int highScore) {
        int index = 0;
        int[] scores = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        //open shared preferences file
        SharedPreferences highScoresSP = PreferenceManager.getDefaultSharedPreferences(context);

        //read all high scores
        for (int i = 0; i < 10; i++) {
            String key = "HighScore" + i;
            scores[i] = highScoresSP.getInt(key, -1);
        }

        //sort array in case somehow it got unsorted
        quicksort(scores, 0, 9);

        //find index into which new high score should be inserted
        for (int i = 0; i < 10 && highScore < scores[i]; i++)
            index++;

        //if index == 10, highScore is not a high score
        if(index >= 10) return;

        //move down all values lower than or equal to index down one step
        for (int i = 9; i > index; i--)
            scores[i] = scores[i-1];

        //insert highScore into index
        scores[index] = highScore;

        SharedPreferences.Editor editor = highScoresSP.edit();

        //write array to shared preferences
        for (int i = 0; i<10; i++) {
            String key = "HighScore" + i;
            editor.putInt(key, scores[i]);
        }

        boolean commited = editor.commit();

        if (commited) ;
    }

    static private void quicksort(int[] arr, int l, int h)
    {
        int index = l;
        int right = h;
        int tmp;
        int pivot = arr[(l+h)/2];

        while (index <= right) {
            while (arr[index] > pivot)
                index++;

            while (arr[right] < pivot)
                right--;

            if (index <= right) {
                tmp = arr[index];
                arr[index] = arr[right];
                arr[right] = tmp;
                index++;
                right--;
            }
        }

        if (l < index-1)
            quicksort(arr, l, index-1);
        if (index <
                h)
            quicksort(arr, index, h);
    }
}
