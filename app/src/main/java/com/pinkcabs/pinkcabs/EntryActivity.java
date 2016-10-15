package com.pinkcabs.pinkcabs;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class EntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        de.hdodenhof.circleimageview.CircleImageView womanWait = (CircleImageView) findViewById(R.id.womanForCab);
        ImageView cab = (ImageView) findViewById(R.id.img_cab);
        Animation animation2 = AnimationUtils.loadAnimation(EntryActivity.this, R.anim.movers);

        cab.startAnimation(animation2);

        // pass intent for he activity depending on login info


//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                Intent i =  new Intent(entry_class.this, MainActivity.class);
//                startActivity(i);
//            }
//        }, 3900);




        //dhruv rathi
    }
}
