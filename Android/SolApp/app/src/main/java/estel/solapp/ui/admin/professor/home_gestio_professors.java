package estel.solapp.ui.admin.professor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import estel.solapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link home_gestio_professors} factory method to
 * create an instance of this fragment.
 */
public class home_gestio_professors extends Fragment {

    View view;


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home_gestio_professors, container, false);

        return  view;
    }
}