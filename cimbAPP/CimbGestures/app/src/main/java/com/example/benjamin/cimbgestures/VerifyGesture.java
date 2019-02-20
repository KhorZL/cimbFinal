package com.example.benjamin.cimbgestures;

import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class VerifyGesture extends AppCompatActivity {



    DatabaseReference mRootDatabaseRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference databaseReferencePart1;
    String CHILD_NODE_PART1 = "Semaphore";
    GestureLibrary lib;
    String user = "user1";

//    TextView txtResult;a

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

//        txtResult = findViewById(R.id.txtResult);

        lib = GestureLibraries.fromRawResource(this, R.raw.gesture);
        if (!lib.load()) {
            finish();
        }
        final GestureOverlayView gesture = findViewById(R.id.gesture);
        gesture.addOnGesturePerformedListener(new GestureOverlayView.OnGesturePerformedListener() {
            @Override
            public void onGesturePerformed(GestureOverlayView gestureOverlayView, Gesture gesture) {
                ArrayList<Prediction> predictionArrayList = lib.recognize(gesture);
                for (Prediction prediction : predictionArrayList) {
                    if (prediction.score > 1.0) {
                        //txtResult.setText(prediction.name);
                        //txtResult.setText("Success");

                        //here we set semaphore to be 1
                        databaseReferencePart1 = mRootDatabaseRef.child(CHILD_NODE_PART1).child(user);
                        databaseReferencePart1.setValue(1);

                        Toast.makeText(VerifyGesture.this, "Successfully verified!", Toast.LENGTH_LONG).show();


                        //quit and return to telegram
                        //now we countdown 3 sec and app destroy

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                moveTaskToBack(true);
                                finish();   //this should close everything
                                //android.os.Process.killProcess(android.os.Process.myPid());
                                //System.exit(1);
                            }
                        }, 2000);


                    } else {
                        //txtResult.setText("Failed");
                        Toast.makeText(VerifyGesture.this, "Failed, please try again!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
    }
}


//<ImageView
//        android:id="@+id/verifygesturepic"
//                android:layout_width="161dp"
//                android:layout_height="197dp"
//                android:layout_centerHorizontal="true"
//
//                android:layout_marginTop="300dp"
//                android:background="@drawable/fingerdraw" />