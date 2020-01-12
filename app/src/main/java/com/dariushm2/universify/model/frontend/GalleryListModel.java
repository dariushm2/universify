package com.dariushm2.universify.model.frontend;

import android.util.Log;

import com.dariushm2.universify.App;

import java.util.List;

public class GalleryListModel {

    private boolean hasError;
    private boolean isLoading;
    private String errorMessage;
    private List<GalleryModel> galleryModels;


    public GalleryListModel(boolean hasError, boolean isLoading, String errorMessage, List<GalleryModel> galleryModels) {
        this.hasError = hasError;
        this.isLoading = isLoading;
        this.errorMessage = errorMessage;
        this.galleryModels = galleryModels;
    }

    public List<GalleryModel> getGalleryModels() {
        return galleryModels;
    }

    public void setGalleryModels(List<GalleryModel> galleryModels) {
        this.galleryModels = galleryModels;
    }

    public boolean hasError() {
        return hasError;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public static GalleryListModel loading() {
        return new GalleryListModel(false, true, null, null);
    }

    public static GalleryListModel error(String errorMessage) {
        return new GalleryListModel(true, false, errorMessage, null);
    }

    public static GalleryListModel success(List<GalleryModel> galleryModels) {
        Log.e(App.TAG, "success " + galleryModels.size());
        return new GalleryListModel(false, false, null, galleryModels);
    }
}
