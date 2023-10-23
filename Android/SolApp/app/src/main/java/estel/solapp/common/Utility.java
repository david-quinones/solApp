package estel.solapp.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Utility {

    public static void showToast(Context context, String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

    /**
     * Since a secondary thread, shows a toast with a concrete message, size and context
     * @param activity activity that will show the toast
     * @param context toast context
     * @param message toast message
     */
    public static void showToast(Activity activity, Context context, String message){
        activity.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * opens an activity and finishes parent activity
     * @param parent  parent activity
     * @param destination destination activity
     */
    public static void gotoActivity(AppCompatActivity parent, Class destination){
        Intent i = new Intent(parent, destination);
        parent.startActivity(i);
        parent.finish();
    }
}
