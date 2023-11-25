package estel.solapp.ui.admin.Alumnes;

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
import android.widget.CheckBox;
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
import estel.solapp.models.Alumne;
import estel.solapp.models.Empleat;

/************************************
 * Metode que llista els alumnes
 * I elimina el escollit de la llista
 *************************************/
public class eliminarAlumne extends Fragment {
    private TextView nom, cognom1, cognom2, nif;
    private CheckBox menjador, acollida;
    private String telefon, dataNaixement, email;
    private Button eliminar;
    private TableLayout taulaAlumnes;
    private boolean alternar=true;
    private int color, idPersona, idAlumne;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_eliminar_alumne, container, false);

        //Asignació de tots els TextView
        nom = view.findViewById(R.id.textViewNom);
        cognom1 = view.findViewById(R.id.textViewCognom1);
        cognom2 = view.findViewById(R.id.textViewCognom2);
        nif = view.findViewById(R.id.textViewNif);
        menjador = view.findViewById(R.id.cBmenjadorEliminar);
        acollida = view.findViewById(R.id.cBacollidaEliminar);
        taulaAlumnes = view.findViewById(R.id.taula_eliminar_alumne);
        taulaAlumnes.removeAllViews();

        llistarAlumnes();//Mostra la llista d'alumnes per escollir

        eliminar = view.findViewById(R.id.eliminarBtn);
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertEliminarAlumne();

            }
        });

        // Inflador de layout pel fragment
        return view;
    }

    /*********************************************
     * Metode per enviar petició i mostrar resposta
     * Per llistar alumnes
     **********************************************/
    public void llistarAlumnes(){

        //Fem petició per agafar les dades de tots els alumnes
        // Creació d'unaltre fil.
        ExecutorService executor = Executors.newSingleThreadExecutor();
        // La petició es fa en unaltre fil
        Future<ValorsResposta> future = executor.submit(()->{return CommController.llistarAlumnes();});
        // Procesar resposta del servidor
        try {

            ValorsResposta resposta = future.get();

            if (resposta==null){

                showToast(this.getActivity(),this.getContext(), "Error de conexió amb el servidor. ");

            }else{

                taulaAlumnes.removeAllViews();
                //Capçelera de la taula
                //*********************

                TableRow capcelera = new TableRow(getContext());

                TextView nomHeader= new TextView(getContext()); nomHeader.setText("NOM");
                nomHeader.setGravity(Gravity.CENTER);
                nomHeader.setBackgroundResource(R.drawable.tablas_listas_heather);
                capcelera.addView(nomHeader);
                TextView cognom1Header= new TextView(getContext()); cognom1Header.setText("PRIMER COGNOM");
                cognom1Header.setGravity(Gravity.CENTER);
                cognom1Header.setBackgroundResource(R.drawable.tablas_listas_heather);
                capcelera.addView(cognom1Header);
                TextView cognom2Header= new TextView(getContext()); cognom2Header.setText("SEGON COGNOM");
                cognom2Header.setGravity(Gravity.CENTER);
                cognom2Header.setBackgroundResource(R.drawable.tablas_listas_heather);
                capcelera.addView(cognom2Header);
                TextView menjadorHeather = new TextView(getContext()); menjadorHeather.setText("MENJADOR");
                menjadorHeather.setGravity(Gravity.CENTER);
                menjadorHeather.setBackgroundResource(R.drawable.tablas_listas_heather);
                capcelera.addView(menjadorHeather);
                TextView acollidaHeather = new TextView(getContext()); acollidaHeather.setText("ACOLLIDA");
                acollidaHeather.setGravity(Gravity.CENTER);
                acollidaHeather.setBackgroundResource(R.drawable.tablas_listas_heather);
                capcelera.addView(acollidaHeather);

                taulaAlumnes.addView(capcelera);

                //Creació de Taula amb llista d'alumnes
                //**************************************

                for (int i=1;i<=((int)resposta.getData(0,int.class));i++){

                    Alumne alumne = (Alumne) resposta.getData(i,Alumne.class);

                    if (alumne.isActiu()) {

                        //Condició per alternar colors a les files
                        if (alternar) {
                            color = (R.drawable.tablas_listas_yellow);
                            alternar = false;
                        } else {
                            color = (R.drawable.tablas_listas_green);
                            alternar = true;
                        }

                        TableRow row = new TableRow(this.getContext());

                        TextView nom = new TextView(getContext());
                        nom.setText(alumne.getNom());
                        nom.setGravity(Gravity.CENTER);
                        nom.setBackgroundResource(color);
                        row.addView(nom);
                        TextView cognom1 = new TextView(getContext());
                        cognom1.setText(alumne.getCognom1());
                        cognom1.setGravity(Gravity.CENTER);
                        cognom1.setBackgroundResource(color);
                        row.addView(cognom1);
                        TextView cognom2 = new TextView(getContext());
                        cognom2.setText(alumne.getCognom2());
                        cognom2.setGravity(Gravity.CENTER);
                        cognom2.setBackgroundResource(color);
                        row.addView(cognom2);
                        CheckBox menjador = new CheckBox(getContext());
                        menjador.setChecked(alumne.isMenjador());
                        menjador.setGravity(Gravity.CENTER);
                        menjador.setBackgroundResource(color);
                        row.addView(menjador);
                        CheckBox acollida = new CheckBox(getContext());
                        acollida.setChecked(alumne.isAcollida());
                        acollida.setGravity(Gravity.CENTER);
                        acollida.setBackgroundResource(color);
                        row.addView(acollida);
                        //Fem clicable la fila de la taula i cridem al métode de mostrar les dades
                        //Pasant-li l'empleat de la fila
                        row.setClickable(true);
                        row.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {mostrarDades(alumne);}
                        });
                        taulaAlumnes.addView(row);
                    }

                }
            }

        } catch (Exception e) {

            showToast(this.getActivity(),this.getContext(), "Error ("+e.getMessage()+")");
            Log.d("ERROR" , "Error ("+e.getMessage()+")");

        }

    }

    /***************************************************************
     * Metode per mostrar les dades de l'alumne escollit a la taula
     * @param alumne
     **********************************************/
    public void mostrarDades (Alumne alumne){

        nom.setText(alumne.getNom().toString());
        cognom1.setText(alumne.getCognom1().toString());
        cognom2.setText(alumne.getCognom2().toString());
        nif.setText(alumne.getDni().toString());
        menjador.setChecked(alumne.isMenjador());
        acollida.setChecked(alumne.isAcollida());
        telefon = alumne.getTelefon();
        email = alumne.getMail();
        dataNaixement = alumne.getData_naixement();
        idAlumne = alumne.getIdAlumne();
        idPersona = alumne.getIdPersona();

    }

    /*********************************************
     * Metode per mostrar confirmació d'eliminar
     *********************************************/
    public void alertEliminarAlumne(){

        if (nom.getText().length()==0) {

            showToast(this.getActivity(), this.getContext(), "Seleccioni un professor de la llista. ");

        }else {

            // Dialeg per preguntar a l'usuari si vol eliminar el professor

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.getContext());
            alertDialog.setMessage("Estàs segur que vols eliminar l'alumne?");
            alertDialog.setTitle("Atenció!");
            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("Si", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    // Resposta si crida a eliminar alumne
                    eliminarAlumne();

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

    /************************************************
     * Metode per enviar petició d'eliminar alumne
     ************************************************/
    public void eliminarAlumne(){

        //Creació d'alumne i persona per eliminar.
        Alumne alumne = new Alumne(idPersona,nom.getText().toString(),cognom1.getText().toString(),cognom2.getText().toString(),dataNaixement,
                nif.getText().toString(),telefon,email,idAlumne,false,menjador.isChecked(),acollida.isChecked());


        // Creació d'unaltre fil.
        ExecutorService executor = Executors.newSingleThreadExecutor();
        // La petició es fa en unaltre fil
        Future<ValorsResposta> future = executor.submit(()->{return CommController.eliminarAlumne(alumne);});
        // Procesar resposta del servidor
        try {

            ValorsResposta resposta = future.get();
            Gson gson= new Gson();
            Log.d("RESPOSTA ELIMINA ALUMNE", gson.toJson(resposta));
            if (resposta==null){

                showToast(this.getActivity(),this.getContext(), "Error de conexió amb el servidor. ");

            }else{

                if (resposta.getReturnCode()==CommController.OK_RETURN_CODE){//Codi correcte

                    showToast(this.getActivity(),this.getContext(), "Alumne eliminat");
                    llistarAlumnes();


                }else {

                    showToast(this.getActivity(),this.getContext(), "No s'ha pogut eliminar l'alumne");

                }
            }
        } catch (ExecutionException e) {
            showToast(this.getActivity(),this.getContext(), "Error ("+e.getMessage()+")");
        } catch (InterruptedException e) {
            showToast(this.getActivity(),this.getContext(), "Error ("+e.getMessage()+")");
        }

    }
}