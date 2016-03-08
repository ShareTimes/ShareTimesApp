package com.timesmunch.timesmunch;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SelectorActivity extends AppCompatActivity {

    private ArrayList<String> categories = new ArrayList<>();
    private SelectorArrayAdapter mCategoriesArrayAdapter;
    private ListView mCategoriesListView;

    private TextView selectionTitle;

    private CursorAdapter mCursorAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);

        mCategoriesListView = (ListView) findViewById(R.id.selectionList);

        NewsWireDBHelper newsWireDBHelper = new NewsWireDBHelper(this, null, null, 0);
        Cursor cursor = newsWireDBHelper.getAllArticles();

        mCursorAdapter = new CursorAdapter(this, cursor, 0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return LayoutInflater.from(context).inflate(R.layout.custom_list_item,parent,false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView selectionTitle = (TextView) view.findViewById(R.id.selectionTitle);
                ImageView imageView = (ImageView) view.findViewById(R.id.imageView);

                String title = cursor.getString(cursor.getColumnIndex(NewsWireDBHelper.COLUMN_ARTICLE_TITLE));
                selectionTitle.setText(title);
            }
        };

        mCategoriesListView.setAdapter(mCursorAdapter);

    }
}
