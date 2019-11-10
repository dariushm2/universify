package com.dariushm2.universify.repository;

import com.dariushm2.universify.model.PictureOfTheDay;
import com.dariushm2.universify.remote.NasaServices;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

public class PicturesRepository {

    private Observable<List<PictureOfTheDay>> pictures;

    public PicturesRepository() {
        fetchPictures();
    }

    private void fetchPictures() {

    }


    public Observable<List<PictureOfTheDay>> getPictures() {

//        Flowable<PictureOfTheDay> pictureOfTheDayFlowable = NasaServices.REMOTE_API
//                .getPictureOfTheDay(NasaServices.API_KEY)
//                .observeOn(Schedulers.io())
//                .
        return pictures;
    }
}
