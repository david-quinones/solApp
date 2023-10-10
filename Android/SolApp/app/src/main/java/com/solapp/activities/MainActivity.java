package com.solapp.activities;

import static com.solapp.common.Utility.showToast;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.solapp.R;
import com.solapp.common.CommController;
import com.solapp.common.Utility;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goHome(View view){
        String user, password;
        AppCompatActivity parent=this;
        Context context = getApplicationContext();
        boolean result;

        user = ((EditText) findViewById(R.id.editTextUser)).getText().toString().trim();
        password = ((EditText) findViewById(R.id.editTextPassword)).getText().toString().trim();

        // checks user entries

        ExecutorService executor = Executors.newSingleThreadExecutor();

        Button loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setEnabled(false);

        // request (must be done in an other thread)
        Future<Integer> future = executor.submit(()->{return CommController.doLogin(user,password);});

        // result processing
        try {
            if (future.get() == CommController.OK_RETURN_CODE) {
                Utility.gotoActivity(this, HomeActivityAdmin.class);
            } else {
                showToast(parent,context, "Usuari o contrasenya incorrectes");
            }
        } catch (ExecutionException | InterruptedException e) {
            showToast(parent,context, "Error ("+e.getMessage()+")");
        }
        finally{
            loginBtn.setEnabled(true);
        };

    }

    @Override
    public void onBackPressed() {
        if(!CommController.isLogged()){

            // creates a dialog to ask to the user

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setMessage("Estas segur que vols tancar l'aplicació?");
            alertDialog.setTitle("Sortir!");
            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("Si", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    // closes application

                    finish();
                    System.exit(0);
                }
            });
            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    // does nothing
                }
            });

            // opens dialog
            alertDialog.show();


        }else{  // warns to a logged user
            showToast(this,getApplicationContext(), "Error!");
        }
    }

}