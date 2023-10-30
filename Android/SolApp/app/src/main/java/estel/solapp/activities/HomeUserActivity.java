package estel.solapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import estel.solapp.R;
/**
 * Classe controlador de pantalla del menú principal pel rol usuari
 * @author Juan Antonio
 */
public class HomeUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);
    }
}