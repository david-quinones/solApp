package estel.solapp.ui.admin.aules;

import static estel.solapp.common.Utility.showToast;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import estel.solapp.R;
import estel.solapp.common.CommController;
import estel.solapp.common.Utility;
import estel.solapp.common.ValorsResposta;
import estel.solapp.models.Alumne;
import estel.solapp.models.Aula;
import estel.solapp.models.Empleat;
import estel.solapp.models.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AfegirAula#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AfegirAula extends Fragment {

    private EditText nomAula;
    private Button altaBtn, esborrarBtn;
    private Empleat empleat;
    private Spinner spinnerProfessors;
    private ArrayList<Empleat> professors = new ArrayList<Empleat>();

    private ArrayList<Alumne> alumnes = new ArrayList<Alumne>();


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_afegir_aula, container, false);

        //Asignació de tots els EditText
        nomAula = view.findViewById(R.id.editTextNomAula);
        spinnerProfessors= view.findViewById(R.id.spinnerProfessors);
        omplirProfessors();//Omplim l'aaraylist de professors per escollir un

        ArrayAdapter professorsAdapter = new ArrayAdapter(this.getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,professors);
        spinnerProfessors.setAdapter(professorsAdapter);
        professors.add(0,new Empleat("Seleccioni un professor","","","","","",""));
        spinnerProfessors.setSelection(0);

        //Botó de alta de professor.
        altaBtn = view.findViewById(R.id.altaBtn);
        altaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Crida al metode per donar d'alta un aula
                afegirAula();

            }
        });

        //Botó per esborrar dades
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
     * Mètode per fer omplir l'arrayList de professors
     *********************************************************/
    private void omplirProfessors() {

        //Fem petició per agafar les dades de tots els professors
        // Creació d'unaltre fil.
        ExecutorService executor = Executors.newSingleThreadExecutor();
        // La petició es fa en unaltre fil
        Future<ValorsResposta> future = executor.submit(()->{return CommController.llistarEmpleats();});
        // Procesar resposta del servidor
        try {

            ValorsResposta resposta = future.get();

            if (resposta==null){

                showToast(this.getActivity(),this.getContext(), "Error de conexió amb el servidor. ");

            }else{
                for (int i=1;i<=((int)resposta.getData(0,int.class));i++){

                    Empleat empleat= (Empleat) resposta.getData(i,Empleat.class);

                    if (empleat.isActiu()) {//Només llistem els actius

                        professors.add(empleat);

                    }

                }
            }

        } catch (Exception e) {

            showToast(this.getActivity(),this.getContext(), "Error ("+e.getMessage()+")");
            Log.d("ERROR" , "Error ("+e.getMessage()+")");

        }
    }

     /*********************************************************
     * Mètode per fer fer peticio de donar d'alta una aula
     *********************************************************/
    public void afegirAula(){

        //Control de dades.

        String error = controlDades();

        if (!error.equals("")){

            Utility.showToast(this.getActivity(),this.getContext(),error);

        }else {//Dades correctes
            Empleat empleatEscollit = (Empleat) spinnerProfessors.getSelectedItem();

            Aula aula = new Aula(nomAula.getText().toString(),empleatEscollit,alumnes);

            // Creació d'unaltre fil.
            ExecutorService executor = Executors.newSingleThreadExecutor();
            // La petició es fa en unaltre fil
            Future<ValorsResposta> future = executor.submit(()->{return CommController.afegirAula(aula);});
            // Procesar resposta del servidor
            try {

                ValorsResposta resposta = future.get();
                Gson gson= new Gson();
                Log.d("RESPOSTA AFEGIR AULA", gson.toJson(resposta));
                if (resposta==null){

                    showToast(this.getActivity(),this.getContext(), "Error de conexió amb el servidor. ");

                }else{

                    if (resposta.getReturnCode()==CommController.OK_RETURN_CODE){//Codi correcte

                        showToast(this.getActivity(),this.getContext(), "Aula creada amb èxit");


                    }else {

                        showToast(this.getActivity(),this.getContext(), "No s'ha pogut crear l'aula");

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

        nomAula.setText("");
        spinnerProfessors.setSelection(0);

    }

    /***************************************************
     * Mètode per controlar camps buits i format de dades
     ****************************************************/
    public String controlDades(){

        String error = "";

        if (nomAula.getText().toString().isEmpty()){error = "La casella nom de l'aula és buida.\n"; }
        if (spinnerProfessors.getSelectedItemPosition()==0){error = error + "Ha de selecciona un professor"; }

        return (error);

    }
}