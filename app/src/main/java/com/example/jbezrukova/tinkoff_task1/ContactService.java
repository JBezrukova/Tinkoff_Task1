package com.example.jbezrukova.tinkoff_task1;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.HashMap;

public class ContactService extends IntentService {

    private final String TAG = "ContactServiceLog";


    public ContactService() {
        super("ContactService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service created");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Getting contacts");
        Cursor contacts = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null);
        HashMap<String, String> contactsMap = new HashMap<>();
        if (contacts != null) {
            while (contacts.moveToNext()) {
                String name = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                contactsMap.put(name, phoneNumber);
            }
            contacts.close();
        }
        Intent resultIntent = new Intent();
        resultIntent.setAction(SecondActivity.BROADCAST_ACTION);
        resultIntent.putExtra(MainActivity.EXTRA_NAME, contactsMap);
        sendBroadcast(resultIntent);
    }
}