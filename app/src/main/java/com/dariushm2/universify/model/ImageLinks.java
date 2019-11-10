package com.dariushm2.universify.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ImageLinks {

    @SerializedName("links")
    private List<ImageLink> imageLinks;

    public ImageLinks(List<ImageLink> imageLinks) {
        this.imageLinks = imageLinks;
    }

    public List<ImageLink> getImageLinks() {
        return imageLinks;
    }

    public void setImageLinks(List<ImageLink> imageLinks) {
        this.imageLinks = imageLinks;
    }
}
