package estel.solapp.ui.admin.users;

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
import estel.solapp.models.Empleat;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EliminaUsuari#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EliminaUsuari extends Fragment {

    private TextView nom, cognom1, cognom2, nif, dataInici, dataFi;

    private String telefon, dataNaixement, email;
    private Button eliminar;
    private TableLayout taulaProfessors;
    private boolean alternar=true;
    private int color, idPersona, idEmpleat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_elimina_usuari, container, false);

        //Asignació de tots els TextView
        nom = view.findViewById(R.id.textViewNom);
        cognom1 = view.findViewById(R.id.textViewCognom1);
        cognom2 = view.findViewById(R.id.textViewCognom2);
        nif = view.findViewById(R.id.textViewNif);
        dataInici = view.findViewById(R.id.textViewInici);
        dataFi = view.findViewById(R.id.textViewFi);
        taulaProfessors = view.findViewById(R.id.taula_modifica_professor);
        taulaProfessors.removeAllViews();

        llistarProfessors();//Mostra la llista de professors per escollir
        eliminar = view.findViewById(R.id.eliminarBtn);
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertEliminarProfessor();

            }
        });

        // Inflador de layout pel fragment
        return view;
    }

    /*********************************************
     * Metode per enviar petició i mostrar resposta
     * Per llistar professors
     **********************************************/
    public void llistarProfessors(){

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

                taulaProfessors.removeAllViews();
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


                taulaProfessors.addView(capcelera);

                //Creació de Taula amb llista d'empleats
                //**************************************

                for (int i=1;i<=((int)resposta.getData(0,int.class));i++){

                    Empleat empleat= (Empleat) resposta.getData(i,Empleat.class);

                    if (empleat.isActiu()) {

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
                        nom.setText(empleat.getNom());
                        nom.setGravity(Gravity.CENTER);
                        nom.setBackgroundResource(color);
                        row.addView(nom);
                        TextView cognom1 = new TextView(getContext());
                        cognom1.setText(empleat.getCognom1());
                        cognom1.setGravity(Gravity.CENTER);
                        cognom1.setBackgroundResource(color);
                        row.addView(cognom1);
                        TextView cognom2 = new TextView(getContext());
                        cognom2.setText(empleat.getCognom2());
                        nom.setGravity(Gravity.CENTER);
                        cognom2.setBackgroundResource(color);
                        row.addView(cognom2);
                        //Fem clicable la fila de la taula i cridem al métode de mostrar les dades
                        //Pasant-li l'empleat de la fila
                        row.setClickable(true);
                        row.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mostrarDades(empleat);
                            }
                        });
                        taulaProfessors.addView(row);
                    }

                }
            }

        } catch (Exception e) {

            showToast(this.getActivity(),this.getContext(), "Error ("+e.getMessage()+")");
            Log.d("ERROR" , "Error ("+e.getMessage()+")");

        }

    }

    /***************************************************************
     * Metode per mostrar les dades de l'empleat escollit a la taula
     * @param empleat
     **********************************************/
    public void mostrarDades (Empleat empleat){

        nom.setText(empleat.getNom().toString());
        cognom1.setText(empleat.getCognom1().toString());
        cognom2.setText(empleat.getCognom2().toString());
        nif.setText(empleat.getDni().toString());
        dataInici.setText(empleat.getIniciContracte().toString());
        dataFi.setText(empleat.getFinalContracte().toString());
        telefon = empleat.getTelefon();
        email = empleat.getMail();
        dataNaixement = empleat.getData_naixement();
        idEmpleat= empleat.getIdEmpleat();
        idPersona = empleat.getIdPersona();

    }

    /*********************************************
     * Metode per mostrar confirmació d'eliminar
     *********************************************/
    public void alertEliminarProfessor(){

        if (nom.getText().length()==0) {

            showToast(this.getActivity(), this.getContext(), "Seleccioni un professor de la llista. ");

        }else {

            // Dialeg per preguntar a l'usuari si vol eliminar el professor

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.getContext());
            alertDialog.setMessage("Estàs segur que vols eliminar el professor?");
            alertDialog.setTitle("Atenció!");
            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("Si", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    // Resposta si crida a eliminar professor
                    eliminarProfessor();

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
     * Metode per enviar petició d'eliminar professor
     ************************************************/
    public void eliminarProfessor(){

        //Creació de Empleat i usuari per donar d'alta.
        Empleat empleat = new Empleat(idPersona,nom.getText().toString(),cognom1.getText().toString(),cognom2.getText().toString(),dataNaixement,
                nif.getText().toString(),telefon,email,idEmpleat,false,dataInici.getText().toString(),dataFi.getText().toString());


        // Creació d'unaltre fil.
        ExecutorService executor = Executors.newSingleThreadExecutor();
        // La petició es fa en unaltre fil
        Future<ValorsResposta> future = executor.submit(()->{return CommController.eliminarEmpleat(empleat);});
        // Procesar resposta del servidor
        try {

            ValorsResposta resposta = future.get();
            Gson gson= new Gson();
            Log.d("RESPOSTA ELIMINA PROFE", gson.toJson(resposta));
            if (resposta==null){

                showToast(this.getActivity(),this.getContext(), "Error de conexió amb el servidor. ");

            }else{

                if (resposta.getReturnCode()==CommController.OK_RETURN_CODE){//Codi correcte

                    showToast(this.getActivity(),this.getContext(), "Empleat eliminat");
                    llistarProfessors();


                }else {

                    showToast(this.getActivity(),this.getContext(), "No s'ha pogut eliminar l'empleat");

                }
            }
        } catch (ExecutionException e) {
            showToast(this.getActivity(),this.getContext(), "Error ("+e.getMessage()+")");
        } catch (InterruptedException e) {
            showToast(this.getActivity(),this.getContext(), "Error ("+e.getMessage()+")");
        }

    }
}