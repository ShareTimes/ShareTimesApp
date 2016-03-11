package com.timesmunch.timesmunch;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2500;

    private TextView mTitle;
    private TextView mSubtitle;
    private String data;
    private NYTSearchResult result;
//    private Button mNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mTitle = (TextView) findViewById(R.id.title);
        mSubtitle = (TextView) findViewById(R.id.subtitle);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
//                startActivity(intent);
//
//                finish();
//            }
//        },SPLASH_TIME_OUT);


        GetNYTData getNYTData = new GetNYTData();
        getNYTData.execute();
        //////


//        for (int i = 0; i < 5; i++) {
//            String title = result.getResults().get(i).getTitle();
//            Log.d("SplashScreen.java", "THE TITLE OF THE " + (i + 1)
//                    + " ARTICLE IS: " + title);
//        }
//        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
//        startActivity(intent);
//        finish();
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



    //AsyncTask
    public class GetNYTData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL url = new URL("http://api.nytimes.com/svc/news/v3/content/nyt/all/.json?limit=20&api-key=fd0457bbde566c4783e7643346b77859:5:74605174");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inStream = connection.getInputStream();
                data = getInputData(inStream);
//                Log.i("DATA:", data);
                Gson gson = new Gson();
                result = gson.fromJson(data, NYTSearchResult.class);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            NewsWireDBHelper helper = new NewsWireDBHelper(getBaseContext(), null, null, 0);
            helper.insertBoth(result.getResults());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }
    }