package estel.solapp.ui.admin;

import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.TableLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import estel.solapp.R;
import estel.solapp.databinding.ActivityNavGestioProfessorsBinding;
import estel.solapp.databinding.ActivityNavGestioUsuarisBinding;

public class NavGestioProfessorsActivity extends AppCompatActivity {

    Button retornBtn;

    TableLayout tl;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityNavGestioProfessorsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNavGestioProfessorsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        setSupportActionBar(binding.appBarNavGestioProfessors.toolbar);
        binding.appBarNavGestioProfessors.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navViewProfessors;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.home_gestio_professors,R.id.afegir_professor,R.id.eliminar_professor,R.id.modificar_professor,R.id.busca_professor,R.id.llistar_professor)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_nav_gestio_professors);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_nav_gestio_professors);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}