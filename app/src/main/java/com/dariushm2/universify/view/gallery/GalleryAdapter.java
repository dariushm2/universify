package com.dariushm2.universify.view.gallery;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dariushm2.universify.R;
import com.dariushm2.universify.model.PictureOfTheDay;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private List<PictureOfTheDay> pictures;
    private Context context;


    public GalleryAdapter(List<PictureOfTheDay> pictures, Context context) {
        this.pictures = pictures;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        PictureOfTheDay picture = pictures.get(position);

        Glide.with(context)
                .load(picture.getUrl())
                .apply(RequestOptions.centerCropTransform())
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return pictures.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView imageView;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img);
        }


    }
}
