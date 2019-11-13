package com.dariushm2.universify.repository;

import android.util.Log;

import com.dariushm2.universify.App;
import com.dariushm2.universify.model.backend.ImageUrls;
import com.dariushm2.universify.model.backend.ImageUrlsCollection;
import com.dariushm2.universify.model.frontend.GalleryListModel;
import com.dariushm2.universify.model.frontend.GalleryModel;
import com.dariushm2.universify.remote.NasaServices;
import com.dariushm2.universify.view.gallery.GalleryDataEvents;
import com.dariushm2.universify.view.image.ImageDataEvents;

import java.io.Serializable;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import retrofit2.Response;

public class GalleryPresenter {

    private static GalleryDataEvents galleryDataEvents;

    private static CompositeDisposable disposables;

    private static BackendToFrontendModelConverter converter;

    private static GalleryListModel mGalleryListModel;

    private static boolean isInitialized = false;

    public static void init(NasaServices nasaServices, GalleryDataEvents events, String query) {

        if (!isInitialized) {
            converter = new BackendToFrontendModelConverter(nasaServices, query);
            galleryDataEvents = events;
            disposables = new CompositeDisposable();
            getImages();
            isInitialized = true;
        }
        else
            if (mGalleryListModel != null) {
                Log.e(App.TAG, "GalleryPresenter is already initialized");
                galleryDataEvents.setUpAdapterAndView(mGalleryListModel);
            }

    }

    public static List<GalleryModel> getGalleryModels() {
        return mGalleryListModel.getGalleryModels();
    }

    public static GalleryModel getImageData(int position) {
        return mGalleryListModel.getGalleryModels().get(position);
    }

    public static void getUrlsFor(NasaServices nasaServices, int position, ImageDataEvents imageDataEvents) {
        if (mGalleryListModel.getGalleryModels().get(position).getOriginalUrl() != null) {
            imageDataEvents.showImage();
            return;
        }
        nasaServices.getUrlsForAsset(mGalleryListModel.getGalleryModels().get(position).getNasaId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<ImageUrlsCollection>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Response<ImageUrlsCollection> response) {
                        if (response.body() != null) {
                            //Log.e(App.TAG, "onSuccess: " + response.body().getImageUrls().getUrls().get(0).getUrl());
                            mGalleryListModel.getGalleryModels().get(position).setOriginalUrl(
                                    response.body().getImageUrls().getUrls().get(0).getUrl()
                            );
                            imageDataEvents.showImage();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(App.TAG, "onError " + e.getMessage());
                    }
                });
    }

    public static void next() {
        getImages();
    }

    public static void search(String query) {
        converter.reset(query);
        getImages();
    }

    public static void stop() {
        disposables.clear();
    }

    private static void getImages() {

        disposables.add(
                converter.fetchImages()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .startWith(GalleryListModel.loading())
                        .onErrorReturn(throwable ->
                                GalleryListModel.error(throwable.getMessage())
                        )
                        .subscribeWith(
                                new DisposableSubscriber<GalleryListModel>() {
                                    @Override
                                    public void onNext(GalleryListModel galleryListModel) {

                                        if (galleryListModel.hasError())
                                            galleryDataEvents.showErrorMessage(galleryListModel.getErrorMessage());
                                        else if (galleryListModel.isLoading())
                                            galleryDataEvents.showLoadingIndicator();
                                        else {
                                            galleryDataEvents.setUpAdapterAndView(galleryListModel);
                                            mGalleryListModel = galleryListModel;
                                        }

                                    }

                                    @Override
                                    public void onError(Throwable t) {

                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                }
                        )

        );
    }
}
