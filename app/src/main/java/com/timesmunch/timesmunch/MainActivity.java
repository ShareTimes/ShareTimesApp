package com.timesmunch.timesmunch;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getCanonicalName();

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

    private TextView mTitle;
    private TextView mSubtitle;
    private Button mNext;



    Account mAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = (TextView) findViewById(R.id.title);
        mSubtitle = (TextView) findViewById(R.id.subtitle);
        mNext = (Button) findViewById(R.id.next);

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle settingsBundle = new Bundle();
                settingsBundle.putBoolean(
                        ContentResolver.SYNC_EXTRAS_MANUAL, true);
                settingsBundle.putBoolean(
                        ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
                /*
                 * Request the sync for the default account, authority, and
                 * manual sync settings
                 */
                ContentResolver.requestSync(mAccount, AUTHORITY, settingsBundle);
                Intent intent = new Intent(MainActivity.this, SelectorActivity.class);
                overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_in);
                startActivity(intent);
            }
        });
    }
}
