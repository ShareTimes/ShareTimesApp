package com.timesmunch.timesmunch;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by User_1_Benjamin_Rosenthal on 3/11/16.
 */
public class SectionArrayAdapter extends ArrayAdapter {

    private ArrayList<String> categories;
    private SharedPreferences mSharedPrefs;


    public SectionArrayAdapter(Context context, int layoutResourceId, ArrayList<String> categories) {
        super(context, layoutResourceId, categories);
        this.categories = categories;
    }

    private class ViewHolder {
        TextView sectionTextview;
        CheckBox sectionCheckbox;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        mSharedPrefs = getContext().getSharedPreferences("com.timesmunch.timesmunch.SECTION_PREFS",Context.MODE_PRIVATE);
        View v = convertView;
        String currentCategory = categories.get(position);


        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.section_list_item, null);
        }

        if (currentCategory != null) {
            TextView sectionTextView = (TextView) v.findViewById(R.id.sectionTextview);
            if (sectionTextView != null) {
                sectionTextView.setText(currentCategory);
            }
        }

        holder = new ViewHolder();

        holder.sectionTextview = (TextView)v.findViewById(R.id.sectionTextview);
        holder.sectionCheckbox = (CheckBox)v.findViewById(R.id.sectionCheckbox);
        v.setTag(holder);



        final ViewHolder finalHolder = holder;
        holder.sectionCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = mSharedPrefs.edit();
                Set<String> mySet = new HashSet<String>();
                mySet = mSharedPrefs.getStringSet("PREFS_SET", new HashSet<String>());
                if (isChecked) {
                    mySet.add(finalHolder.sectionTextview.getText().toString());
                } else {
                    mySet.remove(finalHolder.sectionTextview.getText().toString());
                }
                mSharedPrefs.edit().putStringSet("PREFS_SET", mySet).apply();
            }
        });

        return v;
    }



}
