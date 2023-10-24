package estel.solapp.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
/**
 * Classe d'utilitats pels toasts o tancar i obrir fienstres (intents)
 * @author Juan Antonio
 */
public class Utility {

    public static void showToast(Context context, String message){

        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();

    }

    /**
     * Desde un fil secundari, mostra un toast amb un missatge concret, mida i context
     * @param activity activitat on es mostra el toast
     * @param context context del toast
     * @param message missatge que mostrarà el toast
     */
    public static void showToast(Activity activity, Context context, String message){

        activity.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * Obre una activitat i tanca l'activitat pare
     * @param parent  activitat pare que es tancarà
     * @param destination activitat destí que s'obrirà
     */
    public static void gotoActivity(AppCompatActivity parent, Class destination){

        Intent i = new Intent(parent, destination);
        parent.startActivity(i);
        parent.finish();
    }

}
