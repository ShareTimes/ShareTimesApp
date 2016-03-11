package com.timesmunch.timesmunch;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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
                int idLocation = cursor.getInt(cursor.getColumnIndex(NewsWireDBHelper.COLUMN_FOLLOW));
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
            mCursorAdapter = new SimpleCursorAdapter(this, R.layout.custom_list_item, cursor, new String[]{NewsWireDBHelper.COLUMN_ARTICLE_TITLE}, new int[]{android.R.id.text1}, 0);
            ListView followListView = (ListView) findViewById(R.id.followingListView);

            followListView.setAdapter(mCursorAdapter);

        } else {
            mCursorAdapter.changeCursor(cursor);
        }
        mCursorAdapter.notifyDataSetChanged();
    }

}

