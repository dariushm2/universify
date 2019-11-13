package com.dariushm2.universify.model.frontend;


public class GalleryModel {

    public static final String MEDIA_TYPE_IMAGE = "image";

    private String mediaType;
    private String nasaId;
    private String title;
    private String description;
    private String thumbnailUrl;
    private String originalUrl;

    public GalleryModel(String mediaType, String nasaId, String title, String description, String thumbnailUrl, String originalUrl) {
        this.mediaType = mediaType;
        this.nasaId = nasaId;
        this.title = title;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.originalUrl = originalUrl;
    }

    public String getNasaId() {
        return nasaId;
    }

    public void setNasaId(String nasaId) {
        this.nasaId = nasaId;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
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

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }
}
