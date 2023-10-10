package com.solapp.activities;

import static com.solapp.common.Utility.showToast;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.solapp.R;
import com.solapp.common.CommController;
import com.solapp.common.Utility;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class HomeActivityAdmin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);
    }

    /**
     * One click response
     * goes to the UsersList Activity
     * @param view source event
     *
    public void goUsersList(View view){
        Utility.gotoActivity(this, UsersListActivity.class);
    }
    */



    /**
     * One click response
     * goes to the AddUser Activity
     * @param view source event
     */
    public void goAddUser(View view){
        Utility.gotoActivity(this, AddUserActivity.class);
    }
    /**
     * One click response
     * goes to the QueryUser Activity
     * @param view source event
     */
    public void goQueryUser(View view){
        Utility.gotoActivity(this, QueryUserActivity.class);
    }

    /**
     *  One click response
     *  closes session and returns to the login screen; on error, warns to the user
     * @param view source event
     */
    public void doLogout(View view){

        Context context = getApplicationContext();
        HomeActivityAdmin parent = this;

        Button btn = (Button) findViewById(R.id.BtnLogout);

        // request (must be done in an other thread)
        ExecutorService executor = Executors.newSingleThreadExecutor();
        btn.setEnabled(false);
        Future<Integer> future = executor.submit(() -> {
            return CommController.doLogout();
        });

        // result processing
        try {
            int result=future.get();
            if (result == CommController.OK_RETURN_CODE) {
                Utility.gotoActivity(this, MainActivity.class);
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
        showToast(this,getApplicationContext(), "Forbidden!!");
    }




}
