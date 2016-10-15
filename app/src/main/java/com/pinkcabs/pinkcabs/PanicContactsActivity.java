package com.pinkcabs.pinkcabs;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class PanicContactsActivity extends AppCompatActivity {

    ListView lvPanicContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panic_contacts);


//        ArrayList<String> arrayList = null;
//        lvPanicContact = (ListView) findViewById(R.id.lv_panicContact);
//        ArrayAdapter<String> panicAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, arrayList);
//        lvPanicContact.setAdapter(panicAdapter);



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



            }
        });
        alert.setNegativeButton("CANCEL", null);
        alert.create();
        alert.show();

        return true;
    }
}
