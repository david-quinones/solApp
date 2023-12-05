package estel.solapp.ui.admin.aules;

import static estel.solapp.common.Utility.showToast;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
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
import estel.solapp.common.Utility;
import estel.solapp.common.ValorsResposta;
import estel.solapp.models.Alumne;
import estel.solapp.models.Aula;
import estel.solapp.models.Empleat;
import estel.solapp.models.User;


public class ModificarAula extends Fragment {

    private TextView nomAula;
    private Button modificar, confirmar;
    private TableLayout taulaAules;
    private Spinner spinnerProfessors;
    private ArrayList<Empleat> professors = new ArrayList<Empleat>();
    private ArrayAdapter<Empleat> adaptadorProfessors;
    private ArrayList<Alumne> alumnes = new ArrayList<Alumne>();
    private ArrayList<Alumne>alumnesClase = new ArrayList<Alumne>();
    private ArrayAdapter<Alumne> adaptadorAlumnes, adaptadorAlumnesClasse;
    private ListView llistaAlumnes, llistaAlumnesClase;
    private boolean alternar = true;
    private boolean set;
    private int color;

    private Aula aula;

    private Empleat empleat;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_modificar_aula, container, false);

        //Asignació de tots els TextView
        nomAula = view.findViewById(R.id.editTextNomAula);

        //Taula per mostrar aules a escollir
        taulaAules = view.findViewById(R.id.taula_modifica_aula);
        taulaAules.removeAllViews();
        llistarAules();//Mostra la llista d'aules per escollir

        //Spinner de professors
        spinnerProfessors= view.findViewById(R.id.spinnerProfessors);
        omplirProfessors();//Omplim l'aaraylist de professors per escollir un
        adaptadorProfessors = new ArrayAdapter(this.getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,professors);
        spinnerProfessors.setAdapter(adaptadorProfessors);
        professors.add(0,new Empleat(" ","","","","","",""));
        spinnerProfessors.setSelection(0);

        omplirAlumnes();//Omplim l'arraylist d'alumnes del centre sense classe

        //ListViews
        //ListView llista d'alumnes a classe
        llistaAlumnesClase = view.findViewById(R.id.lvllistaAlumnesClasse);

        //ListView Llista d'alumnes total
        llistaAlumnes = view.findViewById(R.id.lVllistaAlumnes);

        //Afegir alumne a l'aula
        llistaAlumnes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                alumnesClase.add(alumnes.get(i));
                alumnes.remove(i);
                mostrarDades(aula);
            }
        });

        //Treure alumne de l'aula
        llistaAlumnesClase.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                alumnes.add(alumnesClase.get(i));
                alumnesClase.remove(i);
                mostrarDades(aula);
            }
        });

        //Botó de modificar
        modificar = view.findViewById(R.id.modificaBtn);
        modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                set = true;
                setFocusable(set);//activem click als camps per poder modificar-los
            }
        });

        //Botó de desar
        confirmar = view.findViewById(R.id.desaBtn);
        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertmodificarAula();

            }
        });

        // Inflador de layout pel fragment
        return view;
    }

    /*********************************************
     * Metode per enviar petició i mostrar resposta
     * Per llistar aules
     **********************************************/
    public void llistarAules() {

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

                taulaAules.removeAllViews();
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

                    Aula aulaTaula = (Aula) resposta.getData(i,Aula.class);
                    Gson gson1= new Gson();
                    Log.d("AULA "+i, gson1.toJson(aulaTaula));
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
                    TextView nomAula = new TextView(getContext());
                    nomAula.setText(aulaTaula.getNomAula().toString());
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
                    //Fem clicable la fila de la taula i cridem al métode de mostrar les dades
                    //Pasant-li l'aula de la fila
                    row.setClickable(true);
                    row.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            aula = (aulaTaula);
                            mostrarDades (aulaTaula);

                        }
                    });

                    row.addView(nAlumnes);
                    taulaAules.addView(row);

                }

            }

        } catch (Exception e) {

            showToast(this.getActivity(),this.getContext(), "Error ("+e.getMessage()+")");
            Log.d("ERROR" , "Error ("+e.getMessage()+")");

        }

    }

    /***************************************************************
     * Metode per mostrar les dades de l'aula escollida a la taula
     * @param aulaTaula
     ***************************************************************/
    public void mostrarDades(Aula aulaTaula) {

        aula = aulaTaula;
        nomAula.setText(aulaTaula.getNomAula());
        alumnesClase= aulaTaula.getAlumnes();//Omplim l'arraylist d'alumnes de la clase
        adaptadorAlumnesClasse = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1,alumnesClase);
        llistaAlumnesClase.setAdapter(adaptadorAlumnesClasse);


        adaptadorAlumnes = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1,alumnes);
        llistaAlumnes.setAdapter(adaptadorAlumnes);

        int posicio = trobarProfessor(aulaTaula);//Metode per trobar el professor que te asignat l'aula

        spinnerProfessors.setSelection(posicio);

    }

    /*********************************************
     * Metode per mostrar confirmació de modificar
     *********************************************/
    public void alertmodificarAula() {

        // Dialeg per preguntar a l'usuari si vol modificar l'usuari

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.getContext());
        alertDialog.setMessage("Estàs segur que vols modificar l'aula?");
        alertDialog.setTitle("Atenció!");
        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Resposta si crida a modifcar professor

                modificarAula();

            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Resposta "no" no fa res
            }
        });

        // Obrir dialeg
        alertDialog.show();

    }

    /************************************************
     * Metode per enviar petició de modificar aula
     ************************************************/
    public void modificarAula() {

        //Control de dades.

        String error = controlDades();

        if (!error.equals("")) {

            Utility.showToast(this.getActivity(), this.getContext(), error);

        } else {//Dades correctes

            //Creació d'aula modificada.
            Empleat empleatEscollit = (Empleat) spinnerProfessors.getSelectedItem();
            Aula novaAula = new Aula(aula.getId(),nomAula.getText().toString(),empleatEscollit,alumnesClase);

            // Creació d'unaltre fil.
            ExecutorService executor = Executors.newSingleThreadExecutor();
            // La petició es fa en unaltre fil
            Future<ValorsResposta> future = executor.submit(() -> {return CommController.modificarAula(novaAula);
            });
            // Procesar resposta del servidor
            try {

                ValorsResposta resposta = future.get();
                Gson gson = new Gson();
                Log.d("RESPOSTA MODIFICA AULA", gson.toJson(resposta));
                if (resposta == null) {

                    showToast(this.getActivity(), this.getContext(), "Error de conexió amb el servidor. ");

                } else {

                    if (resposta.getReturnCode() == CommController.OK_RETURN_CODE) {//Codi correcte

                        showToast(this.getActivity(), this.getContext(), "Les dades s'han modificat");

                        set = false;
                        setFocusable(set);
                        llistarAules();

                    } else {

                        showToast(this.getActivity(), this.getContext(), "No s'han pogut modificar les dades");

                    }
                }
            } catch (ExecutionException e) {
                showToast(this.getActivity(), this.getContext(), "Error (" + e.getMessage() + ")");
            } catch (InterruptedException e) {
                showToast(this.getActivity(), this.getContext(), "Error (" + e.getMessage() + ")");
            }

        }

    }

    /*************************************************
     * Mètode per fer omplir l'arrayList de professors
     *************************************************/
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
    /***************************************************************
     * Mètode per trobar la posicio a l'array del professor assignat
     ***************************************************************/
    public int trobarProfessor (Aula aula){
        int posicio=-1;
        for (int i=1;i<professors.size();i++){

            if (professors.get(i).getIdEmpleat()==aula.getEmpleat().getIdEmpleat()){posicio=i;}

        }

        return posicio;
    }

    /*********************************************************
     * Mètode per fer omplir l'arrayList d'alumnes sense clase
     *********************************************************/

    public void omplirAlumnes(){

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
                for (int i=1;i<=((int)resposta.getData(0,int.class));i++) {

                    Alumne alumne = (Alumne) resposta.getData(i, Alumne.class);

                    if (alumne.getIdAula()==0) {//Només afegim a l'array els que no tenen classe

                        alumnes.add(alumne);
                    }
                }
            }
        }catch (Exception e) {

            showToast(this.getActivity(),this.getContext(), "Error ("+e.getMessage()+")");
            Log.d("ERROR" , "Error ("+e.getMessage()+")");

        }

    }

    /*********************************************
     * Mètode per fer editables tots els editText
     *********************************************/
    public void setFocusable(boolean set) {

        if (nomAula.getText().length() == 0) {

            showToast(this.getActivity(), this.getContext(), "Seleccioni una aula de la llista. ");

        } else {
            // Fem tots els editText editables
            nomAula.setFocusableInTouchMode(set);
            spinnerProfessors.setClickable(set);
            confirmar.setEnabled(set);
        }

    }

    /***************************************************
     * Mètode per controlar camps buits i format de dades
     ****************************************************/
    public String controlDades() {

        String error = "";

        if (nomAula.getText().toString().isEmpty()) {
            error = "La casella nom d'aula és buida.\n";
        }

        return (error);

    }
}