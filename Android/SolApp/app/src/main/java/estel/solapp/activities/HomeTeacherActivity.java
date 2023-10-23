package estel.solapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import estel.solapp.R;
import estel.solapp.common.SingletonSessio;

public class HomeTeacherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_teacher);

        TextView benvinguda;
        benvinguda =((TextView) findViewById(R.id.textBenvinguda));
        benvinguda.setText("Benvigut "+ SingletonSessio.getInstance().getUserConnectat().getNomUsuari());
    }
}