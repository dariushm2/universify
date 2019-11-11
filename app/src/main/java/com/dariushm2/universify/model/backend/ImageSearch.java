package com.dariushm2.universify.model.backend;

import com.google.gson.annotations.SerializedName;

public class ImageSearch {
    @SerializedName("collection")
    private Collection collection;

    public ImageSearch(Collection collection) {
        this.collection = collection;
    }

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }
}
