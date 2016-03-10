package com.timesmunch.timesmunch;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

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


public class ArticleDetails extends AppCompatActivity {

    NewsWireDBHelper mHelper;
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);

        mHelper = new NewsWireDBHelper(ArticleDetails.this, null, null, 0);

//        Cursor cursor = mHelper.getArticles();


//        WebView articleImage = (WebView) findViewById(R.id.articleImage);
//        articleImage.getSettings().setLoadWithOverviewMode(true);
//        articleImage.getSettings().setUseWideViewPort(true);

        TextView titleTextView = (TextView) findViewById(R.id.titleTextView);
        TextView bylineTextView = (TextView) findViewById(R.id.bylineTextView);

        WebView articleContentWebView = (WebView) findViewById(R.id.articleContentWebView);
        articleContentWebView.getSettings().setLoadWithOverviewMode(true);
        articleContentWebView.getSettings().setUseWideViewPort(true);


        final int id = getIntent().getIntExtra("_id", -1);
        Cursor cursor = mHelper.getAllArticles();
//        cursor.moveToNext();
        cursor.moveToPosition(id - 1);


        if (id > 0) {

//            String location_name = NeighbourSQLiteOpenHelper.getInstance(DescriptionActivity.this).getLocationName(id);

            String articleTitle = cursor.getString(cursor.getColumnIndex(NewsWireDBHelper.COLUMN_ARTICLE_TITLE));

            String bylineText = cursor.getString(cursor.getColumnIndex(NewsWireDBHelper.COLUMN_ARTICLE_BYLINE));

//            String image = cursor.getString(cursor.getColumnIndex(NewsWireDBHelper.COLUMN_IMAGE_URL));

            String imageText = cursor.getString(cursor.getColumnIndex(NewsWireDBHelper.COLUMN_URL));


//            articleImage.loadUrl(image);

            titleTextView.setText(articleTitle);

            bylineTextView.setText(bylineText);

            articleContentWebView.loadUrl(imageText);


        }


    }
}