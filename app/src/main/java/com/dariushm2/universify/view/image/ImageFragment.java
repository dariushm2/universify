package com.dariushm2.universify.view.image;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dariushm2.universify.App;
import com.dariushm2.universify.R;
import com.dariushm2.universify.model.PictureOfTheDay;
import com.dariushm2.universify.remote.NasaServices;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ImageFragment extends Fragment {

    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;

    private int position;

    private ImageView imageView;
    private boolean isBottomBarVisible = false;

    private ImageFragment(int position) {
        this.position = position;
    }

    public static ImageFragment newInstance(int position) {
        return new ImageFragment(position);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);

        imageView = view.findViewById(R.id.img);
        final LinearLayout bottomBar = view.findViewById(R.id.bottomBar);
        TextView btnSetAsWallpaper = view.findViewById(R.id.btnSetAsWallpaper);
        btnSetAsWallpaper.setOnClickListener(v -> {
            WallpaperManager myWallpaperManager
                    = WallpaperManager.getInstance(getContext());
            try {
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                myWallpaperManager.setBitmap(bitmap);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });

        imageView.setOnTouchListener((v, motionEvent) -> {

            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    return true;
                case MotionEvent.ACTION_UP:
                    imageView.performClick();
                    return true;
                default:
                    mScaleGestureDetector.onTouchEvent(motionEvent);
            }

            return true;

        });
        imageView.setOnClickListener(v -> {
            bottomBar.setVisibility(isBottomBarVisible ? View.GONE : View.VISIBLE);
            isBottomBarVisible = !isBottomBarVisible;
        });
        mScaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
        getImage();

        return view;
    }


    private void getImage() {
        NasaServices.NASA_SERVICES
                .getPictureOfTheDay(NasaServices.API_KEY,
                        getDate())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<PictureOfTheDay>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Response<PictureOfTheDay> response) {

                        if (response.body() != null) {
                            Log.e(App.TAG, "onSuccess: ");
                            if (getActivity() != null)
                                Glide.with(getActivity())
                                        .load(response.body().getUrl())
                                        .apply(RequestOptions.centerCropTransform())
                                        .into(imageView);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });

    }


    private String getDate() {
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DATE, position > 0 ? position * -1 : position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        return dateFormat.format(calendar.getTime());
    }


    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(1.0f,
                    Math.min(mScaleFactor, 10.0f));
            imageView.setScaleX(mScaleFactor);
            imageView.setScaleY(mScaleFactor);
            return true;
        }
    }
}
