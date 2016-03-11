package com.timesmunch.timesmunch;

import java.util.ArrayList;

/**
 * Created by User_1_Benjamin_Rosenthal on 3/7/16.
 */
public class NYTSearchResult {
    private ArrayList<StoryItem> results;

    public NYTSearchResult(ArrayList<StoryItem> results) {
        this.results = results;
    }

    public ArrayList<StoryItem> getResults() {
        return results;
    }

    public void setResults(ArrayList<StoryItem> results) {
        this.results = results;
    }

}
