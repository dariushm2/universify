package com.dariushm2.universify.view.image;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.dariushm2.universify.App;
import com.dariushm2.universify.R;
import com.dariushm2.universify.repository.GalleryPresenter;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ImageFragment extends Fragment implements ImageDataEvents {


    private ImageDataEvents imageDataEvents;

    private int position;

    private PhotoView imageView;
    private ContentLoadingProgressBar progressBar;

    private boolean isBottomBarVisible = false;

    private ImageFragment(int position) {
        this.position = position;
    }

    private boolean isFragmentVisible = false;

    public static ImageFragment newInstance(int position) {
        return new ImageFragment(position);
    }

    @Override
    public void onPause() {
        super.onPause();
        isFragmentVisible = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        isFragmentVisible = true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);

        imageDataEvents = this;

        imageView = view.findViewById(R.id.img);

        progressBar = view.findViewById(R.id.progressBar);


        final LinearLayout bottomBar = view.findViewById(R.id.bottomBar);
        TextView btnSetAsWallpaper = view.findViewById(R.id.btnSetAsWallpaper);
        btnSetAsWallpaper.setOnClickListener(v -> {
            WallpaperManager myWallpaperManager
                    = WallpaperManager.getInstance(getContext());
            try {
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                myWallpaperManager.setBitmap(bitmap);
                Toast.makeText(getContext(), R.string.wallpaperChanged, Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                Toast.makeText(getContext(), R.string.failedToChangeWallpaper, Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        });

        imageView.setOnClickListener(v -> {
            bottomBar.setVisibility(isBottomBarVisible ? View.GONE : View.VISIBLE);
            isBottomBarVisible = !isBottomBarVisible;
        });


        if (getActivity() != null) {
            App app = (App) getActivity().getApplication();
            GalleryPresenter.getUrlsFor(app.getNasaServices(), position, imageDataEvents);
        }

        return view;
    }


    @Override
    public void showImage() {

        //Log.e(App.TAG, "showImage " + GalleryPresenter.getImageData(position).getOriginalUrl());

        if (getActivity() != null)
            Glide.with(getActivity())
                    .load(GalleryPresenter.getImageData(position).getOriginalUrl())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            if (e != null)
                                //Log.e(App.TAG, e.getMessage());

                                progressBar.hide();
                            if (isFragmentVisible) {
                                ///Toast.makeText(getContext(), R.string.unsupportedFormat, Toast.LENGTH_LONG).show();
                                Toast.makeText(getContext(), " " + position, Toast.LENGTH_SHORT).show();
                            }
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            //if (isFragmentVisible)
                                progressBar.hide();
                            return false;
                        }
                    })
                    .apply(RequestOptions.centerCropTransform())
                    .into(imageView);

    }


    private String getDate() {
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DATE, position > 0 ? position * -1 : position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        return dateFormat.format(calendar.getTime());
    }

}
