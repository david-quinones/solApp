package estel.solapp.activities;

import static estel.solapp.common.Utility.showToast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import estel.solapp.R;
import estel.solapp.common.CommController;
import estel.solapp.common.Utility;
import estel.solapp.common.ValorsResposta;

public class HomeAdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);
    }

    /**
     * One click response
     * goes to the AddUser Activity
     * @param view source event
     */
    public void goAddUser(View view){
        //Utility.gotoActivity(this, AddUserActivity.class);
    }
    /**
     * One click response
     * goes to the QueryUser Activity
     * @param view source event
     */
//    public void goQueryUser(View view){Utility.gotoActivity(this, QueryUserActivity.class);}

    /**
     *  One click response
     *  closes session and returns to the login screen; on error, warns to the user
     * @param view source event
     */
    public void doLogout(View view){

        Context context = getApplicationContext();
        HomeAdminActivity parent = this;
        String sessionCode =;
        Button btn = (Button) findViewById(R.id.logoutBtn);

        // request (must be done in an other thread)
        ExecutorService executor = Executors.newSingleThreadExecutor();
        btn.setEnabled(false);
        Future<ValorsResposta> future = executor.submit(() -> {return CommController.doLogout();});

        // result processing
        try {
            ValorsResposta resposta=future.get();
            if (resposta.getReturnCode() == CommController.OK_RETURN_CODE) {
                Utility.gotoActivity(this, LoginActivity.class);
//                Intent i = new Intent(this.getApplicationContext(), MainActivity.class);
//                startActivity(i);
//                finish();
            } else {
                showToast(parent, context, "Error tancant sessió!");
            }
        } catch (ExecutionException | InterruptedException e) {
            showToast(parent, context, "Error (" + e.getMessage() + ")");
        } finally {
            btn.setEnabled(true);
        }
        ;

    }

    /**
     * Prevents return to the login screen without previous logout
     */
    @Override
    public void onBackPressed() {
        showToast(this,getApplicationContext(), "S'ha de sortir per tancar sessió");
    }
}