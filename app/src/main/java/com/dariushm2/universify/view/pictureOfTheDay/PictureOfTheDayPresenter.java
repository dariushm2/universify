package com.dariushm2.universify.view.pictureOfTheDay;

import android.util.SparseArray;

import com.dariushm2.universify.model.PictureOfTheDay;

import java.util.HashMap;
import java.util.Map;

public class PictureOfTheDayPresenter {

    protected Map<Integer, PictureOfTheDay> pictures = new HashMap<>();

    protected Integer position = 0;

    protected SparseArray<PictureOfTheDayFragment> registeredFragments = new SparseArray<>();

    protected static boolean isInitialized = false;

    protected static PictureOfTheDayPresenter presenter;

    private PictureOfTheDayPresenter() {

    }

    protected static PictureOfTheDayPresenter init() {
        if (!isInitialized) {
            presenter = new PictureOfTheDayPresenter();
            isInitialized = true;
        }
        return presenter;
    }

}
