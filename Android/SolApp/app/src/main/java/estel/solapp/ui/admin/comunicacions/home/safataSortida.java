package estel.solapp.ui.admin.comunicacions.home;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
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
import estel.solapp.common.ValorsResposta;
import estel.solapp.models.Missatge;
import estel.solapp.models.Persona;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link safataSortida#newInstance} factory method to
 * create an instance of this fragment.
 */
public class safataSortida extends Fragment {

    EditText contingut;
    TextView  datEnviament;

    ArrayList<Persona> destinataris;
    Spinner spinnerDestinataris;
    ArrayAdapter<Persona> adaptadorDestinataris;
    TableLayout taulaMissatgesEnv;
    Button eliminarBtn;
    private boolean alternar = true;
    private boolean set;
    private int color;
    private Missatge missatge;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_safata_sortida, container, false);

        contingut = view.findViewById(R.id.MultiLineContingutEnv);
        taulaMissatgesEnv = view.findViewById(R.id.taula_Llista_Missatge_env);
        spinnerDestinataris = view.findViewById(R.id.spinnerDestinataris);
        datEnviament = view.findViewById(R.id.textViewDataEnv);

        omplirTaula(); //fem petició de rebre tots el missatges de l'usuari

        eliminarBtn = view.findViewById(R.id.eliminarBtn2);
        eliminarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertEliminarMissatge();

            }
        });



        // Inflate the layout for this fragment
        return view;
    }

    /*********************************************
     * Metode per enviar petició i mostrar resposta
     * Per llistar aules
     **********************************************/
    public void omplirTaula() {

        //Fem petició per agafar les dades de tots els professors
        // Creació d'unaltre fil.
        ExecutorService executor = Executors.newSingleThreadExecutor();
        // La petició es fa en unaltre fil
        Future<ValorsResposta> future = executor.submit(()->{return CommController.safataSortida();});
        // Procesar resposta del servidor
        try {

            ValorsResposta resposta = future.get();
            Gson gson= new Gson();
            Log.d("RESPOSTA MISSATGES REBUTS", gson.toJson(resposta));

            if (resposta==null){

                showToast(this.getActivity(),this.getContext(), "Error de conexió amb el servidor. ");

            }else{

                taulaMissatgesEnv.removeAllViews();
                //Capçelera de la taula
                //*********************

                TableRow capcelera = new TableRow(getContext());
                capcelera.setGravity(Gravity.CENTER);

                TextView destinatarisHeader= new TextView(getContext()); destinatarisHeader.setText("DESTINATARIS");
                destinatarisHeader.setGravity(Gravity.CENTER);
                destinatarisHeader.setBackgroundResource(R.drawable.tablas_listas_heather);
                capcelera.addView(destinatarisHeader);
                TextView dataEnviamentHeader= new TextView(getContext()); dataEnviamentHeader.setText("DATA ENVIAMENT");
                dataEnviamentHeader.setGravity(Gravity.CENTER);
                dataEnviamentHeader.setBackgroundResource(R.drawable.tablas_listas_heather);
                capcelera.addView(dataEnviamentHeader);
                taulaMissatgesEnv.addView(capcelera);

                //Creació de Taula amb llista d'aules
                //**************************************

                for (int i=1;i<=((int)resposta.getData(0,int.class));i++){

                    Missatge missatgeTaula = (Missatge) resposta.getData(i,Missatge.class);
                    Gson gson1= new Gson();
                    Log.d("MISSATGE "+i, gson1.toJson(missatgeTaula));
                    //Condició per alternar colors a les files
                    if (alternar) {
                        color = (R.drawable.tablas_listas_yellow);
                        alternar = false;
                    } else {
                        color = (R.drawable.tablas_listas_green);
                        alternar = true;
                    }

                    TableRow row = new TableRow(this.getContext());
                    row.setGravity(Gravity.CENTER);

                    Spinner spinnerDestinatarisTaula = new Spinner(getContext());
                    ArrayList<Persona> destinatarisTaula = new ArrayList<>(missatgeTaula.getDestinataris());
                    ArrayAdapter<Persona> adaptadorDestinatarisTaula = new ArrayAdapter(this.getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,destinatarisTaula);
                    spinnerDestinatarisTaula.setAdapter(adaptadorDestinatarisTaula);
                    spinnerDestinatarisTaula.setBackgroundResource(color);
                    row.addView(spinnerDestinatarisTaula);

                    TextView dataEnviament = new TextView(getContext());
                    dataEnviament.setText(missatgeTaula.getDataEnviament().toString());
                    dataEnviament.setGravity(Gravity.CENTER);
                    dataEnviament.setBackgroundResource(color);
                    row.addView(dataEnviament);

                    //Fem clicable la fila de la taula i cridem al métode de mostrar les dades
                    //Pasant-li l'aula de la fila
                    row.setClickable(true);
                    row.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            missatge = (missatgeTaula);
                            mostrarDades (missatgeTaula);

                        }
                    });

                    taulaMissatgesEnv.addView(row);

                }

            }

        } catch (Exception e) {

            showToast(this.getActivity(),this.getContext(), "Error ("+e.getMessage()+")");
            Log.d("ERROR" , "Error ("+e.getMessage()+")");

        }

    }
    /***************************************************************
     * Metode per mostrar les dades del missatge escillit a la taula
     * @param missatgeTaula
     ***************************************************************/
    public void mostrarDades(Missatge missatgeTaula) {

        missatge = missatgeTaula;
        destinataris = missatge.getDestinataris();
        adaptadorDestinataris = new ArrayAdapter(this.getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,destinataris);
        spinnerDestinataris.setAdapter(adaptadorDestinataris);
        datEnviament.setText(missatge.getDataEnviament().toString());
        contingut.setText(missatge.getContingut());

    }

    /*********************************************
     * Metode per mostrar confirmació d'eliminar
     *********************************************/
    public void alertEliminarMissatge(){

        if (contingut.getText().length()==0) {

            showToast(this.getActivity(), this.getContext(), "Cap missatge seleccionat per eliminar");

        }else {

            // Dialeg per preguntar a l'usuari si vol eliminar el professor

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.getContext());
            alertDialog.setMessage("Estàs segur que vols eliminar el missatge?");
            alertDialog.setTitle("Atenció!");
            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("Si", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    // Resposta si crida a eliminar missatge
                    eliminarMissatge();

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

    /***********************************************
     * Metode per enviar petició d'eliminar missatge
     ***********************************************/
    public void eliminarMissatge(){

        if (contingut.getText().length()==0){//Control de missatge seleccionat

            showToast(this.getActivity(),this.getContext(), "Seleccioni un missatge per eliminar");

        }else {

            // Creació d'unaltre fil.
            ExecutorService executor = Executors.newSingleThreadExecutor();
            // La petició es fa en unaltre fil
            Future<ValorsResposta> future = executor.submit(() -> {
                return CommController.eliminarMissatge(missatge);
            });
            // Procesar resposta del servidor
            try {

                ValorsResposta resposta = future.get();
                Gson gson = new Gson();
                Log.d("RESPOSTA ELIMINA MISSATGE", gson.toJson(resposta));
                if (resposta == null) {

                    showToast(this.getActivity(), this.getContext(), "Error de conexió amb el servidor. ");

                } else {

                    if (resposta.getReturnCode() == CommController.OK_RETURN_CODE) {//Codi correcte

                        showToast(this.getActivity(), this.getContext(), "Missatge eliminat");
                        destinataris.clear();
                        datEnviament.setText("");
                        contingut.setText("");
                        omplirTaula();

                    } else {

                        showToast(this.getActivity(), this.getContext(), "No s'ha pogut eliminar el missatge");

                    }
                }
            } catch (ExecutionException e) {
                showToast(this.getActivity(), this.getContext(), "Error (" + e.getMessage() + ")");
            } catch (InterruptedException e) {
                showToast(this.getActivity(), this.getContext(), "Error (" + e.getMessage() + ")");
            }
        }

    }
}