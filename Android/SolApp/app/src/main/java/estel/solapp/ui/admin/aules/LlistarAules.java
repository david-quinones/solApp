package estel.solapp.ui.admin.aules;

import static estel.solapp.common.Utility.showToast;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import estel.solapp.R;
import estel.solapp.common.CommController;
import estel.solapp.common.ValorsResposta;
import estel.solapp.models.Aula;
import estel.solapp.models.Empleat;

/********************************
 * Fragment de llistar aules
 ********************************/
public class LlistarAules extends Fragment {

    private TableLayout taulaAules;
    private boolean alternar=true;
    private int color;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_llistar_aules, container, false);

        taulaAules = view.findViewById(R.id.taula_Llista_Aules);
        taulaAules.removeAllViews();
        taulaAules.setGravity(Gravity.CENTER);
        LlistaAules();

        // Inflar Layout del fragment
        return view;

    }
    /*********************************************
     * Metode per enviar petició i mostrar resposta
     **********************************************/
    public void LlistaAules(){

        //Fem petició per agafar les dades de tots els professors
        // Creació d'unaltre fil.
        ExecutorService executor = Executors.newSingleThreadExecutor();
        // La petició es fa en unaltre fil
        Future<ValorsResposta> future = executor.submit(()->{return CommController.llistarAules();});
        // Procesar resposta del servidor
        try {

            ValorsResposta resposta = future.get();
            Gson gson= new Gson();
            Log.d("RESPOSTA LLISTAR AULES", gson.toJson(resposta));

            if (resposta==null){

                showToast(this.getActivity(),this.getContext(), "Error de conexió amb el servidor. ");

            }else{


                //Capçelera de la taula
                //*********************

                TableRow capcelera = new TableRow(getContext());
                capcelera.setGravity(Gravity.CENTER);

                TextView nomHeader= new TextView(getContext()); nomHeader.setText("AULA");
                nomHeader.setGravity(Gravity.CENTER);
                nomHeader.setBackgroundResource(R.drawable.tablas_listas_heather);
                capcelera.addView(nomHeader);
                TextView professorNomHeader= new TextView(getContext()); professorNomHeader.setText("PROFESSOR");
                professorNomHeader.setGravity(Gravity.CENTER);
                professorNomHeader.setBackgroundResource(R.drawable.tablas_listas_heather);
                capcelera.addView(professorNomHeader);
                TextView nAlumnesHeader= new TextView(getContext()); nAlumnesHeader.setText("NUMERO ALUMNES");
                nAlumnesHeader.setGravity(Gravity.CENTER);
                nAlumnesHeader.setBackgroundResource(R.drawable.tablas_listas_heather);
                capcelera.addView(nAlumnesHeader);

                taulaAules.addView(capcelera);

                //Creació de Taula amb llista d'aules
                //**************************************

                for (int i=1;i<=((int)resposta.getData(0,int.class));i++){

                    Aula aula = (Aula) resposta.getData(i,Aula.class);
                    Gson gson1= new Gson();
                    Log.d("AULA "+i, gson1.toJson(aula));
                    //Condició per alternar colors a les files

                    if (alternar) {
                        color = (R.drawable.tablas_listas_yellow);
                        alternar = false;
                    } else {
                        color = (R.drawable.tablas_listas_green);
                        alternar = true;
                    }

                    TableRow row = new TableRow(this.getContext());

                    TextView nomAula = new TextView(getContext());
                    nomAula.setText(aula.getNomAula().toString());
                    nomAula.setGravity(Gravity.CENTER);
                    nomAula.setBackgroundResource(color);
                    row.addView(nomAula);

                    TextView professor = new TextView(getContext());
                    professor.setText(aula.getEmpleat().getNom().toString()+" "+aula.getEmpleat().getCognom1().toString()+" "+aula.getEmpleat().getCognom2().toString());
                    professor.setGravity(Gravity.CENTER);
                    professor.setBackgroundResource(color);
                    row.addView(professor);

                    TextView nAlumnes = new TextView(getContext());
                    nAlumnes.setText(String.valueOf(aula.getAlumnes().size()));
                    nAlumnes.setGravity(Gravity.CENTER);
                    nAlumnes.setBackgroundResource(color);
                    row.addView(nAlumnes);

                    taulaAules.addView(row);

                    }

            }

        } catch (Exception e) {

            showToast(this.getActivity(),this.getContext(), "Error ("+e.getMessage()+")");
            Log.d("ERROR" , "Error ("+e.getMessage()+")");

        }

    }
}