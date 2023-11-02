package estel.solapp.activities;

import static estel.solapp.common.Utility.showToast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import estel.solapp.ui.admin.NavGestioUsuarisActivity;
import estel.solapp.R;
import estel.solapp.common.CommController;
import estel.solapp.common.SingletonSessio;
import estel.solapp.common.Utility;
import estel.solapp.common.ValorsResposta;

/**
 * Classe controladora de menú principal d'usuaris admin
 * @author Juan Antonio
 */
public class HomeAdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);

        //Benvinguda amb el nom d'usuari logat
        TextView benvinguda;
        benvinguda =((TextView) findViewById(R.id.textBenvinguda));
        benvinguda.setText("Usuari: "+SingletonSessio.getInstance().getUserConnectat().getNomUsuari());
    }

    /*************************************************************
     *
     * mETODES PER OBRIR LES VISTES CORRESPONENT ALS EVENTS DE BOTÓ
     *
     **************************************************************/

    /**
     * Click per anar
     * a la informació del perfil d'usuari
     * @param view vista
     */
    public void goPerfil(View view){

        Utility.gotoActivityChild(this, PerfilActivity.class);
    }
    /**
     * Click per anar
     * al menú navigation de gestió d'usuaris
     * @param view vista
     */
    public void goGestioUsiaris(View view){

        Utility.gotoActivityChild(this, NavGestioUsuarisActivity.class);
    }

    /**
     * Click per anar
     * al menú navigation de gestió de professors
     * @param view vista
     */
    public void goGestioProfessors(View view){

        Utility.gotoActivityChild(this, NavGestioUsuarisActivity.class);
    }

    /**
     * Click per anar
     * al menú navigation de gestió de alumnes
     * @param view vista
     */
    public void goGestioAlumnes(View view){

        Utility.gotoActivityChild(this, NavGestioUsuarisActivity.class);
    }

    /**
     * Click per anar
     * al menú navigation de gestió de aules
     * @param view vista
     */
    public void goGestioAules(View view){

        Utility.gotoActivityChild(this, NavGestioUsuarisActivity.class);
    }

    /**
     * Click per anar
     * al menú navigation de gestió de comunicacions
     * @param view vista
     */
    public void goGestioComunicacions(View view){

        Utility.gotoActivityChild(this, NavGestioUsuarisActivity.class);
    }

    /**
     * Click per
     * Tancar sessió
     * @param view vista
     */
    public void doLogout(View view){

        Context context = getApplicationContext();
        HomeAdminActivity parent = this;

        Button btn = (Button) findViewById(R.id.logoutBtn);

        // La petició al servidor es fa en unaltre fil
        ExecutorService executor = Executors.newSingleThreadExecutor();
        btn.setEnabled(false);
        Future<ValorsResposta> future = executor.submit(() -> {return CommController.doLogout();});

        // Procesar resposta del servidor
        try {
            ValorsResposta resposta=future.get();
            Gson gson= new Gson();//creació de Json per mostrar al log
            Log.d("RESPOSTA LOGOUT", gson.toJson(resposta)); //Log per controlar el que rebem del server

            if (resposta==null){showToast(parent,context, "Error de conexió amb el servidor. "); //Si no obtenim resposta es que no hi ha conexió.

            }else {

                if (resposta.getReturnCode() == CommController.OK_RETURN_CODE) {//Si el codi de resposta es correcte
                    Utility.gotoActivity(this, LoginActivity.class);
                    SingletonSessio.getInstance().closeConnection();

                } else {
                    showToast(parent, context, "Error tancant sessió!"); //Si el codi no es correcte mostrar missatge error
                }
            }
        } catch (ExecutionException | InterruptedException e) {
            showToast(parent, context, "Error (" + e.getMessage() + ")");//Mostrar missatge error si ha fallat
        } finally {
            btn.setEnabled(true);
        };

    }

    /**
     * Avis amb un missatge si es vol tancar l'aplicació sense fer logout
     */
    @Override
    public void onBackPressed() {

        showToast(this, getApplicationContext(), "S'ha de sortir per tancar sessió");
    }
}