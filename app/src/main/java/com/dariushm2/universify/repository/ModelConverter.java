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

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class ModelConverter {

    private List<GalleryModel> galleryModels = new ArrayList<>();
    private NasaServices nasaServices;

    private String query;
    private int pageNumber = 0;

    @Inject
    public ModelConverter(NasaServices nasaServices) {
        this.nasaServices = nasaServices;
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
                .parallel()
                .map(imageSearch -> {

                    Log.e(App.TAG, "fetchImages " + imageSearch.getCollection().getImageItems().size());

                    for (int i = 0; i < imageSearch.getCollection().getImageItems().size(); i++) {

                        if (imageSearch.getCollection().getImageItems().get(i).getImageLinks() != null) {

                            String nasaId = imageSearch.getCollection()
                                    .getImageItems().get(i)
                                    .getImageDatas().get(0)
                                    .getNasaId();

                            String title = imageSearch.getCollection()
                                    .getImageItems().get(i)
                                    .getImageDatas().get(0)
                                    .getTitle();

                            String description = imageSearch.getCollection()
                                    .getImageItems().get(i)
                                    .getImageDatas().get(0)
                                    .getDescription();

                            String thumbnailUrl = imageSearch.getCollection()
                                    .getImageItems().get(i)
                                    .getImageLinks().get(0)
                                    .getUrl();

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
                    return GalleryListModel.success(galleryModels);
                })
                .sequential();
    }


}
