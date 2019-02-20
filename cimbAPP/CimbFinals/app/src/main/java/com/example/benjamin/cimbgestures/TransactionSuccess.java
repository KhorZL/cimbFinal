package com.example.benjamin.cimbgestures;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class TransactionSuccess extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success_page);

        Bundle extras = getIntent().getExtras();
        final String value = extras.getString("valuekey");
        final String receiverID = extras.getString("receiverkey");
        final String target = extras.getString("targetkey");

        //we send the string over to firebase
        String uuid = UUID.randomUUID().toString();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("transactions").child(uuid);
        myRef.setValue("Benjamin transferred $" + value + " to " + receiverID);


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
