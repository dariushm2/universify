package com.dariushm2.universify.view.gallery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dariushm2.universify.App;
import com.dariushm2.universify.R;
import com.dariushm2.universify.model.frontend.GalleryListModel;
import com.dariushm2.universify.repository.GalleryPresenter;
import com.dariushm2.universify.view.image.ImageActivity;


public class GalleryFragment extends Fragment implements View.OnClickListener, GalleryDataEvents {

    private RecyclerView recyclerView;
    private ContentLoadingProgressBar progressBar;

    private GalleryListModel galleryListModel;
    private GalleryAdapter galleryAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
//        galleryListModel = null;
//        galleryAdapter = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);


        if (getActivity() != null) {
            SearchView searchView = getActivity().findViewById(R.id.searchView);

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    GalleryPresenter.search(query);
                    if (getContext() != null)
                        hideKeyboard(getContext());
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 5));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    GalleryPresenter.next();
                }
            }
        });

        progressBar = view.findViewById(R.id.progressBar);

        GalleryDataEvents galleryDataEvents = this;

        setClickListeners(view);


        if (savedInstanceState == null && getActivity() != null) {
            App app = (App) getActivity().getApplication();
            GalleryPresenter.init(app.getNasaServices(), galleryDataEvents, "universe");
        } else {
            progressBar.hide();

            recyclerView.setAdapter(null);
            recyclerView.setLayoutManager(null);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 5));
            galleryAdapter = new GalleryAdapter(GalleryPresenter.getGalleryModels(), this);
            recyclerView.setAdapter(galleryAdapter);

            galleryAdapter.notifyDataSetChanged();
        }

        return view;
    }

    public void onImageClick(int position) {
        Intent intent = new Intent(getContext(), ImageActivity.class);
        intent.putExtra("position", position);
        if (getActivity() != null)
            getActivity().startActivity(intent);
    }

    private void setClickListeners(View view) {
        view.findViewById(R.id.btnUniverse).setOnClickListener(this);
        view.findViewById(R.id.btnApollo).setOnClickListener(this);
        view.findViewById(R.id.btnMars).setOnClickListener(this);
        view.findViewById(R.id.btnISS).setOnClickListener(this);
        view.findViewById(R.id.btnMoon).setOnClickListener(this);
        view.findViewById(R.id.btnCuriosity).setOnClickListener(this);
        view.findViewById(R.id.btnVoyager).setOnClickListener(this);
        view.findViewById(R.id.btnShuttle).setOnClickListener(this);
        view.findViewById(R.id.btnRocket).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnUniverse:
                GalleryPresenter.search(getString(R.string.universe));
                break;
            case R.id.btnApollo:
                GalleryPresenter.search(getString(R.string.apollo));
                break;
            case R.id.btnMars:
                GalleryPresenter.search(getString(R.string.mars));
                break;
            case R.id.btnISS:
                GalleryPresenter.search(getString(R.string.iss));
                break;
            case R.id.btnMoon:
                GalleryPresenter.search(getString(R.string.moon));
                break;
            case R.id.btnCuriosity:
                GalleryPresenter.search(getString(R.string.curiosity));
                break;
            case R.id.btnVoyager:
                GalleryPresenter.search(getString(R.string.voyager));
                break;
            case R.id.btnShuttle:
                GalleryPresenter.search(getString(R.string.shuttle));
                break;
            case R.id.btnRocket:
                GalleryPresenter.search(getString(R.string.rocket));
                break;
        }
    }

    @Override
    public void setUpAdapterAndView(GalleryListModel galleryListModel) {

        progressBar.hide();
        this.galleryListModel = galleryListModel;
        if (galleryAdapter == null) {
            galleryAdapter = new GalleryAdapter(this.galleryListModel.getGalleryModels(), this);
            recyclerView.setAdapter(galleryAdapter);
            Log.e(App.TAG, "Adapter is  null");
            return;
        }
        galleryAdapter.notifyDataSetChanged();
        Log.e(App.TAG, "setUpAdapterAndView: " + galleryListModel.getGalleryModels().size());
    }

    @Override
    public void showErrorMessage(String error) {
        Log.e(App.TAG, "showErrorMessage: " + error);
    }

    @Override
    public void startMainActivity() {

    }

    @Override
    public void showLoadingIndicator() {
        Log.e(App.TAG, "showLoadingIndicator: ");
        //progressBar.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GalleryPresenter.stop();
    }


    public static void hideKeyboard(Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.hideSoftInputFromWindow(((Activity) mContext).getWindow()
                    .getCurrentFocus().getWindowToken(), 0);
    }

}
