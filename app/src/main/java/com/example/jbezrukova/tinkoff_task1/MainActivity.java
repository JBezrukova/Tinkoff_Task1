package com.example.jbezrukova.tinkoff_task1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    final static int SECOND_ACTIVITY_RESULT_CODE = 1432;
    final static String EXTRA_NAME = "list";
    private static final int REQUEST_CODE_READ_CONTACTS = 987;
    private final String TAG = "MainActivity";
    private ArrayList<String> contactList;
    private final String LIST_KEY = "contactsList";
    private ListView listView;
    private Button getContacts;
    private Intent getContactsActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getContacts = findViewById(R.id.getContactsButton);
        getContactsActivity = new Intent(this, SecondActivity.class);

        int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);

        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            setOnClickToGetContacts();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},
                    REQUEST_CODE_READ_CONTACTS);
        }
    }

    private void setOnClickToGetContacts() {
        getContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(getContactsActivity, SECOND_ACTIVITY_RESULT_CODE);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_READ_CONTACTS:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setOnClickToGetContacts();
                } else {
                    Toast.makeText(getApplicationContext(), "No permission granted", Toast.LENGTH_LONG).show();
                }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SECOND_ACTIVITY_RESULT_CODE && resultCode == RESULT_OK) {
            Log.d(TAG, "Starting getting result");
            HashMap<String, String> listOfContacts = (HashMap<String, String>) data.getExtras().get("list");
            if (listOfContacts.size() > 0) {
                contactList = new ArrayList<>();
                for (Map.Entry entry : listOfContacts.entrySet()) {
                    contactList.add(entry.getKey() + "  " + entry.getValue());
                }
                Collections.sort(contactList);
                setItemsToList();
            } else {
                Toast.makeText(getApplicationContext(), "No contacts founded", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void setItemsToList() {
        listView = findViewById(R.id.list);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.simple_list_item_1, contactList);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (contactList != null)
            outState.putStringArrayList(LIST_KEY, contactList);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        contactList = savedInstanceState.getStringArrayList(LIST_KEY);
        setItemsToList();

    }

}
