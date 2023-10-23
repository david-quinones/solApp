package estel.solapp.activities;

import static estel.solapp.common.Utility.showToast;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import estel.solapp.R;
import estel.solapp.common.CommController;
import estel.solapp.common.SingletonSessio;
import estel.solapp.common.Utility;
import estel.solapp.common.ValorsResposta;
import estel.solapp.models.User;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    public void goHome(View view){
        String username, password, sessionCode;
        AppCompatActivity parent=this;
        Context context = getApplicationContext();
        boolean result;

        username = ((EditText) findViewById(R.id.editTextUser)).getText().toString().trim();
        password = ((EditText) findViewById(R.id.editTextPassword)).getText().toString().trim();

        // checks user entries
        if (username.length()==0 || password.length()==0){

            showToast(context, "S'han d'omplir els camps");
            return;

        }

        ExecutorService executor = Executors.newSingleThreadExecutor();

        Button loginBtn = (Button) findViewById(R.id.logoutBtn);
        loginBtn.setEnabled(false);

        // request (must be done in an other thread)
        Future<ValorsResposta> future = executor.submit(()->{return CommController.doLogin(username,password);});

        // result processing
        try {
            ValorsResposta resposta = future.get();
            Gson gson= new Gson();
            Log.d("RESPOSTA LOGIN", gson.toJson(resposta));
            if (resposta==null){
                showToast(parent,context, "Error de conexió amb el servidor. ");

            }else{
                if (resposta.getReturnCode() == CommController.OK_RETURN_CODE ) {

                    SingletonSessio.getInstance().setUserConnectat((User) resposta.getData(0, User.class));
                    SingletonSessio.getInstance().setKey((String) resposta.getData(1,String.class));
                    Log.d("sessionCode", SingletonSessio.getInstance().getKey());
                    if (SingletonSessio.getInstance().getUserConnectat().isAdmin()) { Utility.gotoActivity(this, HomeAdminActivity.class);}

                    //if (SingletonSessio.getInstance().getUserConnectat().isUser()) { Utility.gotoActivity(this, HomeUserActivity.class);}

                    if (SingletonSessio.getInstance().getUserConnectat().isTeacher()) { Utility.gotoActivity(this, HomeTeacherActivity.class);}


                } else {
                    showToast(parent,context, "Usuari o contrasenya incorrectes");
                }

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

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
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