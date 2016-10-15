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
import android.widget.Toast;

import com.google.android.gms.drive.query.Query;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pinkcabs.pinkcabs.Models.FBUser;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import mehdi.sakout.fancybuttons.FancyButton;

public class EditProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 12341;
    Uri uri;

    EditText etName, etContact;
    FancyButton btnEditProfile, btnEditImage;
    EditText etName, etContact, etTrustedContact;
    Button btnEditProfile, btnEditImage;
                    StorageReference storageRef;
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
       // etTrustedContact = (EditText) findViewById(R.id.et_trusted_contact);
        btnEditImage = (Button) findViewById(R.id.btn_edit_image);
        btnEditProfile = (Button) findViewById(R.id.btn_edit_profile);
        btnEditImage = (FancyButton) findViewById(R.id.btn_edit_image);
        btnEditProfile = (FancyButton) findViewById(R.id.btn_edit_image);

        mainDatabase = FirebaseDatabase.getInstance().getReference();
        usersList = mainDatabase.child("users");
                    FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://pinkcabs-90647.appspot.com/DPs/"+user.getUid());


        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: started");

                String s1 = etName.getText().toString();
                String s2 = etContact.getText().toString();
<<<<<<< HEAD
//                String s3 = etTrustedContact.getText().toString();
                 FBUser fbuser = new FBUser(s2,user.getEmail(),"",s1);
=======
                String s3 = etTrustedContact.getText().toString();
                FBUser fbuser = new FBUser(s2,user.getEmail(),"",s1);
                usersList.orderByChild("contact").equalTo(s3).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String user1 = dataSnapshot.getChildren().iterator().next().getKey();
                        Log.d(TAG, "onDataChange: ");
                        mainDatabase.child("trusted_contacted").child(user1).child(user.getUid()).setValue("true");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
>>>>>>> c629374cbc6eabe919d30dc67b405de9404f2056
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
            uploadPhoto(uri);


            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ImageView imageView = (ImageView) findViewById(R.id.iv_image);
//                Picasso.with(EditProfileActivity.this).load(uri.getPath())
//                        //.resize(100,100)
//                        .into(imageView);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void uploadPhoto(Uri uri) {

        Toast.makeText(this, "Uploading...", Toast.LENGTH_SHORT).show();

        storageRef.putFile(uri)
                .addOnSuccessListener(EditProfileActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG, "uploadPhoto:onSuccess:" +
                                taskSnapshot.getMetadata().getReference().getPath());
                        Toast.makeText(EditProfileActivity.this, "Image uploaded",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "uploadPhoto:onError", e);
                        Toast.makeText(EditProfileActivity.this, "Upload failed",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
