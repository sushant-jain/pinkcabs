package com.pinkcabs.pinkcabs;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.TransitionDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pinkcabs.pinkcabs.Models.PanicContact;

import java.util.ArrayList;

import com.pinkcabs.pinkcabs.Models.FBUser;
import com.txusballesteros.bubbles.BubbleLayout;
import com.txusballesteros.bubbles.BubblesManager;

import de.hdodenhof.circleimageview.CircleImageView;
import mehdi.sakout.fancybuttons.FancyButton;

public class HomeActivity extends AppCompatActivity {

    public FloatingActionButton emergency_two;
    public FloatingActionButton emergency_one;
    public FloatingActionButton emergency_send;
    public TextView txt_two;
    public TextView txt_one;
    public TextView txt_send;
    public RelativeLayout activityHome;
    public static ArrayList<String> arrayList = new ArrayList<>();

    private static final String TAG = "HomeActivity";
    //  private BubblesManager bubblesManager;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    DatabaseReference mainDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        new ServerRequests().newUser(getApplicationContext(),user.getUid());
        startService(new Intent(getApplicationContext(),LocationUpdate.class));
        Log.d(TAG, "onDataChange: "+user.getUid());

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {


                    Intent intent = new Intent(HomeActivity.this,EditProfileActivity.class);

                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        activityHome = (RelativeLayout) findViewById(R.id.activity_home);
        TransitionDrawable transition = (TransitionDrawable) activityHome.getBackground();
        transition.startTransition(2000);

        de.hdodenhof.circleimageview.CircleImageView womanWait = (CircleImageView) findViewById(R.id.womanForCab);
        ImageView cab = (ImageView) findViewById(R.id.img_cab);
        Animation animation2 = AnimationUtils.loadAnimation(HomeActivity.this, R.anim.movers);
        animation2.setRepeatCount(11);
        cab.startAnimation(animation2);

//        transition.reverseTransition(3000);

//        bubblesManager = new BubblesManager.Builder(this)
//                .build();
//        bubblesManager.initialize();
//
//        BubbleLayout bubbleView = (BubbleLayout) LayoutInflater
//                .from(HomeActivity.this).inflate(R.layout.bubble_button, null);
//        bubblesManager.addBubble(bubbleView, 60, 60);

        txt_two = (TextView)

                findViewById(R.id.txt_two);

        txt_one = (TextView)

                findViewById(R.id.txt_one);

        txt_send = (TextView)

                findViewById(R.id.txt_send);

        emergency_two = (FloatingActionButton)

                findViewById(R.id.btn_emergency2);

//        final ArrayList<String> arrayList = new ArrayList<>();
        mainDatabase = FirebaseDatabase.getInstance().getReference();
        mainDatabase.child("panic_contacts").child(user.getUid()).addChildEventListener(new ChildEventListener() {
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

        emergency_one = (FloatingActionButton)

                findViewById(R.id.btn_emergency1);

        emergency_one.setBackgroundTintList(
                ColorStateList.valueOf(Color.parseColor("#fb8c00"))
        );

        emergency_send = (FloatingActionButton)

                findViewById(R.id.btn_emergency_send);

        emergency_send.setBackgroundTintList(
                ColorStateList.valueOf(Color.parseColor("#d50000"))
        );
        FancyButton track = (FancyButton) findViewById(R.id.btn_track);
        FancyButton account = (FancyButton) findViewById(R.id.btn_myAccount);
        FancyButton btnSafety = (FancyButton) findViewById(R.id.btn_safety_details);
        FancyButton btnTakeRide = (FancyButton) findViewById(R.id.btn_take_ride);
        account.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
                startActivity(intent);
            }
        });

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
                txt_two.setVisibility(View.GONE);
                txt_one.setVisibility(View.VISIBLE);
            }
        });

        emergency_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emergency_one.setVisibility(View.GONE);
                emergency_send.setVisibility(View.VISIBLE);
                txt_one.setVisibility(View.GONE);
                txt_send.setVisibility(View.VISIBLE);
            }
        });

        emergency_send.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                for(int i=0;i<arrayList.size();i++) {

//                    String phoneNo = "9868904222";
                    String phoneNo = arrayList.get(i);
                    String sms = /*FBUser*/  "I am not felling safe, please contact ASAP";    // vishal wil send the lat,long from server

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
                txt_send.setVisibility(View.GONE);
                txt_two.setVisibility(View.VISIBLE);
            }
        });

        btnTakeRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
            }
        });


        btnSafety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SafetyActivity.class);
                startActivity(intent);
            }
        });
        emergency_two.setVisibility(View.VISIBLE);
        emergency_one.setVisibility(View.GONE);
        emergency_send.setVisibility(View.GONE);
    }
}


