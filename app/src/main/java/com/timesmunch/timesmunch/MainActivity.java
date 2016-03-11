package com.timesmunch.timesmunch;

import android.accounts.Account;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getCanonicalName();

    private ImageButton mImageButton;
    private SectionArrayAdapter mSectionArrayAdapter;

    private CursorAdapter mCursorAdapter;
    private ListView mListView;

    SharedPreferences mSharedPreferences;
    String[] mSections = {"Home", "World",
            "National", "Politics",
            "NYRegion", "Business",
            "Opinion", "Technology", "Science",
            "Health", "Sports", "Arts",
            "Fashion", "Dining", "Travel",
            "Magazine", "RealEstate"};
    public ArrayList<String> mSectionsArray = new ArrayList<>();
    public Set<String> mPrefsSet = new HashSet<String>();


    // Constants
    // Content provider authority
    public static final String AUTHORITY = "com.timesmunch.timesmunch.StubProvider";
    // Account type
    public static final String ACCOUNT_TYPE = "example.com";
    // Account
    public static final String ACCOUNT = "default_account";

    // Global variables
    // A content resolver for accessing the provider
    ContentResolver mResolver;

    private Account mAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        for (int i = 0; i <mSections.length ; i++) {
            mSectionsArray.add(mSections[i]);

        }

        mSharedPreferences = getSharedPreferences("com.timesmunch.timesmunch.SECTION_PREFS",Context.MODE_PRIVATE);
        mPrefsSet.add("World");
        mSharedPreferences.edit().putStringSet("PREFS_SET", mPrefsSet).apply();
        mListView = (ListView) findViewById(R.id.newsWireListView);
        mSectionArrayAdapter = new SectionArrayAdapter(MainActivity.this, R.layout.activity_main, mSectionsArray);
        mListView.setAdapter(mSectionArrayAdapter);


        mImageButton = (ImageButton)findViewById(R.id.imageButton);
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SelectorActivity.class);
                startActivity(intent);
            }
        });



    }




    //Notification. This notification will be for the breaking news.
    //A simple notification.
    public void newsNotification(){
        Intent intent = new Intent(this,MainActivity.class);

        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(),intent,0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(android.R.drawable.ic_dialog_alert);
        mBuilder.setContentTitle("Breaking News");
        mBuilder.setContentText("#MakeDonaldDrumpfAgain");
        mBuilder.setContentIntent(pIntent);
        mBuilder.setAutoCancel(true);
        mBuilder.setPriority(Notification.PRIORITY_MAX);

        mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.munchlogosmall)).build();

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1,mBuilder.build());

    }
}





