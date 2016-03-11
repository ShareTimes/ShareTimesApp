package com.timesmunch.timesmunch;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Follow extends AppCompatActivity {
    private CursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);

        ListView followListView = (ListView) findViewById(R.id.followingListView);

        NewsWireDBHelper helper = NewsWireDBHelper.getInstance(Follow.this);

        final int id = getIntent().getIntExtra("_id", -1);
        followListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = mCursorAdapter.getCursor();
                Intent intent = new Intent(Follow.this, ArticleDetails.class);

                cursor.moveToPosition(position);
                int idLocation = cursor.getInt(cursor.getColumnIndex(NewsWireDBHelper.COLUMN_ID));
                intent.putExtra("_id", idLocation);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Cursor cursor = NewsWireDBHelper.getInstance(Follow.this).checkFollow();

        if (mCursorAdapter == null) {
            mCursorAdapter = new CursorAdapter(this, cursor, 0) {
                @Override
                public View newView(Context context, Cursor cursor, ViewGroup parent) {
                    return LayoutInflater.from(context).inflate(R.layout.custom_list_item, parent, false);
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

                    if (!url.equals("")) {
                        Picasso.with(context).load(url).into(imageView);
                    } else {

                        imageView.setImageResource(R.drawable.munchthumbnail);
                    }

                    /**
                     * There are many things you can do like .centerCrop.into(imageView); or something
                     * like .centerInside().into(imageView); experiment and see which looks best.
                     */
                }
            };

            ListView followListView = (ListView) findViewById(R.id.followingListView);

            followListView.setAdapter(mCursorAdapter);

        } else {
            mCursorAdapter.changeCursor(cursor);
        }
        mCursorAdapter.notifyDataSetChanged();
    }

}

