package com.dariushm2.universify.view.gallery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.dariushm2.universify.remote.NasaServices;
import com.dariushm2.universify.repository.DaggerModelConverterComponent;
import com.dariushm2.universify.repository.ModelConverter;
import com.dariushm2.universify.repository.BaseSchedulersProvider;
import com.dariushm2.universify.repository.GalleryPresenter;
import com.dariushm2.universify.repository.ModelConverterComponent;
import com.dariushm2.universify.repository.ModelConverterModule;
import com.dariushm2.universify.repository.SchedulersProvider;
import com.dariushm2.universify.view.image.ImageActivity;

import javax.inject.Inject;


public class GalleryFragment extends Fragment implements View.OnClickListener, GalleryDataEvents {

    private RecyclerView recyclerView;
    private ContentLoadingProgressBar progressBar;
    private GalleryPresenter galleryPresenter;
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
                    galleryPresenter.setSearchQuery(query);
                    galleryPresenter.getNextImages();
                    Log.e(App.TAG, "onQueryTextSubmit");
                    saveQueryToPrefs(query);
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
                    galleryPresenter.getNextImages();
                }
            }
        });

        progressBar = view.findViewById(R.id.progressBar);

        GalleryDataEvents galleryDataEvents = this;

        setClickListeners(view);


        if (savedInstanceState == null && getActivity() != null) {
            Log.e(App.TAG, "saveInstanceState is null");
            App app = (App) getActivity().getApplication();

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            String lastSearch = prefs.getString(getString(R.string.lastQuery), "Universe");

            BaseSchedulersProvider schedulersProvider = new SchedulersProvider();

            ModelConverter converter = new ModelConverter(app.getRetrofitFor(NasaServices.BASE_URL_IMAGE_LIBRARY));
            GalleryPresenter.init(app.getRetrofitFor(NasaServices.BASE_URL_IMAGE_LIBRARY), galleryDataEvents, converter, schedulersProvider);
            galleryPresenter = GalleryPresenter.getInstance();
            galleryPresenter.setSearchQuery(lastSearch);

            setSearchQuery(lastSearch);
        } else {
            progressBar.hide();

            recyclerView.setAdapter(null);
            recyclerView.setLayoutManager(null);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 5));
            galleryAdapter = new GalleryAdapter(galleryPresenter.getGalleryModels(), this);
            recyclerView.setAdapter(galleryAdapter);

            galleryAdapter.notifyDataSetChanged();
        }

        return view;
    }

    private void saveQueryToPrefs(String query) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        prefs.edit().putString(getString(R.string.lastQuery), query).apply();
    }

    protected void onImageClick(int position) {
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
                galleryPresenter.setSearchQuery(getString(R.string.universe));
                setSearchQuery(R.string.universe);
                break;
            case R.id.btnApollo:
                galleryPresenter.setSearchQuery(getString(R.string.apollo));
                setSearchQuery(R.string.apollo);
                break;
            case R.id.btnMars:
                galleryPresenter.setSearchQuery(getString(R.string.mars));
                setSearchQuery(R.string.mars);
                break;
            case R.id.btnISS:
                galleryPresenter.setSearchQuery(getString(R.string.iss));
                setSearchQuery(R.string.iss);
                break;
            case R.id.btnMoon:
                galleryPresenter.setSearchQuery(getString(R.string.moon));
                setSearchQuery(R.string.moon);
                break;
            case R.id.btnCuriosity:
                galleryPresenter.setSearchQuery(getString(R.string.curiosity));
                setSearchQuery(R.string.curiosity);
                break;
            case R.id.btnVoyager:
                galleryPresenter.setSearchQuery(getString(R.string.voyager));
                setSearchQuery(R.string.voyager);
                break;
            case R.id.btnShuttle:
                galleryPresenter.setSearchQuery(getString(R.string.shuttle));
                setSearchQuery(R.string.shuttle);
                break;
            case R.id.btnRocket:
                galleryPresenter.setSearchQuery(getString(R.string.rocket));
                setSearchQuery(R.string.rocket);
                break;
        }
    }

    private void setSearchQuery(int id) {
        SearchView searchView = null;
        if (getActivity() != null)
            searchView = getActivity().findViewById(R.id.searchView);
        if (searchView != null && getContext() != null)
            searchView.setQuery(getContext().getString(id), true);
       saveQueryToPrefs(getString(id));

    }

    private void setSearchQuery(String query) {
        SearchView searchView = null;
        if (getActivity() != null)
            searchView = getActivity().findViewById(R.id.searchView);
        if (searchView != null && getContext() != null)
            searchView.setQuery(query, true);
        saveQueryToPrefs(query);

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
    public void showLoadingIndicator() {
        Log.e(App.TAG, "showLoadingIndicator");
        //progressBar.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(App.TAG, "GalleryFragment: onDestroy");
        galleryPresenter.stop();
    }


    public static void hideKeyboard(Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = ((Activity) mContext).getWindow()
                .getCurrentFocus();
        if (imm != null && view != null)
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
