package estel.solapp.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            public void run() {Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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

    public static void gotoActivityChild(AppCompatActivity parent, Class destination){

        Intent i = new Intent(parent, destination);
        parent.startActivity(i);

    }

    /*************************************
    * Metode per validar el format del DNI
    * @param nif
    * Return si es un NIF vàlid
    **************************************/
    public static boolean vailidarNifNie(String nif){

        //si es NIE, eliminar la x,y,z inicial per tratarlo com nif
        if (nif.toUpperCase().startsWith("X")||nif.toUpperCase().startsWith("Y")||nif.toUpperCase().startsWith("Z"))
            nif = nif.substring(1);
            Pattern nifPattern = Pattern.compile("(\\d{1,8})([TRWAGMYFPDXBNJZSQVHLCKEtrwagmyfpdxbnjzsqvhlcke])");
            Matcher m = nifPattern.matcher(nif);
        if(m.matches()){
            String letra = m.group(2);
        //Extreure lletra del NIF
            String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
            int dni = Integer.parseInt(m.group(1));
            dni = dni % 23;
            String reference = letras.substring(dni,dni+1);

            if (reference.equalsIgnoreCase(letra)){

                return true;

            }else{

                return false;
            }
        }
        else
            return false;
    }

    /****************************************************
     * Metode per validar el format de la data
     * @param data
     * Return si es valid el format de la data aaaa-mm-dd
     ****************************************************/
    public static Boolean validarData(String data) {

        Pattern pattern = Pattern
                .compile ("^\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])$");

        Matcher mather = pattern.matcher(data);

        return (mather.find());
    }

    /*****************************************
     * Metode per validar el format de l'email
     * @param email
     * Return si es valid el email
     *****************************************/
    public static boolean validarEmail (String email){

        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

        Matcher mather = pattern.matcher(email);

        return (mather.find());

    }

    /****************************************
     * Metode per codificar les contrasenyes
     * @param password
     * Return String contrasenya codificada
     **************************************/

    public static String codificar(String password) {
        int desplacament = 4; //moviments que realitzarem al carcater dins de ASCII
        //construir la cadena codificada
        StringBuilder passwordCodificat = new StringBuilder();
        //recorrem cada caracter del password
        for (char c : password.toCharArray()) {
            //convertim el caracter actual a ASCII
            int caracterAscii = (int) c;
            //movem el caracter segons la variable "desplacament", asegurant que sigui un ASCII imprimible
            caracterAscii = (caracterAscii + desplacament) % 128;
            //agregem el caracter desplaçat a la cadna
            passwordCodificat.append((char) caracterAscii);
        }
        //retornem el StringBuild pero a String
        return passwordCodificat.toString();
    }





}
