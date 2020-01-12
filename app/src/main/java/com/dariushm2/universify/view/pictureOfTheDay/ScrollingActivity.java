package com.dariushm2.universify.view.pictureOfTheDay;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.dariushm2.universify.App;
import com.dariushm2.universify.R;
import com.dariushm2.universify.model.PictureOfTheDay;
import com.dariushm2.universify.remote.InternetConnectionListener;
import com.dariushm2.universify.remote.NasaServices;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ScrollingActivity extends AppCompatActivity {


    public static final int ON_NOTIFICATION_CLICK = 3;

    private AppCompatTextView txtExplanation;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private Calendar cal = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    private PictureOfTheDayPresenter presenter = PictureOfTheDayPresenter.init();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scrolling);

        initUi();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((App) getApplication()).removeInternetConnectionListener();
    }

    private void initUi() {

        //fullScreen();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        collapsingToolbarLayout = findViewById(R.id.toolbar_layout);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
                    WallpaperManager myWallpaperManager
                            = WallpaperManager.getInstance(getApplicationContext());
                    try {
                        Bitmap bitmap = getImage();
                        if (bitmap != null) {
                            myWallpaperManager.setBitmap(bitmap);
                            Toast.makeText(getApplicationContext(), R.string.wallpaperChanged, Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), R.string.failedToChangeWallpaper, Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
        );

        txtExplanation = findViewById(R.id.txtExplanation);
        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new ImageViewPagerAdapter(getSupportFragmentManager()));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.e(App.TAG, "onPageSelected: " + position);
                presenter.position = position;
                if (presenter.pictures.containsKey(position)) {
                    collapsingToolbarLayout.setTitle(presenter.pictures.get(position).getTitle());
                    txtExplanation.setText(presenter.pictures.get(position).getExplanation());
                    sendUrlToCurrentFragment(presenter.pictures.get(position).getUrl());
                } else {
                    collapsingToolbarLayout.setTitle("...");
                    txtExplanation.setText("...");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private Bitmap getImage() {
        return presenter.registeredFragments.get(presenter.position).getImage();
    }

    private void getPicture(int position) {

        setDate(position);

        App app = (App) getApplication();
        app.getRetrofitFor(NasaServices.BASE_URL_PICTURE_OF_THE_DAY)
                .getPictureOfTheDay(NasaServices.API_KEY, dateFormat.format(cal.getTime()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(response -> {
                    if (response.body() != null) {
                        Log.e(App.TAG, "onSuccess: " + response.body().getTitle());
                        presenter.pictures.put(position, response.body());

                        if (position == 0) {
                            collapsingToolbarLayout.setTitle(presenter.pictures.get(position).getTitle());
                            txtExplanation.setText(presenter.pictures.get(position).getExplanation());
                            sendUrlToCurrentFragment(presenter.pictures.get(position).getUrl());
                        }
                    }
                })
                .doOnError(throwable -> Log.e(App.TAG, throwable.getMessage()))
                .subscribe();
    }

    private void sendUrlToCurrentFragment(String url) {
        presenter.registeredFragments.get(presenter.position).showImage(url);
    }

    private void setDate(int position) {
        cal = Calendar.getInstance();
        int pos = position;
        if (pos > 0)
            pos = position * -1;
        cal.add(Calendar.DATE, pos);
    }


    public void fullScreen() {
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private class ImageViewPagerAdapter extends FragmentStatePagerAdapter {


        ImageViewPagerAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return String.valueOf(position);
        }

        @Override
        public int getCount() {
            return 1000;
        }

        @Override
        @NonNull
        public Fragment getItem(int position) {
            if (!presenter.pictures.containsKey(position))
                getPicture(position);

            return PictureOfTheDayFragment.newInstance(position);
        }

        @Override
        @NonNull
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            PictureOfTheDayFragment fragment = (PictureOfTheDayFragment) super.instantiateItem(container, position);
            presenter.registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            presenter.registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        public Fragment getRegisteredFragment(int position) {
            return presenter.registeredFragments.get(position);
        }

    }
}
