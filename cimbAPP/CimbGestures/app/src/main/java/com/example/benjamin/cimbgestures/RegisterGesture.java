package com.example.benjamin.cimbgestures;


import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class RegisterGesture extends AppCompatActivity {
    private CanvasView canvasView;
    private Button confirmButton;
    private ImageView disappearPic;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_gesture);

        canvasView = findViewById(R.id.canvas);
        confirmButton= findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearCanvas(canvasView);    //maybe dont need
                Intent i = new Intent(RegisterGesture.this, DonePage.class);
                startActivity(i);

            }
        });

//        disappearPic.findViewById(R.id.registergesturepic);
//        disappearPic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                disappearPic.setVisibility(View.GONE);
//            }
//        });

//        <ImageView
//        android:id="@+id/registergesturepic"
//        android:layout_width="161dp"
//        android:layout_height="197dp"
//        android:layout_centerHorizontal="true"
//
//        android:layout_marginTop="300dp"
//        android:background="@drawable/fingerdraw" />


    }

    public void clearCanvas(View v) {
        canvasView.clearCanvas();
    }
}
