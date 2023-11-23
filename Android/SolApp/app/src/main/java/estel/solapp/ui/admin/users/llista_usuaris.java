package estel.solapp.ui.admin.users;

import static estel.solapp.common.Utility.showToast;

import android.annotation.SuppressLint;
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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import estel.solapp.R;
import estel.solapp.common.CommController;
import estel.solapp.common.ValorsResposta;
import estel.solapp.models.User;

/********************************
 * Fragment de llistar usuaris
 ********************************/
public class llista_usuaris extends Fragment {

    private TableLayout taulaUsuaris;
    private boolean alternar=true;
    private int color;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_llista_usuaris, container, false);

        taulaUsuaris = view.findViewById(R.id.taula_Llista_Usuaris);
        taulaUsuaris.removeAllViews();
        LlistarUsuaris();

        //Inflar Layout del fragment
        return view;
    }

    public void LlistarUsuaris(){

        //Fem petició per agafar les dades de tots els professors
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

                //Capçelera de la taula
                //*********************

                TableRow capcelera = new TableRow(getContext());

                TextView nomHeader= new TextView(getContext()); nomHeader.setText("NOM");
                nomHeader.setGravity(Gravity.CENTER);
                capcelera.addView(nomHeader);
                nomHeader.setBackgroundResource(R.drawable.tablas_listas_heather);
                TextView isAdminHeader= new TextView(getContext()); isAdminHeader.setText("ES ADMINISTRADOR/A");
                isAdminHeader.setBackgroundResource(R.drawable.tablas_listas_heather);
                isAdminHeader.setGravity(Gravity.CENTER);
                capcelera.addView(isAdminHeader);
                TextView isTeacherHeader= new TextView(getContext()); isTeacherHeader.setText("ES PROFESSOR/A");
                isTeacherHeader.setBackgroundResource(R.drawable.tablas_listas_heather);
                isTeacherHeader.setGravity(Gravity.CENTER);
                capcelera.addView(isTeacherHeader);
                TextView isActiveHeader= new TextView(getContext()); isActiveHeader.setText("ES ACTIU");
                isActiveHeader.setBackgroundResource(R.drawable.tablas_listas_heather);
                isActiveHeader.setGravity(Gravity.CENTER);
                capcelera.addView(isActiveHeader);

                taulaUsuaris.addView(capcelera);

                //Creació de Taula amb llista d'usuaris
                //**************************************

                for (int i=1;i<=((int)resposta.getData(0,int.class));i++){

                    User user= (User) resposta.getData(i,User.class);

                    if (user.isActive()) {
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
                        nom.setText(user.getNomUsuari());
                        nom.setGravity(Gravity.CENTER);
                        nom.setBackgroundResource(color);
                        row.addView(nom);
                        TextView isAdmin = new TextView(getContext());
                        isAdmin.setText("No");
                        if (user.isAdmin()) {
                            isAdmin.setText("Si");
                        }
                        isAdmin.setGravity(Gravity.CENTER);
                        isAdmin.setBackgroundResource(color);
                        row.addView(isAdmin);
                        TextView isTeacher = new TextView(getContext());
                        isTeacher.setText("No");
                        if (user.isTeacher()) {
                            isTeacher.setText("Si");
                        }
                        isTeacher.setGravity(Gravity.CENTER);
                        isTeacher.setBackgroundResource(color);
                        row.addView(isTeacher);
                        TextView isActive = new TextView(getContext());
                        isActive.setText("No");
                        if (user.isActive()) {
                            isActive.setText("Si");
                        }
                        isActive.setGravity(Gravity.CENTER);
                        isActive.setBackgroundResource(color);
                        row.addView(isActive);


                        taulaUsuaris.addView(row);

                    }

                }
            }

        } catch (Exception e) {

            showToast(this.getActivity(),this.getContext(), "Error ("+e.getMessage()+")");
            Log.d("ERROR" , "Error ("+e.getMessage()+")");

        }

    }

}