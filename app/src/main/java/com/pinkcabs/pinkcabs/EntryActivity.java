package com.pinkcabs.pinkcabs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import de.hdodenhof.circleimageview.CircleImageView;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import static com.firebase.ui.auth.ui.AcquireEmailHelper.RC_SIGN_IN;

public class EntryActivity extends AppCompatActivity {

    private static final String TAG = "EntryActivity";
    public static final int RC_SIGN_IN  = 101;


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




        Log.d(TAG, "onCreate: here!");
        //dhruv rathi
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(this, HomeActivity.class));
        } else {
            startActivityForResult(
                    // Get an instance of AuthUI based on the default app
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setLogo(R.mipmap.ic_launcher)
                            .setIsSmartLockEnabled(false)
                            .setProviders(AuthUI.EMAIL_PROVIDER, AuthUI.GOOGLE_PROVIDER)
                            .build(this),
                    RC_SIGN_IN);
        }

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // user is signed in!
                Intent intent = new Intent(this, HomeActivity.class);
                finish();
                startActivity(intent);
            } else {
                // user is not signed in. Maybe just wait for the user to press
                // "sign in" again, or show a message
            }
        }
    }
}
