package com.timesmunch.timesmunch;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by Faraz on 3/4/16.
 */
public class SelectorArrayAdapter extends ArrayAdapter {
    public SelectorArrayAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
    }
}
