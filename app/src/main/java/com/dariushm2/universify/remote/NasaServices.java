package com.dariushm2.universify.remote;

import com.dariushm2.universify.model.backend.ImageSearch;
import com.dariushm2.universify.model.PictureOfTheDay;

import io.reactivex.Flowable;
import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NasaServices {

    String BASE_URL_IMAGE_LIBRARY = "https://images-api.nasa.gov";
    NasaServices NASA_SERVICES = new Retrofit.Builder()
            .baseUrl(NasaServices.BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NasaServices.class);


    String API_KEY = "61oeOYHyVnw4ZYue06NKOooohsmaWQy4cmGsNrwi";
    String MEDIA_TYPE_IMAGE = "image";
    String BASE_URL = "https://api.nasa.gov/";


    @GET("/search")
    Single<Response<ImageSearch>> getSearchPictures(@Query("q") String query);

    @GET("/search")
    Flowable<ImageSearch> getSearchPictures(@Query("q") String query,
                                                      @Query("media_type") String mediaType,
                                                      @Query("page") int pageNumber);


    @GET("planetary/apod")
    Single<Response<PictureOfTheDay>> getPictureOfTheDay(@Query("api_key") String apiKey);


    @GET("planetary/apod")
    Single<Response<PictureOfTheDay>> getPictureOfTheDay(@Query("api_key") String apiKey, @Query("date") String date);
}
