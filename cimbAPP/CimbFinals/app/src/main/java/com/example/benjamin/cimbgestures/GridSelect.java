package com.example.benjamin.cimbgestures;


import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GridSelect extends AppCompatActivity {

    MediaPlayer mp;
    String item;
    String value;
    String receiverID;
    String target;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_select);

        //retrieve the data from previous activity - studentID
        Bundle extras = getIntent().getExtras();
        value = extras.getString("valuekey");
        receiverID = extras.getString("receiverkey");
        target = extras.getString("targetkey");


        //we send the string over to firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef2 = database.getReference("Benjamin").child("same");
        myRef2.setValue(1);

        //music
        mp= MediaPlayer.create(getApplicationContext(), R.raw.music);
        mp.start();

        item = "clock";

        TextView textView = findViewById(R.id.verifytext);
        textView.setText("Select the grid containing the " + item);

        TextView textView1 = findViewById(R.id.verifytext1);
        textView1.setText("Sending $" + value + " to " + receiverID);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mp.stop();
        mp.release();

    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            mp.reset();
            mp.prepare();
            mp.stop();
            mp.release();
            mp=null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }



    public void clicked(View view) {
        int id = view.getId();

        if (id == R.id.button2) {

            Intent intent = new Intent(GridSelect.this, TransactionSuccess.class);

            intent.putExtra("valuekey", value);
            intent.putExtra("receiverkey", receiverID);
            intent.putExtra("targetkey", target);


            //we send the string over to firebase
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef2 = database.getReference("Benjamin").child("tempresult");
            myRef2.setValue(1);



            startActivity(intent);

        } else {
            Intent intent = new Intent(GridSelect.this, TransactionFailureGrid.class);

            intent.putExtra("valuekey", value);
            intent.putExtra("receiverkey", receiverID);
            intent.putExtra("targetkey", target);


            //we send the string over to firebase
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef2 = database.getReference("Benjamin").child("tempresult");
            myRef2.setValue(0);

            startActivity(intent);

        }


//        } else if (id == R.id.button2) {
//            Toast.makeText(GridSelect.this, "Yay", Toast.LENGTH_LONG).show();
//
//        } else if (id == R.id.button3) {
//
//        } else if (id == R.id.button4) {
//
//        } else if (id == R.id.button5) {
//
//        } else if (id == R.id.button6) {
//
//        } else if (id == R.id.button7) {
//
//        } else if (id == R.id.button8) {
//
//        } else if (id == R.id.button9) {
//
//        }
    }

    public void ConnectOracle() {


        Connection connection = null;
        try {

            // Load the Oracle JDBC driver

            String driverName = "oracle.jdbc.driver.OracleDriver";

            Class.forName(driverName);


            // Create a connection to the database

            String serverName = "129.213.55.164";

            String serverPort = "1521";

            String sid = "CIMBDB";

            String url = "jdbc:oracle:thin:@" + serverName + ":" + serverPort + ":" + sid;

            String username = "sutd";

            String password = "SUTDaa11$$";

            connection = DriverManager.getConnection(url, username, password);



            System.out.println("Successfully Connected to the database!");


        } catch (ClassNotFoundException e) {

            System.out.println("Could not find the database driver " + e.getMessage());
        } catch (SQLException e) {

            System.out.println("Could not connect to the database " + e.getMessage());
        }



    }
}