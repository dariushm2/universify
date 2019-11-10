package com.dariushm2.universify.view.gallery;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dariushm2.universify.App;
import com.dariushm2.universify.R;
import com.dariushm2.universify.model.ImageItems;
import com.dariushm2.universify.model.ImageLink;
import com.dariushm2.universify.model.ImageLinks;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;


public class GalleryFragment extends Fragment {

    private ImageLinks imageLinks;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);


        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 5));

        //GalleryAdapter galleryAdapter = new GalleryAdapter(pictures, this);

        if (getActivity() != null) {
            App app = (App) getActivity().getApplication();

            app.getNasaServices()
                    .getSearchPictures("apollo")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<Response<ImageItems>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(Response<ImageItems> response) {
                            Log.i(App.TAG, "" + response.body().getImageItem().get(0).getImageLinks().get(0).getUrl());
                            if (response.body() != null) {
//                                imageLinks.setImageLinks(response.body().getImageLinks());
//                                Log.i(App.TAG, imageLinks.getImageLinks().get(0).getUrl());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.i(App.TAG, e.getMessage());
                        }
                    });
        }


        return view;
    }
}
