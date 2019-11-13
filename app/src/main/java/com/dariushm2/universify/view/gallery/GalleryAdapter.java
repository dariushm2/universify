package com.dariushm2.universify.view.gallery;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dariushm2.universify.R;
import com.dariushm2.universify.model.backend.ImageLink;
import com.dariushm2.universify.model.frontend.GalleryModel;
import com.dariushm2.universify.view.image.ImageActivity;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private List<GalleryModel> galleryModels;
    private GalleryFragment galleryFragment;




    public GalleryAdapter(List<GalleryModel> galleryModels, GalleryFragment galleryFragment) {
        this.galleryModels = galleryModels;
        this.galleryFragment = galleryFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View galleryItem = inflater.inflate(R.layout.gallery_item, parent, false);

        return new ViewHolder(galleryItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        GalleryModel galleryModel = galleryModels.get(position);

        if (galleryFragment.getContext() != null)
            Glide.with(galleryFragment.getContext())
                .load(galleryModel.getThumbnailUrl())
                .override(200, 200)
                .apply(RequestOptions.centerCropTransform())
                .into(holder.imageView);

        holder.imageView.setOnClickListener(view -> galleryFragment.onImageClick(position));

    }

    @Override
    public int getItemCount() {
        return galleryModels.size();
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
