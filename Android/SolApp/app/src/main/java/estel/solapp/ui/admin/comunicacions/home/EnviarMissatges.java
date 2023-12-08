package estel.solapp.ui.admin.comunicacions.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import estel.solapp.R;
import estel.solapp.models.Persona;

public class EnviarMissatges extends Fragment {

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




        //Afegir destinatari
        llistaUsuaris.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                destinataris.add(usuaris.get(i));
                usuaris.remove(i);
                mostrarDades(aula);
            }
        });

        //Treure destinatari
        llistaDestinataris.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                usuaris.add(destinataris.get(i));
                destinataris.remove(i);
                mostrarDades(aula);
            }
        });



        // Inflate the layout for this fragment
        return view;
    }
}