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
import estel.solapp.common.Utility;
import estel.solapp.common.ValorsResposta;
import estel.solapp.models.Alumne;

/**
 * A simple {@link Fragment} subclass.
 * Use
 * create an instance of this fragment.
 */
public class modificarAlumne extends Fragment {

    private TextView nom, cognom1, cognom2, nif, telefon, dataNaixement, email;
    private Button modificar, confirmar;
    private CheckBox isactive, menjador, acollida;
    private TableLayout taulaAlumnes;
    private boolean alternar=true;
    private boolean set;
    private int color, idPersona, idAlumne;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_modificar_alumne, container, false);

        //Asignació de tots els TextView
        nom = view.findViewById(R.id.editTextNom);
        cognom1 = view.findViewById(R.id.editTextCognom1);
        cognom2 = view.findViewById(R.id.editTextCognom22);
        nif = view.findViewById(R.id.editTextDNI);
        menjador = view.findViewById(R.id.cBMenjador);
        acollida = view.findViewById(R.id.cBAcollida);
        telefon = view.findViewById(R.id.editTextTelefon);
        email = view.findViewById(R.id.editTextEmail);
        dataNaixement = view.findViewById(R.id.editTextDataNaixement);
        isactive = view.findViewById(R.id.cBisactive);
        taulaAlumnes = view.findViewById(R.id.taula_modifica_alumne);
        taulaAlumnes.removeAllViews();

        llistarAlumnes();//Mostra la llista d'alumnes per escollir

        modificar = view.findViewById(R.id.altaBtn);
        modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                set = true;
                setFocusable(set);
            }
        });

        confirmar = view.findViewById(R.id.esborrarBtn);
        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertmodificarAlumne();

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
                TextView actiuHeather = new TextView(getContext()); actiuHeather.setText("ACTIU");
                actiuHeather.setGravity(Gravity.CENTER);
                actiuHeather.setBackgroundResource(R.drawable.tablas_listas_heather);
                capcelera.addView(actiuHeather);

                taulaAlumnes.addView(capcelera);

                //Creació de Taula amb llista d'empleats
                //**************************************

                for (int i=1;i<=((int)resposta.getData(0,int.class));i++){


                    //Condició per alternar colors a les files
                    if (alternar){ color=(R.drawable.tablas_listas_yellow);alternar=false; }
                    else {color=(R.drawable.tablas_listas_green);alternar=true;}

                    Alumne alumne = (Alumne) resposta.getData(i,Alumne.class);

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
                    CheckBox actiu = new CheckBox(getContext());
                    actiu.setChecked(alumne.isActiu());
                    actiu.setGravity(Gravity.CENTER);
                    actiu.setBackgroundResource(color);
                    row.addView(actiu);

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

        } catch (Exception e) {

            showToast(this.getActivity(),this.getContext(), "Error ("+e.getMessage()+")");
            Log.d("ERROR" , "Error ("+e.getMessage()+")");

        }

    }

    /***************************************************************
     * Metode per mostrar les dades de l'alumne escollit a la taula
     * @param alumne
     ***************************************************************/
    public void mostrarDades (Alumne alumne){

        nom.setText(alumne.getNom().toString());
        cognom1.setText(alumne.getCognom1().toString());
        cognom2.setText(alumne.getCognom2().toString());
        nif.setText(alumne.getDni().toString());
        menjador.setChecked(alumne.isMenjador());
        acollida.setChecked(alumne.isAcollida());
        telefon.setText(alumne.getTelefon());
        email.setText(alumne.getMail());
        dataNaixement.setText(alumne.getData_naixement());
        idAlumne = alumne.getIdAlumne();
        idPersona = alumne.getIdPersona();
        isactive.setChecked(alumne.isActiu());

    }

    /*********************************************
     * Metode per mostrar confirmació d'eliminar
     *********************************************/
    public void alertmodificarAlumne(){

        // Dialeg per preguntar a l'usuari si vol modificar l'alumne

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.getContext());
        alertDialog.setMessage("Estàs segur que vols modificar l'alumne?");
        alertDialog.setTitle("Atenció!");
        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Si", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                // Resposta si crida a modifcar alumne

                modificarAlumne();

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
     * Metode per enviar petició de modificar alumne
     ************************************************/
    public void modificarAlumne(){

        //Control de dades.

        String error = controlDades();

        if (!error.equals("")){

            Utility.showToast(this.getActivity(),this.getContext(),error);

        }else {//Dades correctes

            //Creació d'alumne i usuari per donar d'alta.
            Alumne alumne = new Alumne(idPersona,nom.getText().toString(),cognom1.getText().toString(),cognom2.getText().toString(),dataNaixement.getText().toString(),
                    nif.getText().toString(),telefon.getText().toString(),email.getText().toString(),idAlumne,isactive.isChecked(),menjador.isChecked(),acollida.isChecked());


            // Creació d'unaltre fil.
            ExecutorService executor = Executors.newSingleThreadExecutor();
            // La petició es fa en unaltre fil
            Future<ValorsResposta> future = executor.submit(()->{return CommController.modificarAlumne(alumne);});
            // Procesar resposta del servidor
            try {

                ValorsResposta resposta = future.get();
                Gson gson= new Gson();
                Log.d("RESPOSTA MODIFICA ALUMNE", gson.toJson(resposta));
                if (resposta==null){

                    showToast(this.getActivity(),this.getContext(), "Error de conexió amb el servidor. ");

                }else{

                    if (resposta.getReturnCode()==CommController.OK_RETURN_CODE){//Codi correcte

                        showToast(this.getActivity(),this.getContext(), "Les dades s'han modificat");

                        set=false;
                        setFocusable(set);
                        llistarAlumnes();


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

            showToast(this.getActivity(), this.getContext(), "Seleccioni un alumne de la llista. ");

        }else {
            // Fem tots els editText editables
            nom.setFocusableInTouchMode(set);
            cognom1.setFocusableInTouchMode(set);
            cognom2.setFocusableInTouchMode(set);
            nif.setFocusableInTouchMode(set);
            acollida.setEnabled(set);
            menjador.setEnabled(set);
            telefon.setFocusableInTouchMode(set);
            email.setFocusableInTouchMode(set);
            dataNaixement.setFocusableInTouchMode(set);
            confirmar.setEnabled(set);
            isactive.setEnabled(set);
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
        if (!Utility.validarEmail(email.getText().toString())) {error = error + "El email introduït no és correcte";}

        return (error);

    }
}