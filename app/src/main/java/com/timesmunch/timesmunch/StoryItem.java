package com.timesmunch.timesmunch;

import com.google.gson.annotations.SerializedName;

/**
 * Created by User_1_Benjamin_Rosenthal on 3/7/16.
 */
public class StoryItem {

    private String title;
    private String byline;
    private String published_date;
    @SerializedName("abstract")
    private String mAbstract;

    @SerializedName("thumbnail_standard")
    private String mPhotoUrl;
    private String section;
    private String url;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getByline() {
        return byline;
    }

    public void setByline(String byline) {
        this.byline = byline;
    }

    public String getAbstract() {
        return mAbstract;
    }

    public void setAbstract(String mAbstract) {
        this.mAbstract = mAbstract;
    }

    public String getPublished_date() {
        return published_date;
    }

    public void setPublished_date(String published_date) {
        this.published_date = published_date;
    }

    public String getPhotoUrl() {
        return mPhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.mPhotoUrl = mPhotoUrl;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
