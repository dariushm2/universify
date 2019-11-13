package com.dariushm2.universify.model.backend;

import com.google.gson.annotations.SerializedName;

public class Url {

    @SerializedName("href")
    private String url;

    public Url(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
