package com.timesmunch.timesmunch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

public class SelectorActivity extends AppCompatActivity {

    private ArrayList<String> categories = new ArrayList<>();
    private SelectorArrayAdapter mCategoriesArrayAdapter;
    private ListView mCategoriesListView;

    private TextView selectionTitle;

    private CursorAdapter mCursorAdapter;

    private NewsWireDBHelper mHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);

        SharedPreferences sharedPref = getSharedPreferences("com.timesmunch.timesmunch.SECTION_PREFS", Context.MODE_PRIVATE);

        Map<String,?> keys = sharedPref.getAll();

        Log.d("PREFS_LEN", "len of sharedpref:" + keys.size());

        for(Map.Entry<String,?> entry : keys.entrySet()) {
            Log.d("PREFS", entry.getKey() + ": " +
                    entry.getValue().toString());
        }



        mCategoriesListView = (ListView) findViewById(R.id.selectionList);

        NewsWireDBHelper newsWireDBHelper = new NewsWireDBHelper(this, null, null, 0);
        final Cursor cursor = newsWireDBHelper.getAllArticles();


        //Item Click listener passing the cursor (id) to the Article Details.
        mCategoriesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SelectorActivity.this, ArticleDetails.class);
                Cursor cursor1 = mCursorAdapter.getCursor();

                cursor1.moveToPosition(position);
                int cursorPositionId = cursor1.getInt(cursor1.getColumnIndex(NewsWireDBHelper.COLUMN_ID));
                intent.putExtra("_id", cursorPositionId);

                startActivity(intent);
            }
        });


        //Cursor Adapter to inflate the ListView.
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
                String url = cursor.getString(cursor.getColumnIndex(NewsWireDBHelper.COLUMN_IMAGE_URL));
                // Line above is grabbing the URL from the database.
                Log.i("[URL]", url);
                selectionTitle.setText(title);

                if(!url.equals("")){
                    Picasso.with(context).load(url).into(imageView);
                }
                else {

                    imageView.setImageResource(R.drawable.munchthumbnail);
                }

                /**
                 * There are many things you can do like .centerCrop.into(imageView); or something
                 * like .centerInside().into(imageView); experiment and see which looks best.
                 */
            }
        };

        mCategoriesListView.setAdapter(mCursorAdapter);

    }

    @Override
    protected void onResume() {

        if (mCursorAdapter == null){
            mCategoriesListView.setAdapter(mCursorAdapter);
        }
        else {
        }

        super.onResume();
    }
}
