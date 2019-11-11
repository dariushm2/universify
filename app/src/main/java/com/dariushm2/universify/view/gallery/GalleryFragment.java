package com.dariushm2.universify.view.gallery;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dariushm2.universify.App;
import com.dariushm2.universify.R;
import com.dariushm2.universify.model.frontend.GalleryListModel;
import com.dariushm2.universify.repository.GalleryPresenter;


public class GalleryFragment extends Fragment implements View.OnClickListener, GalleryDataEvents {

    private GalleryListModel galleryListModel;
    private RecyclerView recyclerView;
    private GalleryAdapter galleryAdapter;

    private GalleryPresenter galleryPresenter;

    private GalleryDataEvents galleryDataEvents;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);


        if (getActivity() != null) {
            SearchView searchView = getActivity().findViewById(R.id.searchView);

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    galleryPresenter.search(query);
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
                    galleryPresenter.next();
                }
            }
        });

        galleryDataEvents = this;

        if (savedInstanceState == null && getActivity() != null) {
            App app = (App) getActivity().getApplication();
            galleryPresenter = new GalleryPresenter(app, galleryDataEvents, "mars");
        }

        setClickListeners(view);

        return view;
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
                galleryPresenter.search(getString(R.string.universe));
                break;
            case R.id.btnApollo:
                galleryPresenter.search(getString(R.string.apollo));
                break;
            case R.id.btnMars:
                galleryPresenter.search(getString(R.string.mars));
                break;
            case R.id.btnISS:
                galleryPresenter.search(getString(R.string.iss));
                break;
            case R.id.btnMoon:
                galleryPresenter.search(getString(R.string.moon));
                break;
            case R.id.btnCuriosity:
                galleryPresenter.search(getString(R.string.curiosity));
                break;
            case R.id.btnVoyager:
                galleryPresenter.search(getString(R.string.voyager));
                break;
            case R.id.btnShuttle:
                galleryPresenter.search(getString(R.string.shuttle));
                break;
            case R.id.btnRocket:
                galleryPresenter.search(getString(R.string.rocket));
                break;
        }
    }

    @Override
    public void setUpAdapterAndView(GalleryListModel galleryListModel) {
        Log.e(App.TAG, "setUpAdapterAndView: " + galleryListModel.getErrorMessage() + galleryListModel.getGalleryModels().size());
        this.galleryListModel = galleryListModel;
        if (galleryAdapter == null) {
            galleryAdapter = new GalleryAdapter(this.galleryListModel.getGalleryModels(), getContext());
            recyclerView.setAdapter(galleryAdapter);
            return;
        }
        galleryAdapter.notifyDataSetChanged();
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        galleryPresenter.stop();
    }


    public static void hideKeyboard(Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.hideSoftInputFromWindow(((Activity) mContext).getWindow()
                    .getCurrentFocus().getWindowToken(), 0);
    }

}
