package com.pinkcabs.pinkcabs;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pinkcabs.pinkcabs.Models.PanicContact;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    public FloatingActionButton emergency_two;
    public FloatingActionButton emergency_one;
    public FloatingActionButton emergency_send;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    DatabaseReference mainDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final ArrayList<String> arrayList = new ArrayList<>();
        mainDatabase = FirebaseDatabase.getInstance().getReference();
        mainDatabase.child("users").child(user.getUid()).child("panic_contacts").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                PanicContact panicContact = dataSnapshot.getValue(PanicContact.class);
                arrayList.add(panicContact.getContact());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



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

        Button track = (Button) findViewById(R.id.btn_track);
        Button account = (Button) findViewById(R.id.btn_myAccount);
        Button btnSafety = (Button) findViewById(R.id.btn_safety_details);
        Button btnTakeRide = (Button) findViewById(R.id.btn_take_ride);

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

        btnTakeRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
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
