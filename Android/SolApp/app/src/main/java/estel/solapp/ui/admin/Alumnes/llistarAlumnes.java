package estel.solapp.ui.admin.Alumnes;

import static estel.solapp.common.Utility.showToast;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import estel.solapp.R;
import estel.solapp.common.CommController;
import estel.solapp.common.ValorsResposta;
import estel.solapp.models.Alumne;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link llistarAlumnes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class llistarAlumnes extends Fragment {

    private TableLayout taulaAlumnes;
    private boolean alternar=true;
    private int color;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_llistar_alumnes, container, false);

        taulaAlumnes = view.findViewById(R.id.taula_Llista_Alumnes);
        taulaAlumnes.removeAllViews();
        LlistarAlumnes();

        // Inflar Layout del fragment
        return view;

    }
    /*********************************************
     * Metode per enviar petició i mostrar resposta
     **********************************************/
    public void LlistarAlumnes(){

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
                TextView telefonHeader= new TextView(getContext()); telefonHeader.setText("TELEFON");
                telefonHeader.setGravity(Gravity.CENTER);
                telefonHeader.setBackgroundResource(R.drawable.tablas_listas_heather);
                capcelera.addView(telefonHeader);
                TextView emailHeader= new TextView(getContext()); emailHeader.setText("EMAIL");
                emailHeader.setGravity(Gravity.CENTER);
                emailHeader.setBackgroundResource(R.drawable.tablas_listas_heather);
                capcelera.addView(emailHeader);
                TextView menjadorHeather= new TextView(getContext()); menjadorHeather.setText("MENJADOR");
                menjadorHeather.setGravity(Gravity.CENTER);
                menjadorHeather.setBackgroundResource(R.drawable.tablas_listas_heather);
                capcelera.addView(menjadorHeather);
                TextView acollidaHeader= new TextView(getContext()); acollidaHeader.setText("ACOLLIDA");
                acollidaHeader.setGravity(Gravity.CENTER);
                acollidaHeader.setBackgroundResource(R.drawable.tablas_listas_heather);
                capcelera.addView(acollidaHeader);

                taulaAlumnes.addView(capcelera);

                //Creació de Taula amb llista d'empleats
                //**************************************

                for (int i=1;i<=((int)resposta.getData(0,int.class));i++){

                    Alumne alumne = (Alumne) resposta.getData(i,Alumne.class);

                    if (alumne.isActiu()) {//Només llistem els actius

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
                        nom.setGravity(Gravity.CENTER);
                        cognom2.setBackgroundResource(color);
                        row.addView(cognom2);
                        TextView telefon = new TextView(getContext());
                        telefon.setText(alumne.getTelefon());
                        telefon.setGravity(Gravity.CENTER);
                        telefon.setBackgroundResource(color);
                        row.addView(telefon);
                        TextView email = new TextView(getContext());
                        email.setText(alumne.getMail());
                        email.setGravity(Gravity.CENTER);
                        email.setBackgroundResource(color);
                        row.addView(email);
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

                        taulaAlumnes.addView(row);

                    }

                }
            }

        } catch (Exception e) {

            showToast(this.getActivity(),this.getContext(), "Error ("+e.getMessage()+")");
            Log.d("ERROR" , "Error ("+e.getMessage()+")");

        }

    }
}