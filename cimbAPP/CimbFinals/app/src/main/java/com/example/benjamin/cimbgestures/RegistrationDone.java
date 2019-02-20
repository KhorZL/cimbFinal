package com.example.benjamin.cimbgestures;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class RegistrationDone extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_done_page);


        //now we countdown 2 sec and start a new intent

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                moveTaskToBack(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    finishAffinity();

                    startActivity(new Intent(RegistrationDone.this, QrScanner.class));
                }


            }
        }, 2000);



    }


}
