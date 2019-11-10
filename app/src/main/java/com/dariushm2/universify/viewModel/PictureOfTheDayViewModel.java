package com.dariushm2.universify.viewModel;

import androidx.lifecycle.ViewModel;

import com.dariushm2.universify.model.PictureOfTheDay;
import com.dariushm2.universify.remote.NasaServices;

import io.reactivex.Single;
import retrofit2.Response;

public class PictureOfTheDayViewModel extends ViewModel {

    //public Single<Response<PictureOfTheDay>> pictureOfTheDay = NasaServices.REMOTE_API.getPictureOfTheDay(NasaServices.API_KEY);

}
