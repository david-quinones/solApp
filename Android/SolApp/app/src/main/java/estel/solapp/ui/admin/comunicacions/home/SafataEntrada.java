package estel.solapp.ui.admin.comunicacions.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;

import java.util.ArrayList;

import estel.solapp.R;
import estel.solapp.models.Persona;


public class SafataEntrada extends Fragment {

    EditText contingut;
    TableLayout taulamisatgeSeleccionat, taulaMissatges;
    Button enviarBtn;
    private boolean alternar = true;
    private boolean set;
    private int color;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_safata_entrada, container, false);

        contingut = view.findViewById(R.id.MultiLineContingut);
        taulaMissatges = view.findViewById(R.id.taula_Llista_Missatge);

        //omplirTaula();

        // Inflate the layout for this fragment
        return view;
    }




}