package com.timesmunch.timesmunch;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class SelectorActivity extends AppCompatActivity {

    private ArrayList<String> categories = new ArrayList<>();
    private SelectorArrayAdapter mCategoriesArrayAdapter;
    private ListView mCategoriesListView;

    private CursorAdapter mCursorAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);

        mCategoriesListView = (ListView) findViewById(R.id.selectionList);
        mCategoriesArrayAdapter = new SelectorArrayAdapter(this, R.layout.activity_selector, categories);

        mCategoriesListView.setAdapter(mCategoriesArrayAdapter);


        mCursorAdapter = new CursorAdapter() {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                LayoutInflater
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {

            }
        }



    }
}
