package com.timesmunch.timesmunch;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Faraz on 3/8/16.
 */
public class GetNYTData extends AsyncTask<Void, Void, Void> {
    private String data;
    private NYTSearchResult result;
    @Override
    protected Void doInBackground(Void... params) {
        try {
            URL url = new URL("http://api.nytimes.com/svc/news/v3/content/nyt/all/.json?limit=5&api-key=fd0457bbde566c4783e7643346b77859:5:74605174");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inStream = connection.getInputStream();
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
            while ((data = reader.readLine()) != null) {
                builder.append(data);
            }
            reader.close();

            data = builder.toString();
            Log.i("DATA:", data);
            Gson gson = new Gson();
            result = gson.fromJson(data, NYTSearchResult.class);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        NewsWireDBHelper helper = new NewsWireDBHelper(null, null, null, 0);
        helper.insertBoth(result.getResults());
        return null;
    }
}
