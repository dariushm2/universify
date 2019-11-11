package com.dariushm2.universify.model.backend;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Collection {

    @SerializedName("items")
    private List<ImageItems> imageItems;

    public Collection(List<ImageItems> imageItems) {
        this.imageItems = imageItems;
    }

    public List<ImageItems> getImageItems() {
        return imageItems;
    }

    public void setImageItems(List<ImageItems> imageItems) {
        this.imageItems = imageItems;
    }
}
