package com.timesmunch.timesmunch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Faraz on 3/4/16.
 */
public class SelectorArrayAdapter extends ArrayAdapter {
    private ArrayList<String> categories;

    public SelectorArrayAdapter(Context context, int layoutResourceId, ArrayList<String> categories) {
        super(context, layoutResourceId, categories);
        this.categories = categories;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        String currentCategory = categories.get(position);

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.custom_list_item, null);
        }

        if (currentCategory != null) {
            TextView categoryTextView = (TextView) v.findViewById(R.id.selectionTitle);
            if (categoryTextView != null) {
                categoryTextView.setText(currentCategory);
            }

            ImageView thumbnail = (ImageView) v.findViewById(R.id.imageView);
            Picasso.with(thumbnail.getContext()).load("").into(thumbnail);
        }
        return v;
    }
}