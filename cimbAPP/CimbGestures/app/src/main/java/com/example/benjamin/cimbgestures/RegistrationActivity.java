package com.example.benjamin.cimbgestures;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegistrationActivity extends AppCompatActivity {
    private EditText inputUsername, inputPassword;
    private Button exitButton, registerButton;
    DatabaseReference mRootDatabaseRef = FirebaseDatabase.getInstance().getReference();
    //StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    DatabaseReference databaseReferencePart1;

    String CHILD_NODE_PART1 = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);

        inputUsername = findViewById(R.id.inputUsername);
        inputPassword = findViewById(R.id.inputPassword);

        exitButton = findViewById(R.id.exitButton);
        registerButton = findViewById(R.id.regButton);

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });

        databaseReferencePart1 = mRootDatabaseRef.child(CHILD_NODE_PART1);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = inputUsername.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if(TextUtils.isEmpty(username)) {
                    Toast.makeText(RegistrationActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(RegistrationActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    MessageDigest digest = MessageDigest.getInstance("SHA-256");
                    byte[] encodedhash = digest.digest(
                            password.getBytes(StandardCharsets.UTF_8));
                    password = new String(encodedhash);
                    databaseReferencePart1.child(username).child("password").setValue(String.valueOf(password));
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }


                startActivity(new Intent(RegistrationActivity.this, RegisterGesture.class));
            }
        });
    }
}
    /*
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences pref = this.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);


        int howManyTimesBeenRun = pref.getInt(NUMBER_OF_TIMES_RUN_KEY,defaultValue);   //number of times run always starts at default 0

        if (howManyTimesBeenRun != 0 ) {
            Intent i = new Intent(RegistrationActivity.this, WelcomePage.class);
            startActivity(i);
        } */

//        Intent i = new Intent(RegistrationActivity.this, WelcomePage.class);
//        startActivity(i);

//    }
