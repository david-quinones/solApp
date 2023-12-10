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
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import estel.solapp.R;
import estel.solapp.common.CommController;
import estel.solapp.common.ValorsResposta;
import estel.solapp.models.Alumne;
import estel.solapp.models.Aula;
import estel.solapp.models.Empleat;
import estel.solapp.models.Persona;
import estel.solapp.models.User;

public class EnviarMissatges extends Fragment {

    EditText missatge;
    ListView llistaUsuaris, llistaDestinataris;
    ArrayList<Persona> usuaris = new ArrayList<>();
    ArrayList<Persona> destinataris = new ArrayList<>();
    ArrayAdapter<Persona> adaptadorUsuaris, adaptadorDestinataris;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_enviar_missatges, container, false);

        llistaDestinataris = view.findViewById(R.id.LVDestinataris);
        llistaUsuaris = view.findViewById(R.id.LVusuaris);
        missatge = view.findViewById(R.id.editTextMissatge);

        //Omplim la llista d'usuaris persones

        omplirUsuaris();

        //Afegir destinatari
        llistaUsuaris.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                destinataris.add(usuaris.get(i));
                usuaris.remove(i);
                mostrarDades(destinataris,usuaris);
            }
        });

        //Treure destinatari
        llistaDestinataris.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                usuaris.add(destinataris.get(i));
                destinataris.remove(i);
                mostrarDades(destinataris,usuaris);
            }
        });



        // Inflate the layout for this fragment
        return view;
    }


    /***************************************************************
     * Metode per mostrar les listview d'usuaris i destinataris
     * @param destinataris, usuaris
     ***************************************************************/


    public void mostrarDades(ArrayList<Persona> destinataris,ArrayList<Persona> usuaris) {

            adaptadorUsuaris=new ArrayAdapter<>(this.getContext(),android.R.layout.simple_list_item_1,usuaris);
            adaptadorDestinataris = new ArrayAdapter<>(this.getContext(),android.R.layout.simple_list_item_1,destinataris);
            llistaUsuaris.setAdapter(adaptadorUsuaris);
            llistaDestinataris.setAdapter(adaptadorDestinataris);

    }

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

                    Persona persona = (Persona) resposta.getData(i, Empleat.class);

                    usuaris.add(persona);
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
        Future<ValorsResposta> future2= executor.submit(() -> {
            return CommController.llistarAlumnes();
        });
        // Procesar resposta del servidor
        try {

            ValorsResposta resposta = future2.get();

            if (resposta == null) {

                showToast(this.getActivity(), this.getContext(), "Error de conexió amb el servidor. ");

            } else {
                for (int i = 1; i <= ((int) resposta.getData(0, int.class)); i++) {

                    Persona persona = (Persona) resposta.getData(i, Alumne.class);

                    usuaris.add(persona);
                }
            }
        } catch (Exception e) {

            showToast(this.getActivity(), this.getContext(), "Error (" + e.getMessage() + ")");
            Log.d("ERROR", "Error (" + e.getMessage() + ")");

        }
    }

}