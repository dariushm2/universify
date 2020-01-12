package com.dariushm2.universify.remote;

import com.dariushm2.universify.model.backend.ImageSearch;
import com.dariushm2.universify.model.PictureOfTheDay;
import com.dariushm2.universify.model.backend.ImageUrls;
import com.dariushm2.universify.model.backend.ImageUrlsCollection;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NasaServices {

    String BASE_URL_PICTURE_OF_THE_DAY = "https://api.nasa.gov/";
    String BASE_URL_IMAGE_LIBRARY = "https://images-api.nasa.gov";


    String API_KEY = "61oeOYHyVnw4ZYue06NKOooohsmaWQy4cmGsNrwi";
    String MEDIA_TYPE_IMAGE = "image";


    @GET("/search")
    Single<Response<ImageSearch>> getSearchPictures(@Query("q") String query);

    @GET("/search")
    Flowable<ImageSearch> getSearchPictures(@Query("q") String query,
                                                      @Query("media_type") String mediaType,
                                                      @Query("page") int pageNumber);

    @GET("/asset/{nasa_id}")
    Single<Response<ImageUrlsCollection>> getUrlsForAsset(@Path("nasa_id") String nasaId);


    @GET("planetary/apod")
    Single<Response<PictureOfTheDay>> getPictureOfTheDay(@Query("api_key") String apiKey);


    @GET("planetary/apod")
    Single<Response<PictureOfTheDay>> getPictureOfTheDay(@Query("api_key") String apiKey, @Query("date") String date);
}
