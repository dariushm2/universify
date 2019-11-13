package com.dariushm2.universify.model.backend;

import com.google.gson.annotations.SerializedName;

public class ImageUrlsCollection {
    @SerializedName("collection")
    private ImageUrls imageUrls;

    public ImageUrlsCollection(ImageUrls imageUrls) {
        this.imageUrls = imageUrls;
    }

    public ImageUrls getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(ImageUrls imageUrls) {
        this.imageUrls = imageUrls;
    }
}
