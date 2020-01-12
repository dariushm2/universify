package com.dariushm2.universify.repository;

import android.util.Log;

import com.dariushm2.universify.App;
import com.dariushm2.universify.model.backend.ImageUrlsCollection;
import com.dariushm2.universify.model.frontend.GalleryListModel;
import com.dariushm2.universify.model.frontend.GalleryModel;
import com.dariushm2.universify.remote.NasaServices;
import com.dariushm2.universify.view.gallery.GalleryDataEvents;
import com.dariushm2.universify.view.image.ImageDataEvents;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subscribers.DisposableSubscriber;
import retrofit2.Response;

public final class GalleryPresenter {

    private GalleryDataEvents galleryDataEvents;

    private CompositeDisposable disposables;

    @Inject
    ModelConverter converter;

    private GalleryListModel mGalleryListModel;

    private NasaServices nasaServices;

    private BaseSchedulersProvider schedulersProvider;

    private static GalleryPresenter galleryPresenter = null;

    private GalleryPresenter(NasaServices nasaServices, GalleryDataEvents events, ModelConverter converter, BaseSchedulersProvider schedulersProvider) {
        this.nasaServices = nasaServices;
        this.converter = converter;
        this.galleryDataEvents = events;
        this.schedulersProvider = schedulersProvider;
        disposables = new CompositeDisposable();
    }

    public static void init(NasaServices nasaServices, GalleryDataEvents events, ModelConverter converter, BaseSchedulersProvider schedulersProvider) {
        if (galleryPresenter == null) {
            galleryPresenter = new GalleryPresenter(nasaServices, events, converter, schedulersProvider);
        }
    }

    public static GalleryPresenter getInstance() {
        return galleryPresenter;
    }

    public void setSearchQuery(String query) {
        converter.reset(query);
    }


    public void stop() {
        disposables.clear();
        galleryPresenter = null;
        mGalleryListModel.getGalleryModels().clear();
    }

    public List<GalleryModel> getGalleryModels() {
        if (mGalleryListModel != null)
            return mGalleryListModel.getGalleryModels();
        return null;
    }

    public GalleryModel getImageData(int position) {
        if (mGalleryListModel != null)
            return mGalleryListModel.getGalleryModels().get(position);
        return null;
    }

    public void getUrlsFor(int position, ImageDataEvents imageDataEvents) {
        if (mGalleryListModel.getGalleryModels().size() > position)
            if (mGalleryListModel.getGalleryModels().get(position).getOriginalUrl() != null) {
                imageDataEvents.showImage();
                return;
            }
        if (mGalleryListModel.getGalleryModels().size() > position)
            nasaServices.getUrlsForAsset(mGalleryListModel.getGalleryModels().get(position).getNasaId())
                    .subscribeOn(schedulersProvider.io())
                    .observeOn(schedulersProvider.ui())
                    .subscribe(new SingleObserver<Response<ImageUrlsCollection>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(Response<ImageUrlsCollection> response) {
                            if (response.body() != null) {
                                mGalleryListModel.getGalleryModels().get(position).setOriginalUrl(
                                        response.body().getImageUrls().getUrls().get(1).getUrl()
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


    public void getNextImages() {
        disposables.add(
                converter.fetchImages()
                        .subscribeOn(schedulersProvider.io())
                        .observeOn(schedulersProvider.ui())
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
