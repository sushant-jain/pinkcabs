package com.pinkcabs.pinkcabs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AccountActivity extends AppCompatActivity {

    Button btnEditProf, btnLogout, btnReset, btnPanicContacts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        btnEditProf = (Button) findViewById(R.id.btn_edit_prof);
        btnLogout = (Button) findViewById(R.id.btn_logout);
        btnReset = (Button) findViewById(R.id.btn_reset);
        btnPanicContacts = (Button) findViewById(R.id.btn_panic_contacts);

        btnEditProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}
