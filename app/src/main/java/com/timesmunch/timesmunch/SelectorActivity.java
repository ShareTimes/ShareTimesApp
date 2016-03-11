package com.timesmunch.timesmunch;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class SelectorActivity extends AppCompatActivity {

    private ArrayList<String> categories = new ArrayList<>();
    private SelectorArrayAdapter mCategoriesArrayAdapter;
    private ListView mCategoriesListView;
    public SharedPreferences mSharedPrefs;

    private TextView selectionTitle;

    private CursorAdapter mCursorAdapter;


    private NewsWireDBHelper mHelper;

    ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);

        mCategoriesListView = (ListView) findViewById(R.id.selectionList);

        mHelper = new NewsWireDBHelper(SelectorActivity.this, null, null, 0);
        final Cursor cursor = mHelper.getAllArticles();

        GetNYTSectionData getSectionData = new GetNYTSectionData();
        getSectionData.execute();

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




        mCategoriesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SelectorActivity.this);

                LayoutInflater inflaterDialog = getLayoutInflater();

                View viewDialog = inflaterDialog.inflate(R.layout.force_touch, null);

                TextView titleDialog = (TextView) viewDialog.findViewById(R.id.dialogTitle);

                TextView textDialog = (TextView) viewDialog.findViewById(R.id.dialogText);


                    String dialogTitle = cursor.getColumnName(cursor.getColumnIndex(NewsWireDBHelper.COLUMN_ARTICLE_TITLE));

                    String dialogText = cursor.getColumnName(cursor.getColumnIndex(NewsWireDBHelper.COLUMN_ARTICLE_BYLINE));


                titleDialog.setText(dialogTitle);
                textDialog.setText(dialogText);

                dialogBuilder.setView(viewDialog);


                AlertDialog dialog = dialogBuilder.create();
                dialog.show();
                return false;
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

//        mCategoriesListView.setAdapter(mCursorAdapter);
//        mCursorAdapter.notifyDataSetChanged();
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

    public String getInputData(InputStream inStream) throws IOException {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));

        String data = null;

        while ((data = reader.readLine()) != null) {
            builder.append(data);
        }

        reader.close();

        return builder.toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.d(SelectorActivity.class.getName(),"Button clicked");
        if (id == R.id.buttonFollowList) {
            Intent i = new Intent(SelectorActivity.this, Follow.class);
            startActivity(i);
        }
        return true;
    }

    public class GetNYTSectionData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<String> sectionsUrls = apiCallBySection();
            String data = "";
            NYTSearchResult result;
            try {
                for (int i = 0; i < sectionsUrls.size(); i++) {
                    URL url = new URL(sectionsUrls.get(i));
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    InputStream inStream = connection.getInputStream();
                    data = getInputData(inStream);
                    Gson gson = new Gson();
                    result = gson.fromJson(data, NYTSearchResult.class);
                    NewsWireDBHelper helper = new NewsWireDBHelper(getBaseContext(), null, null, 0);
                    helper.insertBoth(result.getResults());
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }


            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mCursorAdapter.notifyDataSetChanged();

        }
    }

    public ArrayList<String> apiCallBySection() {
        mSharedPrefs= getSharedPreferences("com.timesmunch.timesmunch.SECTION_PREFS", Context.MODE_PRIVATE);
        Map<String,?> keys = mSharedPrefs.getAll();
        ArrayList<String> sectionURLs = new ArrayList<>();
        for(Map.Entry<String, ?> entry : keys.entrySet()) {
            String sectionURL = "http://api.nytimes.com/svc/news/v3/content/all/"
                    +entry
                    +"/.json?api-key=61bce87ec83f2b84a3c3214d1d812e3b:19:74605174";
            sectionURLs.add(sectionURL);
        }
        return sectionURLs;
    }

    // When you click back from the search it should go back to the article list screen
    @Override
    public void onBackPressed() {

//        Cursor cursor = mHelper.getAllArticles();
//        mCursorAdapter.swapCursor(cursor);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (mCursorAdapter == null) {
//            mCategoriesListView.setAdapter(mCursorAdapter);

        Cursor cursorResume = mHelper.getAllArticles();
        mCursorAdapter.swapCursor(cursorResume);

        mCategoriesListView.setAdapter(mCursorAdapter);
        mCursorAdapter.notifyDataSetChanged();


//        }

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        AppEventsLogger.deactivateApp(this);
    }

}
//Cursor cursor = mHelper.getFavoritesList();
//mSwipeAdapter = new SwipeAdapter(SearchActivity.this, cursor);
//        mFavoritesListView.setAdapter(mSwipeAdapter);
//        mSwipeAdapter.swapCursor(cursor);
//
