package com.dariushm2.universify.model.backend;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ImageUrls {

    @SerializedName("items")
    private List<Url> urls;

    public ImageUrls(List<Url> urls) {
        this.urls = urls;
    }

    public List<Url> getUrls() {
        return urls;
    }

    public void setUrls(List<Url> urls) {
        this.urls = urls;
    }
}
