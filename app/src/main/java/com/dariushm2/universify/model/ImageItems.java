package com.dariushm2.universify.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ImageItems {
    @SerializedName("items")
    private List<ImageLinks> imageItem;

    public ImageItems(List<ImageLinks> imageItem) {
        this.imageItem = imageItem;
    }

    public List<ImageLinks> getImageItem() {
        return imageItem;
    }

    public void setImageItem(List<ImageLinks> imageItem) {
        this.imageItem = imageItem;
    }
}
