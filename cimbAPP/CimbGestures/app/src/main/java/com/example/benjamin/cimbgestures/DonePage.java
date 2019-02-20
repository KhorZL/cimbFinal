package com.example.benjamin.cimbgestures;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class DonePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.done_page);


        //now we countdown 2 sec and app destroy

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                moveTaskToBack(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    finishAffinity();
                }
                finish();   //this should close everything
                //android.os.Process.killProcess(android.os.Process.myPid());
                //System.exit(1);
            }
        }, 2000);



    }


}
