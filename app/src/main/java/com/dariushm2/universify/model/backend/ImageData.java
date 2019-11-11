package com.dariushm2.universify.model.backend;

import com.google.gson.annotations.SerializedName;

public class ImageData {
    @SerializedName("nasa_id")
    private String nasaId;
    @SerializedName("media_type")
    private String mediaType;
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;

    public ImageData(String nasaId, String mediaType, String title, String description) {
        this.nasaId = nasaId;
        this.mediaType = mediaType;
        this.title = title;
        this.description = description;
    }

    public String getNasaId() {
        return nasaId;
    }

    public void setNasaId(String nasaId) {
        this.nasaId = nasaId;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
