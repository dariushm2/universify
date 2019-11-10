package com.dariushm2.universify;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.dariushm2.universify.remote.InternetConnectionListener;
import com.dariushm2.universify.remote.NasaServices;
import com.dariushm2.universify.remote.NetworkConnectionInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {


    public static final String TAG = "Universify: ";


    private NasaServices nasaServices;
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


    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null)
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public NasaServices getNasaServices() {
        if (nasaServices == null) {
            nasaServices = provideRetrofit().create(NasaServices.class);
        }
        return nasaServices;
    }

    private Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(NasaServices.BASE_URL)
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
                    mInternetConnectionListener.onInternetUnavailable();
                }
            }
        });

        return okHttpClientBuilder.build();
    }
}
