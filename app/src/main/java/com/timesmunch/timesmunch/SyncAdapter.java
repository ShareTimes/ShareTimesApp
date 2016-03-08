package com.timesmunch.timesmunch;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by User_1_Benjamin_Rosenthal on 3/7/16.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = SyncAdapter.class.getSimpleName();

    // Global variables
    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;

    /**
     * Set up the sync adapter
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }

    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();

    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        String data = "";
//        try {
//            URL url = new URL("http://api.nytimes.com/svc/news/v3/content/nyt/all/.json?limit=5&api-key=fd0457bbde566c4783e7643346b77859:5:74605174");
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.connect();
//            InputStream inStream = connection.getInputStream();
//            data = getInputData(inStream);
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//
//
//        Gson gson = new Gson();
//        NYTSearchResult result = gson.fromJson(data, NYTSearchResult.class);
//        for (int i = 0; i < 5; i++) {
//            String title = result.getResults().get(i).getTitle();
//            Log.d(TAG, "THE TITLE OF THE " + (i + 1)
//                    + " ARTICLE IS: " + title);
//        }
    }

    private String getInputData(InputStream inStream) throws IOException {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));

        String data = null;

        while ((data = reader.readLine()) != null) {
            builder.append(data);
        }

        reader.close();

        return builder.toString();
    }
}
