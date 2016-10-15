package com.pinkcabs.pinkcabs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button btnTakeRide = (Button) findViewById(R.id.btn_take_ride);
        Button btnMyAccount = (Button) findViewById(R.id.btn_myAccount);
        Button btnSafety = (Button) findViewById(R.id.btn_safety_details);
        Button btnTrack = (Button) findViewById(R.id.btn_track);

        btnTakeRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                startActivity(intent);
            }
        });


        btnMyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AccountActivity.class);
                startActivity(intent);
            }
        });

        btnSafety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SafetyActivity.class);
                startActivity(intent);
            }
        });
    }
}
