package com.dariushm2.universify.model.backend;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ImageItems {

    @SerializedName("links")
    private List<ImageLink> imageLinks;
    @SerializedName("data")
    private List<ImageData> imageDatas;

    public ImageItems(List<ImageLink> imageLinks, List<ImageData> imageDatas) {
        this.imageLinks = imageLinks;
        this.imageDatas = imageDatas;
    }

    public List<ImageLink> getImageLinks() {
        return imageLinks;
    }

    public void setImageLinks(List<ImageLink> imageLinks) {
        this.imageLinks = imageLinks;
    }

    public List<ImageData> getImageDatas() {
        return imageDatas;
    }

    public void setImageDatas(List<ImageData> imageDatas) {
        this.imageDatas = imageDatas;
    }
}
