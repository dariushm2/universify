package com.dariushm2.universify.view.pictureOfTheDay;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
import com.dariushm2.universify.R;
import com.github.chrisbanes.photoview.PhotoView;

public class PictureOfTheDayFragment extends Fragment {

    private PhotoView imageView;
    private ContentLoadingProgressBar progressBar;

    private String url;

    private PictureOfTheDayFragment() {
        this.url = url;
    }

    private boolean isFragmentVisible = false;

    public static PictureOfTheDayFragment newInstance(int position) {
        return new PictureOfTheDayFragment();
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
        View view = inflater.inflate(R.layout.fragment_picture_of_the_day, container, false);

        progressBar = view.findViewById(R.id.progressBar);
        imageView = view.findViewById(R.id.img);

        return view;
    }

    protected void showImage(String url) {
        Glide.with(getContext())
                .load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.hide();
                        return false;
                    }
                })
                .apply(RequestOptions.centerCropTransform())
                .into(imageView);

    }

    protected Bitmap getImage() {
        if (imageView.getDrawable() != null)
            return ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        else
            return null;
    }

}
