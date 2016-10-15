package com.pinkcabs.pinkcabs;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pinkcabs.pinkcabs.Models.PanicContact;

import java.util.ArrayList;

public class PanicContactsActivity extends AppCompatActivity {

    private static final String TAG = "PanicContactsActivity";
    ListView lvPanicContact;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference mainDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panic_contacts);


        final ArrayList<String> arrayList = new ArrayList<>();
        mainDatabase = FirebaseDatabase.getInstance().getReference();
        lvPanicContact = (ListView) findViewById(R.id.lv_panicContact);
        final ArrayAdapter<String> panicAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        lvPanicContact.setAdapter(panicAdapter);
        Log.d(TAG, "onCreate: yynhnhn");
        mainDatabase.child("users").child(user.getUid()).child("panic_contacts").addChildEventListener(new ChildEventListener() {


            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                PanicContact panicContact = dataSnapshot.getValue(PanicContact.class);
                arrayList.add(panicContact.getName());
                panicAdapter.notifyDataSetChanged();
                Log.d(TAG, "onChildAdded: " + dataSnapshot);
                Log.d(TAG, "onChildAdded: check working\n"+panicContact.getName());
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


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_panic_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        LayoutInflater li = LayoutInflater.from(this);
        final View alertDialogView = li.inflate(R.layout.alert_dialog_panic, null);
        final EditText etPanicNumber = (EditText) alertDialogView.findViewById(R.id.et_panic_number);
        final EditText etNamePanic = (EditText) alertDialogView.findViewById(R.id.et_name_panic_contact);
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Add Panic Contact");
        alert.setView(alertDialogView);
        alert.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                DatabaseReference mainDatabase;
                mainDatabase = FirebaseDatabase.getInstance().getReference();
                String name = etNamePanic.getText().toString();
                String contact = etPanicNumber.getText().toString();
                PanicContact panicContact = new PanicContact(name,contact);
                mainDatabase.child("users").child(user.getUid()).child("panic_contacts").push().setValue(panicContact).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(getApplicationContext(),AccountActivity.class);
                        startActivity(intent);
                    }
                });



            }
        });
        alert.setNegativeButton("CANCEL", null);
        alert.create();
        alert.show();

        return true;
    }
}
