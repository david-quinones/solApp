package estel.solapp.ui.admin.professor;

import static estel.solapp.common.Utility.showToast;

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
import estel.solapp.models.Empleat;

/********************************
 * Fragment de llistar professors
 ********************************/
public class llistar_professor extends Fragment {
    private TableLayout taulaProfessors;
    private boolean alternar=true;
    private int color;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_llistar_professor, container, false);

        taulaProfessors = view.findViewById(R.id.taula_Llista_Professors);
        taulaProfessors.removeAllViews();
        LlistarProfessors();

        // Inflar Layout del fragment
        return view;

    }
    /*********************************************
    * Metode per enviar petició i mostrar resposta
    **********************************************/
    public void LlistarProfessors(){

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
                TextView dataIniciHeader= new TextView(getContext()); dataIniciHeader.setText("INICI DE CONTRACTE");
                dataIniciHeader.setGravity(Gravity.CENTER);
                dataIniciHeader.setBackgroundResource(R.drawable.tablas_listas_heather);
                capcelera.addView(dataIniciHeader);
                TextView dataFiHeader= new TextView(getContext()); dataFiHeader.setText("FI DE CONTRACTE");
                dataFiHeader.setGravity(Gravity.CENTER);
                dataFiHeader.setBackgroundResource(R.drawable.tablas_listas_heather);
                capcelera.addView(dataFiHeader);

                taulaProfessors.addView(capcelera);

                //Creació de Taula amb llista d'empleats
                //**************************************

                for (int i=1;i<=((int)resposta.getData(0,int.class));i++){

                    Empleat empleat= (Empleat) resposta.getData(i,Empleat.class);

                    if (empleat.isActiu()) {//Només llistem els actius

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
                        TextView telefon = new TextView(getContext());
                        telefon.setText(empleat.getTelefon());
                        telefon.setGravity(Gravity.CENTER);
                        telefon.setBackgroundResource(color);
                        row.addView(telefon);
                        TextView email = new TextView(getContext());
                        email.setText(empleat.getMail());
                        email.setGravity(Gravity.CENTER);
                        email.setBackgroundResource(color);
                        row.addView(email);
                        TextView dataInici = new TextView(getContext());
                        dataInici.setText(empleat.getIniciContracte());
                        dataInici.setGravity(Gravity.CENTER);
                        dataInici.setBackgroundResource(color);
                        row.addView(dataInici);
                        TextView dataFi = new TextView(getContext());
                        dataFi.setText(empleat.getFinalContracte());
                        dataFi.setGravity(Gravity.CENTER);
                        dataFi.setBackgroundResource(color);
                        row.addView(dataFi);

                        taulaProfessors.addView(row);

                    }

                }
            }

        } catch (Exception e) {

            showToast(this.getActivity(),this.getContext(), "Error ("+e.getMessage()+")");
            Log.d("ERROR" , "Error ("+e.getMessage()+")");

        }

    }

}