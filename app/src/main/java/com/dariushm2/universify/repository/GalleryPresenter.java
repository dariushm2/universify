package com.dariushm2.universify.repository;

import com.dariushm2.universify.App;
import com.dariushm2.universify.model.frontend.GalleryListModel;
import com.dariushm2.universify.view.gallery.GalleryDataEvents;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class GalleryPresenter {

    private GalleryDataEvents galleryDataEvents;

    private CompositeDisposable disposables;

    private BackendToFrontendModelConverter converter;

    public GalleryPresenter(App app, GalleryDataEvents galleryDataEvents, String query) {

        converter = new BackendToFrontendModelConverter(app.getNasaServices(), query);
        this.galleryDataEvents = galleryDataEvents;
        disposables = new CompositeDisposable();

        getImages();
    }

    public void next() {
        getImages();
    }

    public void search(String query) {
        converter.reset(query);
        getImages();
    }

    public void stop() {
        disposables.clear();
    }

    private void getImages() {

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
                                        else
                                            galleryDataEvents.setUpAdapterAndView(galleryListModel);

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
