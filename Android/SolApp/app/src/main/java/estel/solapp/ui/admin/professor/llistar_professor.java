package estel.solapp.ui.admin.professor;

import static estel.solapp.common.Utility.showToast;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import estel.solapp.R;
import estel.solapp.common.CommController;
import estel.solapp.common.ValorsResposta;
import estel.solapp.models.Empleat;
import estel.solapp.models.Persona;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class llistar_professor extends Fragment {

    private TableLayout taulaProfessors;
    private ArrayList<Empleat> professors= new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_llistar_professor, container, false);

        taulaProfessors = view.findViewById(R.id.taula_Llista_Professors);
        taulaProfessors.removeAllViews();
        LlistarProfessors();

        // Inflate the layout for this fragment
        return view;

    }

    public void LlistarProfessors(){

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

                    Log.d("EMPLEAT "+ i , empleat.getNom());

                    TableRow row = new TableRow(this.getContext());

                    TextView nom = new TextView(getContext());
                    nom.setText(empleat.getNom());
                    row.addView(nom);
                    TextView cognom1 = new TextView(getContext());
                    cognom1.setText(empleat.getCognom1());
                    row.addView(cognom1);
                    TextView cognom2 = new TextView(getContext());
                    cognom2.setText(empleat.getCognom2());
                    row.addView(cognom2);
                    TextView telefon = new TextView(getContext());
                    telefon.setText(empleat.getTelefon());
                    row.addView(telefon);
                    TextView email = new TextView(getContext());
                    email.setText(empleat.getMail());
                    row.addView(email);
                    TextView dataInici = new TextView(getContext());
                    email.setText(empleat.getIniciContracte());
                    row.addView(dataInici);
                    TextView dataFi = new TextView(getContext());
                    email.setText(empleat.getFinalContracte());
                    row.addView(dataFi);
                    taulaProfessors.addView(row);

                }
            }

        } catch (Exception e) {

            showToast(this.getActivity(),this.getContext(), "Error ("+e.getMessage()+")");
            Log.d("ERROR" , "Error ("+e.getMessage()+")");
        }

    }


}