package com.solapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import com.solapp.common.Utility;

public class GoBackActivity extends AppCompatActivity {

    /**
     * Back button processing: go to the HomeActivity and finish current activity
     */
    @Override
    public void onBackPressed() {
        Utility.gotoActivity(this, HomeActivityAdmin.class);
    }

}
