package com.dariushm2.universify.repository;

import android.util.Log;

import com.dariushm2.universify.App;
import com.dariushm2.universify.model.backend.ImageSearch;
import com.dariushm2.universify.model.frontend.GalleryListModel;
import com.dariushm2.universify.model.frontend.GalleryModel;
import com.dariushm2.universify.remote.NasaServices;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class BackendToFrontendModelConverter {

    private List<GalleryModel> galleryModels = new ArrayList<>();
    private NasaServices nasaServices;

    private String query;
    private int pageNumber = 0;

    public BackendToFrontendModelConverter(NasaServices nasaServices, String query) {
        this.nasaServices = nasaServices;
        this.query = query;
    }

    protected void reset(String query) {
        this.query = query;
        pageNumber = 0;
        galleryModels.clear();
    }

    public Flowable<GalleryListModel> fetchImages() {
        pageNumber++;
        return nasaServices.getSearchPictures(query,
                NasaServices.MEDIA_TYPE_IMAGE,
                pageNumber)
                .flatMap((Function<ImageSearch, Publisher<GalleryListModel>>) imageSearch -> {

                    Log.e(App.TAG, "fetchImages " + imageSearch.getCollection().getImageItems().size());

                    for (int i = 0; i < imageSearch.getCollection().getImageItems().size(); i++) {
                        Log.e(App.TAG, "index " + i);
                        if (imageSearch.getCollection().getImageItems().get(i).getImageLinks() != null) {

                            String nasaId = imageSearch.getCollection()
                                    .getImageItems().get(i)
                                    .getImageDatas().get(0)
                                    .getNasaId();
                            Log.e(App.TAG, "converter " + nasaId);
                            String title = imageSearch.getCollection()
                                    .getImageItems().get(i)
                                    .getImageDatas().get(0)
                                    .getTitle();
                            Log.e(App.TAG, "converter " + title);
                            String description = imageSearch.getCollection()
                                    .getImageItems().get(i)
                                    .getImageDatas().get(0)
                                    .getDescription();
                            Log.e(App.TAG, "converter " + description);
                            String thumbnailUrl = imageSearch.getCollection()
                                    .getImageItems().get(i)
                                    .getImageLinks().get(0)
                                    .getUrl();
                            Log.e(App.TAG, "converter " + thumbnailUrl);
                            GalleryModel galleryModel = new GalleryModel(
                                    GalleryModel.MEDIA_TYPE_IMAGE,
                                    nasaId,
                                    title,
                                    description,
                                    thumbnailUrl,
                                    null
                            );
                            galleryModels.add(galleryModel);
                        }
                    }
                    Log.e(App.TAG, "converter done " + galleryModels.size());
                    return Flowable.just(GalleryListModel.success(galleryModels));
                })
                .subscribeOn(Schedulers.io());
    }


}
