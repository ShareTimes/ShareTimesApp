package com.timesmunch.timesmunch;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.graphics.BitmapFactory;
import android.graphics.Color;

import android.net.Uri;

import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import android.support.design.widget.FloatingActionButton;

import android.widget.VideoView;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;



public class ArticleDetails extends AppCompatActivity {

    NewsWireDBHelper mHelper;
    SQLiteDatabase db;
    ProgressBar mProgressBar;
    private boolean mFollowFlag;
    String articleTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);
        FacebookSdk.sdkInitialize(getApplicationContext());


        mHelper = new NewsWireDBHelper(ArticleDetails.this, null, null, 0);
        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);


        TextView titleTextView = (TextView) findViewById(R.id.titleTextView);
        TextView bylineTextView = (TextView) findViewById(R.id.bylineTextView);




        final int id = getIntent().getIntExtra("_id", -1);
        Cursor cursor = mHelper.getAllArticles();
        cursor.moveToPosition(id - 1);

        HTMLAsyncTask htmlAsyncTask = new HTMLAsyncTask();
        htmlAsyncTask.execute(cursor.getString(cursor.getColumnIndex(NewsWireDBHelper.COLUMN_URL)));


        if (id > 0) {


            articleTitle = cursor.getString(cursor.getColumnIndex(NewsWireDBHelper.COLUMN_ARTICLE_TITLE));

            String bylineText = cursor.getString(cursor.getColumnIndex(NewsWireDBHelper.COLUMN_ARTICLE_BYLINE));


            titleTextView.setText(articleTitle);
            titleTextView.setTextColor(Color.parseColor("#ffffff"));

            bylineTextView.setText(bylineText);
            bylineTextView.setTextColor(Color.parseColor("#ffffff"));



            //Facebook Share Button. When Clicked it prompts you to login before sharing.
            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse(cursor.getString(cursor.getColumnIndex(NewsWireDBHelper.COLUMN_URL))))
                    .build();

            ShareButton facebookShare = (ShareButton)findViewById(R.id.faceBookShareButton);
            facebookShare.setShareContent(content);



        }



        final FloatingActionButton buttonFollow = (FloatingActionButton) findViewById(R.id.buttonFollow);

        if (mHelper.checkIfArticleFollow(id)) {
            mFollowFlag = true;
            buttonFollow.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff5252")));
        } else {
            mFollowFlag = false;
            buttonFollow.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#616161")));
        }

        buttonFollow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (mFollowFlag == true) {
                    // If the button is already a favorite
//                    SQLiteDatabase db = mHelper.getWritableDatabase();
//                    db.execSQL("UPDATE ArticleDetails SET Follow = 0 WHERE _id = " + id);
                    mHelper.setUnFollow(id);
                    Toast.makeText(ArticleDetails.this, "Article is not being followed", Toast.LENGTH_SHORT).show();
                    buttonFollow.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#616161")));

                    mFollowFlag = false;
                } else { // If the button is not already a favorite
//                    SQLiteDatabase db = mHelper.getWritableDatabase();
//                    db.execSQL("UPDATE ArticleDetails SET Follow = 1 WHERE _id = " + id);
                    mHelper.setFollow(id);
                    Toast.makeText(ArticleDetails.this, "Article is being followed", Toast.LENGTH_SHORT).show();
                    buttonFollow.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF5252")));

                    newsNotification();
                    mFollowFlag = true;
                }

            }
        });
    }

    public void newsNotification(){
        Intent intent = new Intent(this,SelectorActivity.class);

        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(),intent,0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(android.R.drawable.ic_dialog_alert);
        mBuilder.setContentTitle("Yay! You favorited a Story!");
        mBuilder.setContentText("#followMunch");
        mBuilder.setContentIntent(pIntent);
        mBuilder.setAutoCancel(true);
        mBuilder.setPriority(Notification.PRIORITY_MAX);

        mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.munchlogosmall)).build();

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());

    }

    public class HTMLAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);


        }

        @Override
        protected String doInBackground(String... urls) {

            String theUrl = urls[0];
            StringBuilder stringBuilder = new StringBuilder();
            try {
                Document document = Jsoup.connect(theUrl).get();
                Elements bodyText = document.getElementsByAttributeValueContaining("class", "p-block");

                for (Element section : bodyText) {
                    stringBuilder.append(section.text());
                    stringBuilder.append("\n");
                    stringBuilder.append("\n");
                }



            } catch (IOException e) {
                e.printStackTrace();
            }


            return stringBuilder.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (mProgressBar.getVisibility() == View.VISIBLE) {
                mProgressBar.setVisibility(View.GONE);
            }

            TextView contentText = (TextView) findViewById(R.id.contextText);
            contentText.setText(s);

        }

    }
}