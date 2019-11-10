package com.dariushm2.universify.remote;

import com.dariushm2.universify.model.ImageItems;
import com.dariushm2.universify.model.ImageLink;
import com.dariushm2.universify.model.ImageLinks;
import com.dariushm2.universify.model.PictureOfTheDay;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NasaServices {

    String API_KEY = "61oeOYHyVnw4ZYue06NKOooohsmaWQy4cmGsNrwi";
    String BASE_URL = "https://images-api.nasa.gov";


    @GET("/search")
    Single<Response<ImageItems>> getSearchPictures(@Query("q") String query);


    @GET("planetary/apod")
    Single<Response<PictureOfTheDay>> getPictureOfTheDay(@Query("api_key") String apiKey);


    @GET("planetary/apod")
    Single<Response<PictureOfTheDay>> getPictureOfTheDay(@Query("api_key") String apiKey, @Query("date") String date);
}
