package com.example.benjamin.cimbgestures;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QrScanner extends AppCompatActivity {

    public static TextView resultTextView;
    Button scanButton;
    Button nextButton;
    private static final int MY_CAMERA_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_scanner);

        resultTextView = findViewById(R.id.result_text);
        scanButton = findViewById(R.id.button_scan);
        nextButton = findViewById(R.id.nextButton);



        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    MY_CAMERA_REQUEST_CODE);
        }


        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(QrScanner.this, ScanCodeActivity.class));
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resultTextView.getText().equals("result shown here")) {  //means user has not scanned qr yet
                    Toast.makeText(QrScanner.this, "Please scan the QR code first", Toast.LENGTH_LONG).show();

                } else {
                    String computerIP = resultTextView.getText().toString();
                    resultTextView.setText("Sucessfully scanned");
                    WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                    String phoneIP = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
                    //we check the data to see if the the computer and the phone is on the same network.

                    String computerIPsub = computerIP.substring(0,10);
                    String phoneIPsub = phoneIP.substring(0,10);

                    String[] parts = computerIP.split("-");
                    final String value = parts[1];
                    final String receiverID = parts[2];
                    //final String target = parts[3];
                    final String target = "placeholder";



                    if (computerIPsub.equals(phoneIPsub)) {
                        //we will use GridSelect because the two devices are on the same network
                        Toast.makeText(QrScanner.this, "Laptop and phone are on the same network", Toast.LENGTH_LONG).show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                final Intent mainIntent = new Intent(QrScanner.this, GridSelect.class);
                                mainIntent.putExtra("valuekey", value);
                                mainIntent.putExtra("receiverkey", receiverID);
                                mainIntent.putExtra("targetkey", target);
                                QrScanner.this.startActivity(mainIntent);
                                QrScanner.this.finish();
                            }
                        }, 1300);
                    } else {
                        Toast.makeText(QrScanner.this, "Laptop and phone are not on the same network", Toast.LENGTH_LONG).show();
//                        Toast.makeText(QrScanner.this, computerIP, Toast.LENGTH_LONG).show();



                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                final Intent mainIntent = new Intent(QrScanner.this, PicSelect.class);
                                mainIntent.putExtra("valuekey", value);
                                mainIntent.putExtra("receiverkey", receiverID);
                                mainIntent.putExtra("targetkey", target);
                                QrScanner.this.startActivity(mainIntent);
                                QrScanner.this.finish();
                            }
                        }, 1300);

                    }




                }
            }
        });

    }
}
