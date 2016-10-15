package com.pinkcabs.pinkcabs;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pinkcabs.pinkcabs.Models.FBUser;

public class EditProfileActivity extends AppCompatActivity {

    EditText etName, etContact;
    Button btnEditProfile, btnEditImage;
    DatabaseReference mainDatabase,usersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        etName = (EditText) findViewById(R.id.et_name);
        etContact = (EditText) findViewById(R.id.et_contact);
        btnEditImage = (Button) findViewById(R.id.btn_edit_image);
        btnEditProfile = (Button) findViewById(R.id.btn_edit_image);

        mainDatabase = FirebaseDatabase.getInstance().getReference();
        usersList = mainDatabase.child("users");


        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s1 = etName.getText().toString();
                String s2 = etContact.getText().toString();
                FBUser fbuser = new FBUser(s2,user.getEmail(),"",s2);
                usersList.child(user.getUid()).setValue(fbuser).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent =new Intent(getApplicationContext(),AccountActivity.class);
                        startActivity(intent);
                    }
                });

            }
        });

    }
}
