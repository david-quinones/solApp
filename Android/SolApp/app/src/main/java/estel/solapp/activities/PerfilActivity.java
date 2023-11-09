package estel.solapp.activities;

import static estel.solapp.common.Utility.showToast;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import estel.solapp.R;
import estel.solapp.common.CommController;
import estel.solapp.common.SingletonSessio;
import estel.solapp.common.Utility;
import estel.solapp.common.ValorsResposta;
import estel.solapp.models.Persona;

public class PerfilActivity extends AppCompatActivity {

    private EditText nomUsuari, nom, cognom1, cognom2, dataNaixement, nif, telefon, email;
    private  Button modificarPerfilBtn, mofificarBtn, enrereBtn;
    private int userId;

    private Persona persona;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        //Benvinguda amb el nom d'usuari logat
        TextView benvinguda;
        benvinguda =((TextView) findViewById(R.id.textViewPerfil));
        benvinguda.setText("Usuari: "+ SingletonSessio.getInstance().getUserConnectat().getNomUsuari());

        //Asignació de tots els EditText
        nomUsuari = (EditText) findViewById(R.id.editTextUserName);
        nomUsuari.setText(SingletonSessio.getInstance().getUserConnectat().getNomUsuari());
        nom = (EditText) findViewById(R.id.editTextNom);
        cognom1 = (EditText) findViewById(R.id.editTextCognom1);
        cognom2 = (EditText) findViewById(R.id.editTextCognom2);
        dataNaixement = (EditText) findViewById(R.id.editTextDataNaixement);
        nif = (EditText) findViewById(R.id.editTextNIF);
        telefon = (EditText) findViewById(R.id.editTextTelefon);
        email = (EditText) findViewById(R.id.editTextEmail);

        //Crida al mètode per mostrar les dades del perfil

        MostraPerfil();

        //Botó de modificar perfil fa editable els edittext

        modificarPerfilBtn= (Button)findViewById(R.id.modificarPerfilBtn);
        modificarPerfilBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mofificarBtn.setVisibility(View.VISIBLE);
                nom.setFocusableInTouchMode(true);
                cognom1.setFocusableInTouchMode(true);
                cognom2.setFocusableInTouchMode(true);
                dataNaixement.setFocusableInTouchMode(true);
                nif.setFocusableInTouchMode(true);
                telefon.setFocusableInTouchMode(true);
                email.setFocusableInTouchMode(true);

            }
        });

        //Botó de confirmar canvis fa la modificació del perfil.
        mofificarBtn = (Button)findViewById(R.id.modificarlBtn);
        mofificarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Crida al metode per modificar les dades al servidor
               ModificaPerfil();

            }
        });

        //Botó per tornar enrere
        enrereBtn = (Button)findViewById(R.id.enrereBtn);
        enrereBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Crida al metode per tornar enrere
               onBackPressed();

            }
        });

    }
    /***************************************
    *Métode per mostrar les dades del perfil
    * **************************************/
    public void MostraPerfil(){

        //Fem petició per agafar les dades de l'usuari
        // Creació d'unaltre fil.
        ExecutorService executor = Executors.newSingleThreadExecutor();
        // La petició es fa en unaltre fil
        Future<ValorsResposta> future = executor.submit(()->{return CommController.consultaPerfil();});
        // Procesar resposta del servidor
        try {
            ValorsResposta resposta = future.get();
            Gson gson= new Gson();
            Log.d("RESPOSTA CONSULTAR PERFIL", gson.toJson(resposta));
            if (resposta==null){

                showToast(this,this, "Error de conexió amb el servidor. ");

            }else{

                //Creem una instancia de persona amb les dades de la resposta del servidor
                persona = (Persona)(resposta.getData(0, Persona.class));
                userId = persona.getIdPersona();
                nom.setText(persona.getNom());
                cognom1.setText(persona.getCognom1());
                cognom2.setText(persona.getCognom2());
                dataNaixement.setText(persona.getData_naixement());
                nif.setText(persona.getDni());
                telefon.setText(persona.getTelefon());
                email.setText(persona.getMail());

            }

        } catch (Exception e) {

            showToast(this,this, "Error ("+e.getMessage()+")");
        }

    }

    /*******************************************
     * Métode per modificar les dades del perfil
     *******************************************/
    public void ModificaPerfil() {

        //Control de dades.

        String error = controlDades();

        if (!error.equals("")){

            Utility.showToast(this,this,error);

        }else {//Dades correctes

            //Creació de Persona per modificar.
            persona=new Persona(userId,nom.getText().toString(),cognom1.getText().toString(),cognom2.getText().toString(),dataNaixement.getText().toString(),
                    nif.getText().toString(),telefon.getText().toString(),email.getText().toString());

            // Creació d'unaltre fil.
            ExecutorService executor = Executors.newSingleThreadExecutor();
            // La petició es fa en unaltre fil
            Future<ValorsResposta> future = executor.submit(()->{return CommController.modificaPerfil(persona);});
            // Procesar resposta del servidor
            try {

                ValorsResposta resposta = future.get();
                Gson gson= new Gson();
                Log.d("RESPOSTA MODIFICAR PERFIL", gson.toJson(resposta));
                if (resposta==null){

                    showToast(this,this, "Error de conexió amb el servidor. ");

                }else{

                    if (resposta.getReturnCode()==CommController.OK_RETURN_CODE){//Codi correcte

                        showToast(this,this, "Dades modificades");
                        //Tornem a deixar com NO editables els EditText
                        mofificarBtn.setClickable(false);
                        nom.setFocusableInTouchMode(false);
                        cognom1.setFocusableInTouchMode(false);
                        cognom2.setFocusableInTouchMode(false);
                        dataNaixement.setFocusableInTouchMode(false);
                        nif.setFocusableInTouchMode(false);
                        telefon.setFocusableInTouchMode(false);
                        email.setFocusableInTouchMode(false);
                        onBackPressed();

                    }else {

                        showToast(this,this, "No s'han pogut modificar les dades");

                    }
                }
            } catch (ExecutionException e) {
                showToast(this,this, "Error ("+e.getMessage()+")");
            } catch (InterruptedException e) {
                showToast(this,this, "Error ("+e.getMessage()+")");
            }

        }

    }
    public String controlDades(){

        String error = "";

        if (nom.getText().toString().isEmpty()){error = "La casella nom és buida.\n"; }
        if (cognom1.getText().toString().isEmpty()){error = error + "La casella Primer cognom és buida.\n"; }
        if (cognom2.getText().toString().isEmpty()){error = error + "La casella Segón cognom és buida.\n"; }
        if (dataNaixement.getText().toString().isEmpty()){error = error + "La casella Data de neiement és buida.\n"; }
        if (nif.getText().toString().isEmpty()){error = error + "La casella NIF és buida.\n"; }
        if (!Utility.vailidarNifNie(nif.getText().toString())){error=error + "El format del NIF no és vàlid.\n"; }
        if (dataNaixement.getText().toString().isEmpty()){error = error + "La casella de data de neixement és buida.\n"; }
        if (telefon.getText().toString().isEmpty()){error = error + "La casella Telèfon es buida.\n"; }
        if (email.getText().toString().isEmpty()){error = error + "La casella Email es buida.\n"; }

        return (error);

    }

}