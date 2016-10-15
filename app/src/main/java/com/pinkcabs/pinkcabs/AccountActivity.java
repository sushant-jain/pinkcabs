package com.pinkcabs.pinkcabs;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import mehdi.sakout.fancybuttons.FancyButton;

public class AccountActivity extends AppCompatActivity {

    FancyButton btnEditProf, btnLogout, btnReset, btnPanicContacts;
    private static final String TAG = "AccountActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        Log.d(TAG, "onCreate: hihihi");

        btnEditProf = (FancyButton) findViewById(R.id.btn_edit_prof);
        btnLogout = (FancyButton) findViewById(R.id.btn_logout);
        btnReset = (FancyButton) findViewById(R.id.btn_reset);
        btnPanicContacts = (FancyButton) findViewById(R.id.btn_panic_contacts);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance()
                        .signOut(AccountActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // user is now signed out
                                startActivity(new Intent(getApplicationContext(), EntryActivity.class));
                            }
                        });
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String emailAddress = user.getEmail();

                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Recovery E-mail sent on registered E-mail ID", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        btnEditProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        btnPanicContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(getApplicationContext(), PanicContactsActivity.class);
                startActivity(intent);
            }
        });
    }
}
