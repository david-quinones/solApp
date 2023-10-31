package estel.solapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import estel.solapp.R;
import estel.solapp.common.SingletonSessio;

public class PerfilActivity extends AppCompatActivity {

    private EditText nomUsuari, nom;
    private  Button modificarPerfilBtn, mofificarBtn;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        //Benvinguda amb el nom d'usuari logat
        TextView benvinguda;
        benvinguda =((TextView) findViewById(R.id.textViewPerfil));
        benvinguda.setText("Usuari: "+ SingletonSessio.getInstance().getUserConnectat().getNomUsuari());
        nomUsuari = (EditText) findViewById(R.id.editTextUserName);
        nom = (EditText) findViewById(R.id.editTextNom);

        modificarPerfilBtn= (Button)findViewById(R.id.modificarPerfilBtn);
        mofificarBtn = (Button)findViewById(R.id.modificarlBtn);

        modificarPerfilBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mofificarBtn.setClickable(true);
                nomUsuari.setFocusableInTouchMode(true);
                nom.setFocusableInTouchMode(true);
            }
        });

    }
}