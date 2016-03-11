package com.timesmunch.timesmunch;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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

        mCategoriesListView.setAdapter(mCursorAdapter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // Do the actual database search

            String query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(SelectorActivity.this, "You searched for " + query, Toast.LENGTH_SHORT).show();

            Cursor newCursor = NewsWireDBHelper.getInstance(SelectorActivity.this).searchArticleList(query);
            mCursorAdapter.changeCursor(newCursor);

            mCursorAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        SearchableInfo info = searchManager.getSearchableInfo(getComponentName());

        searchView.setSearchableInfo(info);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Cursor textCursor = NewsWireDBHelper.getInstance(SelectorActivity.this).searchArticleList(query);
                Toast.makeText(SelectorActivity.this, "Results matching criteria: " + textCursor.getCount(), Toast.LENGTH_SHORT).show();
                mCursorAdapter.swapCursor(textCursor);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.buttonFollow) {
            Intent i = new Intent(SelectorActivity.this, Follow.class);
            startActivity(i);
        }
        return true;
    }

    // When you click back from the search it should go back to the article list screen
    @Override
    public void onBackPressed() {
        Cursor cursor = mHelper.getAllArticles();
        mCursorAdapter.swapCursor(cursor);
    }

    @Override
    protected void onResume() {

        if (mCursorAdapter == null) {
            mCategoriesListView.setAdapter(mCursorAdapter);
        } else {
        }

        super.onResume();
    }

}
