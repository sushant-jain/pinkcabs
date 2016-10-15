package com.pinkcabs.pinkcabs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pinkcabs.pinkcabs.Models.FBUser;

import java.io.IOException;

public class EditProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 12341;
    Uri uri;

    EditText etName, etContact;
    Button btnEditProfile, btnEditImage;
    boolean picChanged=false;
    private static final String TAG = "EditProfileActivity";
    DatabaseReference mainDatabase,usersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        etName = (EditText) findViewById(R.id.et_name);
        etContact = (EditText) findViewById(R.id.et_contact);
        btnEditImage = (Button) findViewById(R.id.btn_edit_image);
        btnEditProfile = (Button) findViewById(R.id.btn_edit_profile);

        mainDatabase = FirebaseDatabase.getInstance().getReference();
        usersList = mainDatabase.child("users");


        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: started");

                String s1 = etName.getText().toString();
                String s2 = etContact.getText().toString();
                if(picChanged==true)
                {
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReferenceFromUrl("gs://pinkcabs-90647.appspot.com/DPs");


                }
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

        btnEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                picChanged=true;
                ImageView imageView = (ImageView) findViewById(R.id.iv_image);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
