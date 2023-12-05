package estel.solapp.ui.admin.Alumnes;

import static estel.solapp.common.Utility.showToast;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.gson.Gson;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import estel.solapp.R;
import estel.solapp.common.CommController;
import estel.solapp.common.Utility;
import estel.solapp.common.ValorsResposta;
import estel.solapp.models.Alumne;
import estel.solapp.models.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AfegirAlumne#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AfegirAlumne extends Fragment {

    private EditText nomUsuari,contrasenya, confirmaContrasenya, nom, cognom1, cognom2, dataNaixement, nif, telefon, email;
    private Button altaBtn, esborrarBtn;

    private CheckBox menjador, acollida;
    private Alumne alumne;
    private User usuari;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_afegir_alumne, container, false);

        //Asignació de tots els EditText
        nomUsuari = view.findViewById(R.id.editTextUserName);
        contrasenya = view.findViewById(R.id.editTextContrasenya);
        confirmaContrasenya = view.findViewById(R.id.editTextConfirmaContrasenya);
        nom = view.findViewById(R.id.editTextNom);
        cognom1 = view.findViewById(R.id.editTextCognom1);
        cognom2 = view.findViewById(R.id.editTextCognom22);
        dataNaixement = view.findViewById(R.id.editTextDataNaixement);
        nif = view.findViewById(R.id.editTextDNI);
        telefon = view.findViewById(R.id.editTextTelefon);
        email = view.findViewById(R.id.editTextEmail);
        menjador= view.findViewById(R.id.cBmenjador);
        acollida = view.findViewById(R.id.cBacollida);

        //Botó de alta de professor.
        altaBtn = view.findViewById(R.id.altaBtn);
        altaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Crida al metode per donar d'alta un professor
                afegirProfessor();

            }
        });

        //Botó per tornar enrere
        esborrarBtn = view.findViewById(R.id.esborrarBtn);
        esborrarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Crida al metode per esborrar dades
                esborrarDades();

            }
        });

        // Infla el layout d'aquest fragment
        return view;
    }

    /*********************************************************
     * Mètode per fer fer peticio de donar d'alta un professor
     *********************************************************/
    public void afegirProfessor(){

        //Control de dades.

        String error = controlDades();

        if (!error.equals("")){

            Utility.showToast(this.getActivity(),this.getContext(),error);

        }else {//Dades correctes

            //Creació de Empleat i usuari per donar d'alta.
            alumne = new Alumne(nom.getText().toString(),cognom1.getText().toString(),cognom2.getText().toString(),dataNaixement.getText().toString(),
                    nif.getText().toString(),telefon.getText().toString(),email.getText().toString(),true,menjador.isChecked(),acollida.isChecked());
            usuari = new User(nomUsuari.getText().toString(),contrasenya.getText().toString(),false,false,true);

            // Creació d'unaltre fil.
            ExecutorService executor = Executors.newSingleThreadExecutor();
            // La petició es fa en unaltre fil
            Future<ValorsResposta> future = executor.submit(()->{return CommController.afegirAlumne(alumne, usuari);});
            // Procesar resposta del servidor
            try {

                ValorsResposta resposta = future.get();
                Gson gson= new Gson();
                Log.d("RESPOSTA ALTA ALUMNE", gson.toJson(resposta));
                if (resposta==null){

                    showToast(this.getActivity(),this.getContext(), "Error de conexió amb el servidor. ");

                }else{

                    if (resposta.getReturnCode()==CommController.OK_RETURN_CODE){//Codi correcte

                        showToast(this.getActivity(),this.getContext(), "Alumne donat d'alta amb èxit");


                    }else {

                        showToast(this.getActivity(),this.getContext(), "No s'ha pogut donar d'alta l'alumne");

                    }
                }
            } catch (ExecutionException e) {
                showToast(this.getActivity(),this.getContext(), "Error ("+e.getMessage()+")");
            } catch (InterruptedException e) {
                showToast(this.getActivity(),this.getContext(), "Error ("+e.getMessage()+")");
            }

        }

    }

    /***************************************************
     * Mètode per borrar les dades
     ****************************************************/
    public void esborrarDades(){

        nomUsuari.setText("");contrasenya.setText("");confirmaContrasenya.setText("");nom.setText("");cognom1.setText("");cognom2.setText("");dataNaixement.setText("");
        nif.setText("");telefon.setText("");email.setText("");menjador.setChecked(false);acollida.setChecked(false);

    }

    /***************************************************
     * Mètode per controlar camps buits i format de dades
     ****************************************************/
    public String controlDades(){

        String error = "";

        if (nomUsuari.getText().toString().isEmpty()){error = "La casella nom d'usuari és buida.\n"; }
        if (contrasenya.getText().toString().isEmpty()){error = error + "La casella contrasenya és buida.\n"; }
        if ((!contrasenya.getText().toString().equals(confirmaContrasenya.getText().toString()))){error = error + "Les contrasenyes no coincideixen."; }
        if (nom.getText().toString().isEmpty()){error = "La casella nom és buida.\n"; }
        if (cognom1.getText().toString().isEmpty()){error = error + "La casella Primer cognom és buida.\n"; }
        if (cognom2.getText().toString().isEmpty()){error = error + "La casella Segón cognom és buida.\n"; }
        if (dataNaixement.getText().toString().isEmpty()){error = error + "La casella Data de naixement és buida.\n"; }
        if (!Utility.validarData(dataNaixement.getText().toString())) {error= error + "El format de la data ha de ser aaaa-mm-dd";}
        if (nif.getText().toString().isEmpty()){error = error + "La casella NIF és buida.\n"; }
        //if (!Utility.vailidarNifNie(nif.getText().toString())){error=error + "El format del NIF no és vàlid.\n";}
        if (telefon.getText().toString().isEmpty()){error = error + "La casella Telèfon es buida.\n"; }
        if (email.getText().toString().isEmpty()){error = error + "La casella Email es buida.\n"; }
        if (!Utility.validarEmail(email.getText().toString())) {error = error + "El emailintroduït no és correcte";}

        return (error);

    }
}