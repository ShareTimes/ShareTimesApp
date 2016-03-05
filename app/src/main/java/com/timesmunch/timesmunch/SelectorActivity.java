package com.timesmunch.timesmunch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

public class SelectorActivity extends AppCompatActivity {

    private ArrayList<String> categories = new ArrayList<>();
    private SelectorArrayAdapter mCategoriesArrayAdapter;
    private ListView mCategoriesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);

        categories.add("Politics");
        categories.add("Sports");
        categories.add("Economics");

        mCategoriesListView = (ListView) findViewById(R.id.selectionList);
        mCategoriesArrayAdapter = new SelectorArrayAdapter(this, R.layout.activity_selector, categories);



    }
}
