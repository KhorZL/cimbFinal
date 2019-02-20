package com.example.benjamin.cimbgestures;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class WelcomePage extends AppCompatActivity {
    //implements View.OnTouchListener
    private int howManyTimesBeenRun = 0;
    private static final String NUMBER_OF_TIMES_RUN_KEY = "NUMBER_OF_TIMES_RUN_KEY";
    private SharedPreferences sharedPreferences;
    private SharedPreferences sshareeP;
    private WifiManager wifiManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);
        sharedPreferences = getPreferences(Context.MODE_PRIVATE); //create shared preferences
        sshareeP = getSharedPreferences("ssid", MODE_PRIVATE);
        wifiManager = getApplicationContext().getSystemService(WifiManager.class);
        String ssid = wifiManager.getConnectionInfo().getSSID();


        if (sshareeP.getString(ssid,null)!=null) {
            Toast.makeText(this.getApplicationContext(), "SSID Verified", Toast.LENGTH_LONG).show();
            Log.d("TAG", "Ssid succ: " + ssid);
            int defaultValue = 0;
            //read
            howManyTimesBeenRun = sharedPreferences.getInt(NUMBER_OF_TIMES_RUN_KEY, defaultValue);   //number of times run always starts at default 0
            //first time message
            if (howManyTimesBeenRun == 0) {   //aka first time so registration only
                //Toast.makeText(this,"Welcome to first-time registration", Toast.LENGTH_LONG).show();
                //for debugging
                editSharedPref();


                //wait for 2 sec and auto move to next screen
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final Intent mainIntent = new Intent(WelcomePage.this, RegistrationActivity.class);
                        WelcomePage.this.startActivity(mainIntent);
                        WelcomePage.this.finish();
                    }
                }, 2000);


            } else {//if it is not the first time starting the app - meaning that we are doing a verification

                editSharedPref();

                //wait for 2 sec and auto move to next screen
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //now we open the verification page

                        final Intent i = new Intent(WelcomePage.this, VerifyGesture.class);
                        WelcomePage.this.startActivity(i);
                        WelcomePage.this.finish();
                    }
                }, 2000);

            }

            //touch to go next screen
//        RelativeLayout layout= findViewById(R.id.relativeLayout);
//        layout.setOnTouchListener(this);

        } else {
            Log.d("TAG", "Ssid fucc : " + ssid);
            Toast.makeText(this.getApplicationContext(), "SSID Not Verified", Toast.LENGTH_LONG).show();
            //TODO verify
            Intent intento = new Intent(WelcomePage.this, SsidVerification.class);
            WelcomePage.this.startActivity(intento);
            WelcomePage.this.finish();
            //verify(ssid);
        }


        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
    }

    private void editSharedPref() {
        howManyTimesBeenRun++;  //increment count by 1
        //for debugging - next line
        //Toast.makeText(WelcomePage.this, "number of times app ran: " + howManyTimesBeenRun, Toast.LENGTH_SHORT).show();
        SharedPreferences.Editor editor = sharedPreferences.edit(); //open a editor
        editor.putInt(NUMBER_OF_TIMES_RUN_KEY,howManyTimesBeenRun); //save the number of times ran
        editor.commit();    //commit the changes
    }

    private void verify(String ssid){
        SharedPreferences.Editor editor = sharedPreferences.edit(); //open a editor
        editor.putString(ssid,ssid);
        editor.commit();
    }

    private void unverify(String ssid){
        SharedPreferences.Editor editor = sharedPreferences.edit(); //open a editor
        editor.remove(ssid);
        editor.commit();

    }




//    @Override
//    public boolean onTouch(View view, MotionEvent motionEvent) {
//        Intent i = new Intent(WelcomePage.this,  RegistrationActivity.class);
//        startActivity(i);
//        // here we move to the registration page.
//        return true;
//    }
}
