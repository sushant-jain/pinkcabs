package com.pinkcabs.pinkcabs;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    public FloatingActionButton emergency_two;
    public FloatingActionButton emergency_one;
    public FloatingActionButton emergency_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        emergency_two = (FloatingActionButton) findViewById(R.id.btn_emergency2);
        emergency_two.setBackgroundTintList(
                ColorStateList.valueOf(Color.parseColor("#27ae60"))
        );

        emergency_one = (FloatingActionButton) findViewById(R.id.btn_emergency1);
        emergency_one.setBackgroundTintList(
                ColorStateList.valueOf(Color.parseColor("#fb8c00"))
        );

        emergency_send = (FloatingActionButton) findViewById(R.id.btn_emergency_send);
        emergency_send.setBackgroundTintList(
                ColorStateList.valueOf(Color.parseColor("#d50000"))
        );

        Button track = (Button) findViewById(R.id.btn_safety_details);
        Button account = (Button) findViewById(R.id.btn_myAccount);
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
                startActivity(intent);
            }
        });

        emergency_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emergency_two.setVisibility(View.GONE);
                emergency_one.setVisibility(View.VISIBLE);
            }
        });

        emergency_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emergency_one.setVisibility(View.GONE);
                emergency_send.setVisibility(View.VISIBLE);
            }
        });

        emergency_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phoneNo = "9868904222";
                String sms = "I AM FINE, GO AWAY PLSSSSSSSS!!! HAHA LOL LEL";

                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, sms, null, null);
                    Toast.makeText(getApplicationContext(), "SMS Sent!",
                            Toast.LENGTH_LONG).show();

                    emergency_two.setVisibility(View.VISIBLE);
                    emergency_one.setVisibility(View.GONE);
                    emergency_send.setVisibility(View.GONE);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),
                            "SMS failed, please try again in a moment!",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
    }
}
