package com.solapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.solapp.R;
import com.solapp.common.CommController;
import com.solapp.common.Utility;
import com.solapp.models.User;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class QueryUserActivity extends GoBackActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_user);
    }

    /**
     *   One click response
     *   Sends a "query user" request to the server with data entered by the user
     *   if data or result are erroneous, warns the user;
     *   @param view source event
     */
    public void doQueryUser(View view) {
        String name, userName;
        Context context = getApplicationContext();
        AppCompatActivity parent = this;
        EditText editTextName;
        EditText editTextUsername;

        editTextName = (EditText) findViewById(R.id.eTQueryName);
        editTextUsername = (EditText) findViewById(R.id.eTQueryUserName);

        name = editTextName.getText().toString().trim();
        userName = editTextUsername.getText().toString().trim();


        Button btn = (Button) findViewById(R.id.btnQuery);

        if (userName.isEmpty()) {
            Utility.showToast(context, "Escriu un nom d'usuari per consultar!!");
            return;
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();

        btn.setEnabled(false);

        // request (must be done in an other thread)

        Future<User> future = executor.submit(() -> {
            return CommController.doQueryUser(userName);
        });

        // result processing

        try {
            User user=future.get();
            if (user != null) {
                editTextName.setText(user.getName());
            } else {
                editTextName.setText("");
                Utility.showToast(parent, context, "No s'han trobat dades!");
            }
        } catch (ExecutionException | InterruptedException e) {
            Utility.showToast(parent, context, "Error (" + e.getMessage() + ")");
        } finally {
            btn.setEnabled(true);
        }
        ;

    }






}
