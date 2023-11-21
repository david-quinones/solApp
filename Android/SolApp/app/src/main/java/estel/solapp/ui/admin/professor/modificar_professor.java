package estel.solapp.ui.admin.professor;

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
import estel.solapp.common.Utility;
import estel.solapp.common.ValorsResposta;
import estel.solapp.models.Empleat;

/**
 * A simple {@link Fragment} subclass.
 *
 * create an instance of this fragment.
 */
public class modificar_professor extends Fragment {

    private TextView nom, cognom1, cognom2, nif, dataInici, dataFi, telefon, dataNaixement, email;
    private Button modificar, confirmar;
    private TableLayout taulaProfessors;
    private boolean alternar=true;

    private boolean set;
    private int color, idPersona, idEmpleat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_modificar_professor, container, false);

        //Asignació de tots els TextView
        nom = view.findViewById(R.id.editTextNom);
        cognom1 = view.findViewById(R.id.editTextCognom1);
        cognom2 = view.findViewById(R.id.editTextCognom22);
        nif = view.findViewById(R.id.editTextNif);
        dataInici = view.findViewById(R.id.editTextInici);
        dataFi = view.findViewById(R.id.editTextFi);
        telefon = view.findViewById(R.id.editTextTelefon);
        email = view.findViewById(R.id.editTextEmail);
        dataNaixement = view.findViewById(R.id.editTextDataNaixement);
        taulaProfessors = view.findViewById(R.id.taula_modifica_professor);
        taulaProfessors.removeAllViews();

        llistarProfessors();//Mostra la llista de professors per escollir

        modificar = view.findViewById(R.id.modificarBtn);
        modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                set = true;
                setFocusable(set);
            }
        });

        confirmar = view.findViewById(R.id.confirmarBtn);
        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertmodificarProfessor();

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


                    //Condició per alternar colors a les files
                    if (alternar){ color=(R.drawable.tablas_listas_yellow);alternar=false; }
                    else {color=(R.drawable.tablas_listas_green);alternar=true;}

                    Empleat empleat= (Empleat) resposta.getData(i,Empleat.class);

                    TableRow row = new TableRow(this.getContext());

                    TextView nom = new TextView(getContext()); nom.setText(empleat.getNom());
                    nom.setGravity(Gravity.CENTER);
                    nom.setBackgroundResource(color);
                    row.addView(nom);
                    TextView cognom1 = new TextView(getContext()); cognom1.setText(empleat.getCognom1());
                    cognom1.setGravity(Gravity.CENTER);
                    cognom1.setBackgroundResource(color);
                    row.addView(cognom1);
                    TextView cognom2 = new TextView(getContext()); cognom2.setText(empleat.getCognom2());
                    nom.setGravity(Gravity.CENTER);
                    cognom2.setBackgroundResource(color);
                    row.addView(cognom2);
                    //Fem clicable la fila de la taula i cridem al métode de mostrar les dades
                    //Pasant-li l'empleat de la fila
                    row.setClickable(true);
                    row.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mostrarDades (empleat);
                        }
                    });
                    taulaProfessors.addView(row);

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
        dataInici.setText(empleat.getIniciContracte());
        dataFi.setText(empleat.getFinalContracte());
        telefon.setText(empleat.getTelefon());
        email.setText(empleat.getMail().toString()); ;
        dataNaixement.setText(empleat.getData_naixement());
        idEmpleat= empleat.getIdEmpleat();
        idPersona = empleat.getIdPersona();

    }

    /*********************************************
     * Metode per mostrar confirmació d'eliminar
     *********************************************/
    public void alertmodificarProfessor(){

        // Dialeg per preguntar a l'usuari si vol modificar el professor

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.getContext());
        alertDialog.setMessage("Estàs segur que vols modificar el professor?");
        alertDialog.setTitle("Atenció!");
        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Si", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                // Resposta si crida a modifcar professor

                modificarProfessor();

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

    /************************************************
     * Metode per enviar petició de modificar professor
     ************************************************/
    public void modificarProfessor(){

        //Control de dades.

        String error = controlDades();

        if (!error.equals("")){

            Utility.showToast(this.getActivity(),this.getContext(),error);

        }else {//Dades correctes

            //Creació de Empleat i usuari per donar d'alta.
            Empleat empleat = new Empleat(idPersona,nom.getText().toString(),cognom1.getText().toString(),cognom2.getText().toString(),dataNaixement.getText().toString(),
                    nif.getText().toString(),telefon.getText().toString(),email.getText().toString(),idEmpleat,true,dataInici.getText().toString(),dataFi.getText().toString());


            // Creació d'unaltre fil.
            ExecutorService executor = Executors.newSingleThreadExecutor();
            // La petició es fa en unaltre fil
            Future<ValorsResposta> future = executor.submit(()->{return CommController.modificarEmpleat(empleat);});
            // Procesar resposta del servidor
            try {

                ValorsResposta resposta = future.get();
                Gson gson= new Gson();
                Log.d("RESPOSTA MODIFICA PROFE", gson.toJson(resposta));
                if (resposta==null){

                    showToast(this.getActivity(),this.getContext(), "Error de conexió amb el servidor. ");

                }else{

                    if (resposta.getReturnCode()==CommController.OK_RETURN_CODE){//Codi correcte

                        showToast(this.getActivity(),this.getContext(), "Les dades s'han modificat");

                        set=false;
                        setFocusable(set);
                        llistarProfessors();


                    }else {

                        showToast(this.getActivity(),this.getContext(), "No s'han pogut modificar les dades");

                    }
                }
            } catch (ExecutionException e) {
                showToast(this.getActivity(),this.getContext(), "Error ("+e.getMessage()+")");
            } catch (InterruptedException e) {
                showToast(this.getActivity(),this.getContext(), "Error ("+e.getMessage()+")");
            }

        }

    }

    /*********************************************
     * Mètode per fer editables tots els editText
     *********************************************/
    public void setFocusable(boolean set){

        if (nom.getText().length()==0) {

            showToast(this.getActivity(), this.getContext(), "Seleccioni un professor de la llista. ");

        }else {
            // Fem tots els editText editables
            nom.setFocusableInTouchMode(set);
            cognom1.setFocusableInTouchMode(set);
            cognom2.setFocusableInTouchMode(set);
            nif.setFocusableInTouchMode(set);
            dataInici.setFocusableInTouchMode(set);
            dataFi.setFocusableInTouchMode(set);
            telefon.setFocusableInTouchMode(set);
            email.setFocusableInTouchMode(set);
            dataNaixement.setFocusableInTouchMode(set);
            confirmar.setEnabled(set);
        }

    }

    /***************************************************
     * Mètode per controlar camps buits i format de dades
     ****************************************************/
    public String controlDades(){

        String error = "";

        if (nom.getText().toString().isEmpty()){error = "La casella nom és buida.\n"; }
        if (cognom1.getText().toString().isEmpty()){error = error + "La casella Primer cognom és buida.\n"; }
        if (cognom2.getText().toString().isEmpty()){error = error + "La casella Segón cognom és buida.\n"; }
        if (dataNaixement.getText().toString().isEmpty()){error = error + "La casella Data de naixement és buida.\n"; }
        if (nif.getText().toString().isEmpty()){error = error + "La casella NIF és buida.\n"; }
        //if (!Utility.vailidarNifNie(nif.getText().toString())){error=error + "El format del NIF no és vàlid.\n"; }
        if (!Utility.validarData(dataNaixement.getText().toString())) {error= error + "El format de la data ha de ser aaaa-mm-dd";}
        if (telefon.getText().toString().isEmpty()){error = error + "La casella Telèfon es buida.\n"; }
        if (email.getText().toString().isEmpty()){error = error + "La casella Email es buida.\n"; }
        if (!Utility.validarEmail(email.getText().toString())) {error = error + "El emailintroduït no és correcte";}

        return (error);

    }

}