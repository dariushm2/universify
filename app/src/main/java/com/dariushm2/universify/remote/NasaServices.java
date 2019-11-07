package com.dariushm2.universify.remote;

import com.dariushm2.universify.model.PictureOfTheDay;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NasaServices {

    String API_KEY = "61oeOYHyVnw4ZYue06NKOooohsmaWQy4cmGsNrwi";
    String BASE_URL = "https://api.nasa.gov/";

    NasaServices REMOTE_API = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NasaServices.class);

    @GET("planetary/apod")
    Single<Response<PictureOfTheDay>> getPictureOfTheDay(@Query("api_key") String apiKey);



    @GET("planetary/apod")
    Single<Response<PictureOfTheDay>> getPictureOfTheDay(@Query("api_key") String apiKey, @Query("date") String date);
}
