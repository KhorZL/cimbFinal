package com.example.benjamin.cimbgestures;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class TransactionFailure extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.failure_page);

        //retrieve the data from previous activity - studentID
        Bundle extras = getIntent().getExtras();
        final String value = extras.getString("valuekey");
        final String receiverID = extras.getString("receiverkey");
        final String target = extras.getString("targetkey");



        Button button = findViewById(R.id.failurebutton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TransactionFailure.this, PicSelect.class);


                intent.putExtra("valuekey", value);
                intent.putExtra("receiverkey", receiverID);
                intent.putExtra("targetkey", target);

                startActivity(intent);
            }
        });
    }
}
