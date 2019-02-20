package com.example.benjamin.cimbgestures;



import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.* ;  // for standard JDBC programs
import java.math.* ; // for BigDecimal and BigInteger support
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.JsonObject;
import com.publit.publit_io.utils.Publitio;
import com.publit.publit_io.utils.PublitioCallback;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PicSelect extends AppCompatActivity {

    MediaPlayer mp;
    String item;

    private Button takePic;
    private Button doneButton;
    private ImageView imageView;
    ContentValues values;
    Uri imageUri;
    String imageurl;
    Connection con;
    Publitio mPublitio;
    boolean bool;
    int boolChanged = 0;
    boolean firebaseCheck = false;


    private static final int REQUEST_ID_READ_WRITE_PERMISSION = 99;
    private static final int REQUEST_ID_IMAGE_CAPTURE = 100;

    //creating reference to firebase storage
    //THIS IS NEEDED!!!
    FirebaseStorage storage;
    StorageReference storageReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pic_select);
        //retrieve the data from previous activity - studentID
        Bundle extras = getIntent().getExtras();
        final String value = extras.getString("valuekey");
        final String receiverID = extras.getString("receiverkey");
        final String target = extras.getString("targetkey");

        //we send the string over to firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef2 = database.getReference("Benjamin").child("same");
        myRef2.setValue(0);



        boolChanged = 0;
        firebaseCheck = false;


        //music
        mp= MediaPlayer.create(getApplicationContext(), R.raw.music);
        mp.start();

        item = "fork";
        final String[] newitem = new String[1];

        DatabaseReference itemref = FirebaseDatabase.getInstance().getReference("Benjamin").child("target");
        itemref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                newitem[0] = dataSnapshot.getValue(String.class);
//                Toast.makeText(PicSelect.this, newitem[0], Toast.LENGTH_LONG).show();
                TextView textView = findViewById(R.id.verify2text);
                textView.setText("Please take a photo containing the item:  " + newitem[0]);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(PicSelect.this, "toast failed", Toast.LENGTH_LONG).show();
            }
        });

        TextView textView = findViewById(R.id.verify2text);

        if (textView.getText().toString().equals("Please take a photo containing the item: null")) {
            textView.setText("Please take a photo containing the item: fork");
        }

        TextView text2View1 = findViewById(R.id.verify2text1);
        text2View1.setText("Sending $" + value + " to " + receiverID);



        //To fix permission issues
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            int readPermission = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            int writePermission = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int internetPermission = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_NETWORK_STATE);
            int networkPermission = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_NETWORK_STATE);


            if (writePermission != PackageManager.PERMISSION_GRANTED ||
                    readPermission != PackageManager.PERMISSION_GRANTED ||
                    internetPermission != PackageManager.PERMISSION_GRANTED ||
                    networkPermission != PackageManager.PERMISSION_GRANTED) {
                // If don't have permission so prompt the user.
                this.requestPermissions(
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.ACCESS_NETWORK_STATE,
                                Manifest.permission.INTERNET},
                        REQUEST_ID_READ_WRITE_PERMISSION
                );
            }
        }



        //done button closes the app.
        doneButton = this.findViewById(R.id.done_button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    ConnectOracle();
                    // create a Statement from the connection
//                    Statement statement = con.createStatement();
//
//                    // insert the data
////                    statement.executeUpdate("INSERT INTO EMP " + "VALUES ('2', '2', '23131', TO_DATE('2019-01-19 21:49:24', 'YYYY-MM-DD HH24:MI:SS'))\n");
////                    statement.executeUpdate("INSERT INTO TABLE1 " + "VALUES ('jon', 50)");
//                    String query = "INSERT INTO TABLE1 (NAME, AGE) VALUES ('jon', 50)";
////                    statement.executeUpdate(query);
//
//                    con.commit();
//                    con.close();

                }
                catch(Exception ex) {
                    Toast.makeText(PicSelect.this, "connection to oracle failed!", Toast.LENGTH_LONG).show();

                }

                //here we send the data to the python CV, and await for a response.

                if (boolChanged == 1 && firebaseCheck == true) {
                    if (bool == true) {
                        //now we go to transaction approved page
                        Intent intent = new Intent(PicSelect.this, TransactionSuccess.class);

                        intent.putExtra("valuekey", value);
                        intent.putExtra("receiverkey", receiverID);
                        intent.putExtra("targetkey", target);

                        startActivity(intent);
                        Toast.makeText(PicSelect.this, "connection to oracle successful!", Toast.LENGTH_LONG).show();
                    } else {
                        //now we go to transaction failed page
                        Intent intent = new Intent(PicSelect.this, TransactionFailure.class);


                        intent.putExtra("valuekey", value);
                        intent.putExtra("receiverkey", receiverID);
                        intent.putExtra("targetkey", target);


                        startActivity(intent);
                    }
                } else {    //means user did not take pic
                    Toast.makeText(PicSelect.this, "Please submit a picture", Toast.LENGTH_LONG).show();
                }
            }
        });

        takePic = this.findViewById(R.id.take_pic_button);
        imageView = this.findViewById(R.id.imageView);

        takePic.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });


        DatabaseReference dataref = FirebaseDatabase.getInstance().getReference("Benjamin");
        dataref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String id = dataSnapshot.getKey();
//                Toast.makeText(PicSelect.this, id, Toast.LENGTH_LONG).show();

                if (id.equals("result")) {
                    String result = dataSnapshot.getValue().toString();
                    Toast.makeText(PicSelect.this, "Processing complete, press done", Toast.LENGTH_LONG).show();

                    if (result.equals("1")) {
                        bool = true;
                    } else {
                        bool = false;
                    }

                    firebaseCheck = true;
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

//                String id = dataSnapshot.getKey();
////                Toast.makeText(PicSelect.this, id, Toast.LENGTH_LONG).show();
//
//                if (id.equals("result")) {
//                    String result = dataSnapshot.getValue().toString();
////                    Toast.makeText(PicSelect.this, result, Toast.LENGTH_LONG).show();
//
//                    if (result.equals("1")) {
//                        bool = true;
//                        boolChanged = 1;
//                    } else {
//                        bool = false;
//                        boolChanged = 1;
//                    }
//                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


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

    private void captureImage() {
        values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        // Create an implicit intent, for image capture.
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        // Start camera and wait for the results.
        startActivityForResult(intent, REQUEST_ID_IMAGE_CAPTURE);
    }


    // When you have the request results
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //
        switch (requestCode) {
            case REQUEST_ID_READ_WRITE_PERMISSION: {

                // Note: If request is cancelled, the result arrays are empty.
                // Permissions granted (read/write).
                if (grantResults.length > 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED
                        && grantResults[3] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Permission granted!", Toast.LENGTH_LONG).show();

                }
                // Cancelled or denied.
                else {
                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }


    // When results returned
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPublitio = new Publitio(this);
        Map<String, String> create = new HashMap<>();
        if (requestCode == REQUEST_ID_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                try {
                    //we retrieve the bitmap form the intent
                    boolChanged = 1;
                    Bitmap pic = MediaStore.Images.Media.getBitmap(
                            getContentResolver(), imageUri);
                    imageView.setImageBitmap(pic);
                    imageurl = getRealPathFromURI(imageUri);
                    //we retrieve an publito callback
                    mPublitio.files().uploadFile(imageUri, create, new PublitioCallback<JsonObject>() {
                        @Override
                        public void success(JsonObject result) {
                            //get instance of firebase database and insert publito URL to it
                            //we send the string over to firebase
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference();
                            myRef.child("Benjamin").child("image").setValue(result.get("url_download").toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                            Toast.makeText(PicSelect.this, "Please hold on..", Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PicSelect.this, "Image upload failure", Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                        @Override
                        public void failure(String message) {
                            Toast.makeText(PicSelect.this, "Error uploading to publito", Toast.LENGTH_LONG).show();
                        }
                    });

                } catch (Exception e) {
                    Toast.makeText(PicSelect.this, "ERROR", Toast.LENGTH_SHORT).show();
                }

            }   else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(PicSelect.this, "Action canceled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(PicSelect.this, "Action Failed", Toast.LENGTH_LONG).show();
            }
        }






    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void ConnectOracle() {


        String driver = "oracle.jdbc.driver.OracleDriver"; //

        String serverName = "129.213.55.164";
        String portNumber = "1521";
        String db = "CIMBDB";
        String url = "jdbc:oracle:thin:@" + serverName + ":" + portNumber + ":"
                + db; // connectOracle is the data
        // source name
        String user = "sutd"; // username of oracle database
        String pwd = "SUTDaa11$$"; // password of oracle database
        con = null;
        ServerSocket serverSocket = null;

        try {
            Class.forName(driver);// for loading the jdbc driver

            System.out.println("JDBC Driver loaded");

            con = DriverManager.getConnection(url, user, pwd);// for
            // establishing
            // connection
            // with database
            Statement stmt = con.createStatement();

            serverSocket = new ServerSocket(1521);
            System.out.println("Listening :1521");


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}






