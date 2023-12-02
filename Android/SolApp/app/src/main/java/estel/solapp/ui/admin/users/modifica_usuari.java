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
import estel.solapp.models.User;

/**
 * A simple {@link Fragment} subclass.
 *
 * create an instance of this fragment.
 */
public class modifica_usuari extends Fragment {


    private TextView nomUsuari,contrasenya, confirmaContrasenya;
    private Button modificar, confirmar;
    private CheckBox isactive, isAdmin, isTeacher;
    private TableLayout taulaUsuaris;
    private boolean alternar=true;
    private boolean set;
    private int color, id;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_modifica_usuari, container, false);

        //Asignació de tots els TextView
        nomUsuari = view.findViewById(R.id.editTextNomUsuari);
        contrasenya = view.findViewById(R.id.editTextcontrasenya);
        confirmaContrasenya = view.findViewById(R.id.editTextConfirmContrasenya);
        isAdmin = view.findViewById(R.id.cBisAdmin);
        isTeacher = view.findViewById(R.id.cBisProfessor);
        isactive = view.findViewById(R.id.cBisactive);
        taulaUsuaris = view.findViewById(R.id.taula_modifica_professor);
        taulaUsuaris.removeAllViews();

        llistarUsuaris();//Mostra la llista d'usuaris per escollir

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

                alertmodificarUsuari();

            }
        });

        // Inflador de layout pel fragment
        return view;
    }

    /*********************************************
     * Metode per enviar petició i mostrar resposta
     * Per llistar professors
     **********************************************/
    public void llistarUsuaris(){

        //Fem petició per agafar les dades de tots els usuaris
        // Creació d'unaltre fil.
        ExecutorService executor = Executors.newSingleThreadExecutor();
        // La petició es fa en unaltre fil
        Future<ValorsResposta> future = executor.submit(()->{return CommController.llistarUsuaris();});
        // Procesar resposta del servidor
        try {

            ValorsResposta resposta = future.get();

            if (resposta==null){

                showToast(this.getActivity(),this.getContext(), "Error de conexió amb el servidor. ");

            }else{

                taulaUsuaris.removeAllViews();
                //Capçelera de la taula
                //*********************

                TableRow capcelera = new TableRow(getContext());

                TextView nomHeader= new TextView(getContext()); nomHeader.setText("NOM USUARI");
                nomHeader.setGravity(Gravity.CENTER);
                nomHeader.setBackgroundResource(R.drawable.tablas_listas_heather);
                capcelera.addView(nomHeader);
                TextView isAdminHeather= new TextView(getContext()); isAdminHeather.setText("ADMINISTRADOR");
                isAdminHeather.setGravity(Gravity.CENTER);
                isAdminHeather.setBackgroundResource(R.drawable.tablas_listas_heather);
                capcelera.addView(isAdminHeather);
                TextView isTeacherHeader= new TextView(getContext()); isTeacherHeader.setText("PROFESSOR");
                isTeacherHeader.setGravity(Gravity.CENTER);
                isTeacherHeader.setBackgroundResource(R.drawable.tablas_listas_heather);
                capcelera.addView(isTeacherHeader);
                TextView isactiveHeader= new TextView(getContext()); isactiveHeader.setText("ACTIU");
                isactiveHeader.setGravity(Gravity.CENTER);
                isactiveHeader.setBackgroundResource(R.drawable.tablas_listas_heather);
                capcelera.addView(isactiveHeader);

                taulaUsuaris.addView(capcelera);

                //Creació de Taula amb llista d'empleats
                //**************************************

                for (int i=1;i<=((int)resposta.getData(0,int.class));i++){


                    //Condició per alternar colors a les files
                    if (alternar){ color=(R.drawable.tablas_listas_yellow);alternar=false; }
                    else {color=(R.drawable.tablas_listas_green);alternar=true;}

                    User usuari = (User) resposta.getData(i,User.class);

                    TableRow row = new TableRow(this.getContext());

                    TextView nom = new TextView(getContext()); nom.setText(usuari.getNomUsuari());
                    nom.setGravity(Gravity.CENTER);
                    nom.setBackgroundResource(color);
                    row.addView(nom);
                    CheckBox isAdmin = new CheckBox(getContext()); isAdmin.setChecked(usuari.isAdmin());
                    isAdmin.setGravity(Gravity.CENTER);
                    isAdmin.setBackgroundResource(color);
                    isAdmin.setEnabled(false);
                    row.addView(isAdmin);
                    CheckBox isTeacher = new CheckBox(getContext()); isTeacher.setChecked(usuari.isTeacher());
                    isTeacher.setGravity(Gravity.CENTER);
                    isTeacher.setBackgroundResource(color);
                    row.addView(isTeacher);
                    CheckBox isActive = new CheckBox(getContext()); isActive.setChecked(usuari.isActive());
                    isActive.setGravity(Gravity.CENTER);
                    isActive.setBackgroundResource(color);
                    row.addView(isActive);
                    //Fem clicable la fila de la taula i cridem al métode de mostrar les dades
                    //Pasant-li l'usuari de la fila
                    row.setClickable(true);
                    row.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {mostrarDades (usuari);}
                    });
                    taulaUsuaris.addView(row);

                }
            }

        } catch (Exception e) {

            showToast(this.getActivity(),this.getContext(), "Error ("+e.getMessage()+")");
            Log.d("ERROR" , "Error ("+e.getMessage()+")");

        }

    }

    /***************************************************************
     * Metode per mostrar les dades de l'empleat escollit a la taula
     * @param usuari
     **********************************************/
    public void mostrarDades (User usuari){

        nomUsuari.setText(usuari.getNomUsuari().toString());
        isAdmin.setChecked(usuari.isAdmin());
        contrasenya.setText(usuari.getPassword());
        confirmaContrasenya.setText(usuari.getPassword());
        isTeacher.setChecked(usuari.isTeacher());
        isactive.setChecked(usuari.isActive());
        id = usuari.getId();

    }

    /*********************************************
     * Metode per mostrar confirmació de modificar
     *********************************************/
    public void alertmodificarUsuari(){

        // Dialeg per preguntar a l'usuari si vol modificar l'usuari

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.getContext());
        alertDialog.setMessage("Estàs segur que vols modificar l'usuari?");
        alertDialog.setTitle("Atenció!");
        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Si", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                // Resposta si crida a modifcar professor

                modificarUsuari();

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
    public void modificarUsuari(){

        //Control de dades.

        String error = controlDades();

        if (!error.equals("")){

            Utility.showToast(this.getActivity(),this.getContext(),error);

        }else {//Dades correctes

            //Creació de Empleat i usuari per donar d'alta.
            User usuari = new User(id, nomUsuari.getText().toString(),contrasenya.getText().toString(),isAdmin.isChecked(),isTeacher.isChecked(),isactive.isChecked());


            // Creació d'unaltre fil.
            ExecutorService executor = Executors.newSingleThreadExecutor();
            // La petició es fa en unaltre fil
            Future<ValorsResposta> future = executor.submit(()->{return CommController.modificarUsuari(usuari);});
            // Procesar resposta del servidor
            try {

                ValorsResposta resposta = future.get();
                Gson gson= new Gson();
                Log.d("RESPOSTA MODIFICA USUARI", gson.toJson(resposta));
                if (resposta==null){

                    showToast(this.getActivity(),this.getContext(), "Error de conexió amb el servidor. ");

                }else{

                    if (resposta.getReturnCode()==CommController.OK_RETURN_CODE){//Codi correcte

                        showToast(this.getActivity(),this.getContext(), "Les dades s'han modificat");

                        set=false;
                        setFocusable(set);
                        llistarUsuaris();


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

        if (nomUsuari.getText().length()==0) {

            showToast(this.getActivity(), this.getContext(), "Seleccioni un professor de la llista. ");

        }else {
            // Fem tots els editText editables
            nomUsuari.setFocusableInTouchMode(set);
            contrasenya.setFocusableInTouchMode(set);
            confirmaContrasenya.setFocusableInTouchMode(set);
            isAdmin.setEnabled(set);
            isTeacher.setEnabled(set);
            isactive.setEnabled(set);
            confirmar.setEnabled(set);
        }

    }

    /***************************************************
     * Mètode per controlar camps buits i format de dades
     ****************************************************/
    public String controlDades(){

        String error = "";

        if (nomUsuari.getText().toString().isEmpty()){error = "La casella nom d'usuari és buida.\n"; }
        if (contrasenya.getText().toString().isEmpty()){error = error + "La casella contrasenya és buida.\n"; }
        if ((!contrasenya.getText().toString().equals(confirmaContrasenya.getText().toString()))){error = error + "Les contrasenyes no coincideixen."; }

        return (error);

    }
}