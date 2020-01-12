package com.dariushm2.universify;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;

import com.dariushm2.universify.remote.InternetConnectionListener;
import com.dariushm2.universify.remote.NasaServices;
import com.dariushm2.universify.remote.NetworkConnectionInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {


    public static final String TAG = "Universify";


    private NasaServices nasaServicesForImageLibrary;
    private NasaServices nasaServicesForPictureOfTheDay;
    private InternetConnectionListener mInternetConnectionListener;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void setInternetConnectionListener(InternetConnectionListener listener) {
        mInternetConnectionListener = listener;
    }

    public void removeInternetConnectionListener() {
        mInternetConnectionListener = null;
    }


    public boolean isInternetAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null)
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public NasaServices getRetrofitFor(String baseUrl) {
        if (baseUrl.equals(NasaServices.BASE_URL_IMAGE_LIBRARY)) {
            if (nasaServicesForImageLibrary == null) {
                nasaServicesForImageLibrary = provideRetrofitFor(baseUrl).create(NasaServices.class);
            }
            return nasaServicesForImageLibrary;
        }
        if (nasaServicesForPictureOfTheDay == null) {
            nasaServicesForPictureOfTheDay = provideRetrofitFor(baseUrl).create(NasaServices.class);
        }
        return nasaServicesForPictureOfTheDay;
    }

    private Retrofit provideRetrofitFor(String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(provideOkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    private OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
//        okhttpClientBuilder.connectTimeout(30, TimeUnit.SECONDS);
//        okhttpClientBuilder.readTimeout(30, TimeUnit.SECONDS);
//        okhttpClientBuilder.writeTimeout(30, TimeUnit.SECONDS);

        okHttpClientBuilder.addInterceptor(new NetworkConnectionInterceptor() {
            @Override
            public boolean isInternetAvailable() {
                return App.this.isInternetAvailable();
            }

            @Override
            public void onInternetUnavailable() {

                if (mInternetConnectionListener != null) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        mInternetConnectionListener.onInternetUnavailable();
                    });
                }
            }
        });

        return okHttpClientBuilder.build();
    }
}
