package estel.solapp.ui.admin.comunicacions.home;

import static estel.solapp.common.Utility.showToast;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import estel.solapp.R;
import estel.solapp.common.CommController;
import estel.solapp.common.ValorsResposta;
import estel.solapp.models.Alumne;
import estel.solapp.models.Aula;
import estel.solapp.models.Empleat;
import estel.solapp.models.Missatge;
import estel.solapp.models.Persona;
import estel.solapp.models.User;

public class EnviarMissatges extends Fragment {

    EditText contingut;
    ListView llistaUsuaris, llistaDestinataris;
    ArrayList<Persona> usuaris = new ArrayList<>();
    ArrayList<Persona> destinataris = new ArrayList<>();
    ArrayAdapter<Persona> adaptadorUsuaris, adaptadorDestinataris;

    Button enviarBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_enviar_missatges, container, false);

        llistaDestinataris = view.findViewById(R.id.LVDestinataris);
        llistaUsuaris = view.findViewById(R.id.LVusuaris);
        contingut = view.findViewById(R.id.editTextMissatge);

        //Omplim la llista d'usuaris persones

        omplirUsuaris();

        //Mostrem els list views

        mostrarDades(destinataris, usuaris);

        //Afegir destinatari
        llistaUsuaris.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                destinataris.add(usuaris.get(i));
                usuaris.remove(i);
                mostrarDades(destinataris, usuaris);
            }
        });

        //Treure destinatari
        llistaDestinataris.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                usuaris.add(destinataris.get(i));
                destinataris.remove(i);
                mostrarDades(destinataris, usuaris);
            }
        });

        //Botó d'enviar crida al mètode d'enviar missatge

        enviarBtn = view.findViewById(R.id.enviarBtn);
        enviarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                enviarMissage();

            }
        });


        // Inflate the layout for this fragment
        return view;
    }


    /***************************************************************
     * Metode per mostrar les listview d'usuaris i destinataris
     * @param destinataris, usuaris
     ***************************************************************/
    public void mostrarDades(ArrayList<Persona> destinataris, ArrayList<Persona> usuaris) {

        adaptadorUsuaris = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1, usuaris);
        adaptadorDestinataris = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1, destinataris);
        llistaUsuaris.setAdapter(adaptadorUsuaris);
        llistaDestinataris.setAdapter(adaptadorDestinataris);

    }

    /***************************************************************
     * Metode per omplir l'arraylist d'usuaris
     *
     ***************************************************************/
    public void omplirUsuaris() {

        //Llista de persones

        //Fem petició per agafar les dades de tots els empleats
        // Creació d'unaltre fil.
        ExecutorService executor = Executors.newSingleThreadExecutor();
        // La petició es fa en unaltre fil
        Future<ValorsResposta> future = executor.submit(() -> {
            return CommController.llistarEmpleats();
        });
        // Procesar resposta del servidor
        try {

            ValorsResposta resposta = future.get();

            if (resposta == null) {

                showToast(this.getActivity(), this.getContext(), "Error de conexió amb el servidor. ");

            } else {
                for (int i = 1; i <= ((int) resposta.getData(0, int.class)); i++) {

                    Empleat empleat = (Empleat) resposta.getData(i, Empleat.class);

                    usuaris.add(empleat);
                }
            }
        } catch (Exception e) {

            showToast(this.getActivity(), this.getContext(), "Error (" + e.getMessage() + ")");
            Log.d("ERROR", "Error (" + e.getMessage() + ")");

        }

        //Fem petició per agafar les dades de tots els Alumnes
        // Creació d'unaltre fil.
        ExecutorService executor2 = Executors.newSingleThreadExecutor();
        // La petició es fa en unaltre fil
        Future<ValorsResposta> future2 = executor.submit(() -> {
            return CommController.llistarAlumnes();
        });
        // Procesar resposta del servidor
        try {

            ValorsResposta resposta = future2.get();

            if (resposta == null) {

                showToast(this.getActivity(), this.getContext(), "Error de conexió amb el servidor. ");

            } else {
                for (int i = 1; i <= ((int) resposta.getData(0, int.class)); i++) {

                    Alumne alumne = (Alumne) resposta.getData(i, Alumne.class);

                    usuaris.add(alumne);
                }
            }
        } catch (Exception e) {

            showToast(this.getActivity(), this.getContext(), "Error (" + e.getMessage() + ")");
            Log.d("ERROR", "Error (" + e.getMessage() + ")");

        }
    }

    /***************************************************************
     * Metode per enviar missatge
     *
     ***************************************************************/

    public void enviarMissage() {

        //Recuperem persona del usuari conectat per tenir el remitent

        //Fem petició per agafar les dades de l'usuari
        // Creació d'unaltre fil.
        ExecutorService executor = Executors.newSingleThreadExecutor();
        // La petició es fa en unaltre fil
        Future<ValorsResposta> future = executor.submit(() -> {
            return CommController.consultaPerfil();
        });
        // Procesar resposta del servidor
        try {
            ValorsResposta resposta = future.get();
            Gson gson = new Gson();
            Log.d("RESPOSTA CONSULTAR PERFIL", gson.toJson(resposta));
            Persona remitent = null;
            java.util.Date data = new Date();
            String dataEnviament= data.toString();
            if (resposta == null) {

                showToast(this.getActivity(), this.getContext(), "Error de conexió amb el servidor. ");

            } else {

                remitent = (Persona) resposta.getData(0, Persona.class);
            }

            //Instanciem el missatge que volem enviar

            Missatge missatge = new Missatge(remitent,destinataris,dataEnviament,contingut.getText().toString());

            //Fem petició per enviar missatge
            // Creació d'unaltre fil.
            ExecutorService executor2 = Executors.newSingleThreadExecutor();
            // La petició es fa en unaltre fil
            Future<ValorsResposta> future2 = executor2.submit(() -> {
                return CommController.enviarMissatge(missatge);
            });
            // Procesar resposta del servidor
            try {

                ValorsResposta resposta2 = future.get();

                if (resposta == null) {

                    showToast(this.getActivity(), this.getContext(), "Error de conexió amb el servidor. ");

                } else {

                    showToast(this.getActivity(), this.getContext(), "Missatge enviat");

                }
            } catch (Exception e) {

                showToast(this.getActivity(), this.getContext(), "Error (" + e.getMessage() + ")");
                Log.d("ERROR", "Error (" + e.getMessage() + ")");

            }

        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}