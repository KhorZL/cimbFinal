package com.example.benjamin.cimbgestures;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class WelcomePage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);

        //wait for 2 sec and auto move to next screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent mainIntent = new Intent(WelcomePage.this, RegistrationActivity.class);
                WelcomePage.this.startActivity(mainIntent);
                WelcomePage.this.finish();
            }
        }, 2000);


    }

}
