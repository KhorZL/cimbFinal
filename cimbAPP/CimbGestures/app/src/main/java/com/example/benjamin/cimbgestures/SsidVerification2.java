package com.example.benjamin.cimbgestures;
//NOT IN USE.
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SsidVerification2 extends AppCompatActivity {


    private SharedPreferences sharedPreferences;
    private WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ssid_verification);

        sharedPreferences = getSharedPreferences("ssid", MODE_PRIVATE);
        //sharedPreferences = getPreferences(Context.MODE_PRIVATE); //create shared preferences
        wifiManager = getApplicationContext().getSystemService(WifiManager.class);
        final String ssid = wifiManager.getConnectionInfo().getSSID();


        final Button verify = findViewById(R.id.verify_button);
        Button unverify = findViewById(R.id.unverify_button);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verify(ssid);
                Intent intent = new Intent(SsidVerification2.this, WelcomePage.class);
                SsidVerification2.this.startActivity(intent);
            }
        });





        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SsidVerification2.this);
                builder.setTitle("Message from the Sphinx");
                builder.setMessage("Which creature has one voice and yet becomes four-footed and two-footed and three-footed?");
                builder.setPositiveButton("A man", null);
                builder.setNegativeButton("Dunno", null);

                builder.show();
            }
        });


    }

    private void verify(String ssid){
        SharedPreferences.Editor editor = sharedPreferences.edit(); //open a editor
        editor.putString(ssid,ssid);
        editor.commit();
        Log.d("TAG", "SP : ");
    }



}


