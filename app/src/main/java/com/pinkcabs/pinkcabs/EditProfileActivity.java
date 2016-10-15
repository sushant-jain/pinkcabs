package com.pinkcabs.pinkcabs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditProfileActivity extends AppCompatActivity {

    EditText etName, etContact;
    Button btnEditProfile, btnEditImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        etName = (EditText) findViewById(R.id.et_name);
        etContact = (EditText) findViewById(R.id.et_contact);
        btnEditImage = (Button) findViewById(R.id.btn_edit_image);
        btnEditProfile = (Button) findViewById(R.id.btn_edit_image);

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s1 = etName.getText().toString();
                String s2 = etContact.getText().toString();


            }
        });

    }
}
