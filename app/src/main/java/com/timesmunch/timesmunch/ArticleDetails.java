package com.timesmunch.timesmunch;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.TextView;

public class ArticleDetails extends AppCompatActivity {

    NewsWireDBHelper mHelper;
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);

//        mHelper = new NewsWireDBHelper(ArticleDetails.this);
        Cursor cursor = mHelper.getAllArticles();

        WebView articleImage = (WebView) findViewById(R.id.articleImage);
        articleImage.getSettings().setLoadWithOverviewMode(true);
        articleImage.getSettings().setUseWideViewPort(true);

        TextView titleTextView = (TextView) findViewById(R.id.titleTextView);
        TextView bylineTextView = (TextView) findViewById(R.id.bylineTextView);

        WebView articleContentWebView = (WebView) findViewById(R.id.articleContentWebView);
        articleContentWebView.getSettings().setLoadWithOverviewMode(true);
        articleContentWebView.getSettings().setUseWideViewPort(true);


        final int id = getIntent().getIntExtra("_id", -1);

       // if (id > 0){

//            String articleTitle = NewsWireDBHelper.getInstance(ArticleDetails.this)
    //    }




    }
}
