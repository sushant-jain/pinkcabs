package com.pinkcabs.pinkcabs;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.TransitionDrawable;
import android.support.design.widget.FloatingActionButton;
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

//    private BubblesManager bubblesManager;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    DatabaseReference mainDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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

        final ArrayList<String> arrayList = new ArrayList<>();
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

        FancyButton track = (FancyButton) findViewById(R.id.btn_safety_details);
        FancyButton account = (FancyButton) findViewById(R.id.btn_myAccount);
        account.setOnClickListener(new View.OnClickListener()

                                   {
                                       @Override
                                       public void onClick(View view) {
                                           Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
                                           startActivity(intent);
                                       }
                                   }
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

        );

        emergency_send.setOnClickListener(new View.OnClickListener()

                                          {
                                              @Override
                                              public void onClick(View v) {

                                                  String phoneNo = "9868904222";
                                                  String sms = FBUser.copyName + " is not felling safe, please contact ASAP, location at ...... ";    // vishal wil send the lat,long from server

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
                                          }

        );
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
