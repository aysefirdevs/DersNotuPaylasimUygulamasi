package com.example.notuygulamam.Models;

import android.widget.RatingBar;

import com.google.firebase.database.Exclude;

public class Upload {
    private String mName;
    private String mImageUrl;
    private String mKey;
    private String mKonu;

    public Upload() {
        //empty constractor needed
    }

    public Upload(String name, String konu, String imageUrl) {
        if (name.trim().equals("") || konu.trim().equals("")) {
            name = "";
            konu = "";
        }
        mKonu = konu;
        mName = name;
        mImageUrl = imageUrl;


    }


    public String getKonu() {
        return mKonu;
    }

    public void setKonu(String konu) {
        mKonu = konu;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }


    @Exclude
    public String getKey() {
        return mKey;
    }

    @Exclude
    public void setKey(String key) {
        mKey = key;
    }

}
