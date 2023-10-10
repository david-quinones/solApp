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

public class AddUserActivity extends GoBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
    }

    /**
     * One click response
     * Sends add user request to the server, after have checked data;
     * if data is bad, warns to the user and does nothing
     * @param view source event
     */
    public void addUser(View view) {
        String name, userName;
        Context context = getApplicationContext();
        AppCompatActivity parent = this;
        EditText editTextName;
        EditText editTextUsername;

        editTextName = (EditText) findViewById(R.id.eTAddUserName);
        editTextUsername = (EditText) findViewById(R.id.eTAddUserUserName);

        name = editTextName.getText().toString().trim();
        userName = editTextUsername.getText().toString().trim();


        Button btn = (Button) findViewById(R.id.btnAddUser);

        if (name.isEmpty() || userName.isEmpty()) {
            Utility.showToast(context, "Dades incompletes!!");
            return;
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();

        btn.setEnabled(false);

        // request (must be done in an other thread)

        Future<Integer> future = executor.submit(() -> {
            return CommController.doAddUser(new User(userName, name));
        });

        // result processing

        try {
            if (future.get() == CommController.OK_RETURN_CODE) {
                editTextName.setText("");
                editTextUsername.setText("");
                Utility.showToast(parent, context, "Usuari afegit!");
            } else {
                Utility.showToast(parent, context, "Dades erronies!!");
            }
        } catch (ExecutionException | InterruptedException e) {
            Utility.showToast(parent, context, "Error (" + e.getMessage() + ")");
        } finally {
            btn.setEnabled(true);
        }
    }

}
