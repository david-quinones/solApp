package estel.solapp.ui.admin.aules;

import static estel.solapp.common.Utility.showToast;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import estel.solapp.R;
import estel.solapp.common.CommController;
import estel.solapp.common.ValorsResposta;
import estel.solapp.models.Aula;
import estel.solapp.models.Empleat;


public class EliminarAula extends Fragment {

    private TextView nomAula,professor,nAlumnes;
    private Button eliminar;
    private TableLayout taulaAules;
    private boolean alternar=true;
    private int color;
    private Aula aula;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_eliminar_aula, container, false);

        //Asignació de tots els TextView
        nomAula = view.findViewById(R.id.textViewAula);
        professor = view.findViewById(R.id.textViewProfessor);
        nAlumnes = view.findViewById(R.id.textViewNAlumnes);
        taulaAules = view.findViewById(R.id.taula_elimina_aula);
        taulaAules.removeAllViews();

        llistarAules();//Mostra la llista de professors per escollir
        eliminar = view.findViewById(R.id.eliminarBtn);
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertEliminarAula();

            }
        });

        // Inflador de layout pel fragment
        return view;
    }

    /*********************************************
     * Metode per enviar petició i mostrar resposta
     * Per llistar aules
     **********************************************/
    public void llistarAules(){

        //Fem petició per agafar les dades de tots els professors
        // Creació d'unaltre fil.
        ExecutorService executor = Executors.newSingleThreadExecutor();
        // La petició es fa en unaltre fil
        Future<ValorsResposta> future = executor.submit(()->{return CommController.llistarAules();});
        // Procesar resposta del servidor
        try {

            ValorsResposta resposta = future.get();

            if (resposta==null){

                showToast(this.getActivity(),this.getContext(), "Error de conexió amb el servidor. ");

            }else{

                taulaAules.removeAllViews();
                //Capçelera de la taula
                //*********************

                TableRow capcelera = new TableRow(getContext());

                TextView aulaHeader= new TextView(getContext()); aulaHeader.setText("AULA");
                aulaHeader.setGravity(Gravity.CENTER);
                aulaHeader.setBackgroundResource(R.drawable.tablas_listas_heather);
                capcelera.addView(aulaHeader);
                TextView professorHeader= new TextView(getContext()); professorHeader.setText("PROFESSOR");
                professorHeader.setGravity(Gravity.CENTER);
                professorHeader.setBackgroundResource(R.drawable.tablas_listas_heather);
                capcelera.addView(professorHeader);
                TextView nAlumnesHeader= new TextView(getContext()); nAlumnesHeader.setText("NUMERO ALUMNES");
                nAlumnesHeader.setGravity(Gravity.CENTER);
                nAlumnesHeader.setBackgroundResource(R.drawable.tablas_listas_heather);
                capcelera.addView(nAlumnesHeader);


                taulaAules.addView(capcelera);

                //Creació de Taula amb llista d'aules
                //**************************************

                for (int i=1;i<=((int)resposta.getData(0,int.class));i++){

                    Aula aulaTaula= (Aula) resposta.getData(i,Aula.class);

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
                        nomAula.setText(aulaTaula.getNomAula());
                        nomAula.setGravity(Gravity.CENTER);
                        nomAula.setBackgroundResource(color);
                        row.addView(nomAula);
                        TextView professor = new TextView(getContext());
                        professor.setText(aulaTaula.getEmpleat().toString());
                        professor.setGravity(Gravity.CENTER);
                        professor.setBackgroundResource(color);
                        row.addView(professor);
                        TextView nAlumnes = new TextView(getContext());
                        nAlumnes.setText(String.valueOf(aulaTaula.getAlumnes().size()));
                        nAlumnes.setGravity(Gravity.CENTER);
                        nAlumnes.setBackgroundResource(color);
                        row.addView(nAlumnes);

                        //Fem clicable la fila de la taula i cridem al métode de mostrar les dades
                        //Pasant-li l'aula de la fila
                        row.setClickable(true);
                        row.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mostrarDades(aulaTaula);
                            }
                        });
                        taulaAules.addView(row);
                    }

                }


        } catch (Exception e) {

            showToast(this.getActivity(),this.getContext(), "Error ("+e.getMessage()+")");
            Log.d("ERROR" , "Error ("+e.getMessage()+")");

        }

    }

    /***************************************************************
     * Metode per mostrar les dades de l'empleat escollit a la taula
     * @param aulaTaula
     **********************************************/
    public void mostrarDades (Aula aulaTaula){

        aula =aulaTaula;
        nomAula.setText(aula.getNomAula());
        professor.setText(aula.getEmpleat().toString());
        nAlumnes.setText(String.valueOf(aula.getAlumnes().size()));

    }

    /*********************************************
     * Metode per mostrar confirmació d'eliminar
     *********************************************/
    public void alertEliminarAula(){

        if (nomAula.getText().length()==0) {

            showToast(this.getActivity(), this.getContext(), "Seleccioni un professor de la llista. ");

        }else {

            // Dialeg per preguntar a l'usuari si vol eliminar el professor

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.getContext());
            alertDialog.setMessage("Estàs segur que vols eliminar l'aula?");
            alertDialog.setTitle("Atenció!");
            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("Si", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    // Resposta si crida a eliminar professor
                    eliminarAula();

                }
            });
            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    // Resposta "no" no fa res
                }
            });

            // Obrir dialeg
            alertDialog.show();

        }

    }

    /********************************************
     * Metode per enviar petició d'eliminar aula
     ********************************************/
    public void eliminarAula(){

        // Creació d'unaltre fil.
        ExecutorService executor = Executors.newSingleThreadExecutor();
        // La petició es fa en unaltre fil
        Future<ValorsResposta> future = executor.submit(()->{return CommController.eliminarAula(aula);});
        // Procesar resposta del servidor
        try {

            ValorsResposta resposta = future.get();
            Gson gson= new Gson();
            Log.d("RESPOSTA ELIMINA AULA", gson.toJson(resposta));
            if (resposta==null){

                showToast(this.getActivity(),this.getContext(), "Error de conexió amb el servidor. ");

            }else{

                if (resposta.getReturnCode()==CommController.OK_RETURN_CODE){//Codi correcte

                    showToast(this.getActivity(),this.getContext(), "Aula eliminada");
                    llistarAules();

                }else {

                    showToast(this.getActivity(),this.getContext(), "No s'ha pogut eliminar l'aula\n L'aula ha de ser buida d'alumnes.");

                }
            }
        } catch (ExecutionException e) {
            showToast(this.getActivity(),this.getContext(), "Error ("+e.getMessage()+")");
        } catch (InterruptedException e) {
            showToast(this.getActivity(),this.getContext(), "Error ("+e.getMessage()+")");
        }

    }
}