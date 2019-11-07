package com.dariushm2.universify.view;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dariushm2.universify.R;
import com.dariushm2.universify.model.PictureOfTheDay;
import com.dariushm2.universify.remote.NasaServices;
import com.dariushm2.universify.view.util.OnSwipeTouchListener;
import com.dariushm2.universify.viewModel.PictureOfTheDayViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ScrollingActivity extends AppCompatActivity {

    public static final String TAG = "Universify";

    private AppCompatImageView imageView;
    private AppCompatTextView txtExplanation;
    private Toolbar toolbar;

    private Calendar cal = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);

    private PictureOfTheDay pictureOfTheDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view ->

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()

        );

        txtExplanation = findViewById(R.id.txtExplanation);
        imageView = findViewById(R.id.img);

        imageView.setOnTouchListener(new OnSwipeTouchListener(ScrollingActivity.this) {
            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                Log.i(TAG, "onSwipeRight: Detected");
                prevDay();
            }

            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                Log.i(TAG, "onSwipeLeft: Detected");
                nextDay();
            }

        });

        getNextPicture();
        fullScreen();
    }

    private void getNextPicture() {
        NasaServices.REMOTE_API
                .getPictureOfTheDay(NasaServices.API_KEY, dateFormat.format(cal.getTime()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<PictureOfTheDay>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Response<PictureOfTheDay> response) {

                        if (response.body() != null) {
                            Log.i(TAG, "onSuccess: " + response.body().getTitle());

                            pictureOfTheDay = response.body();
                            toolbar.setTitle(pictureOfTheDay.getTitle());
                            txtExplanation.setText(pictureOfTheDay.getExplanation());

                            Glide.with(getApplicationContext())
                                    .load(pictureOfTheDay.getUrl())
                                    .apply(RequestOptions.centerCropTransform())
                                    .into(imageView);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "onError: " + e.getMessage());
                    }
                });
    }

    private void prevDay() {
        cal.add(Calendar.DATE, -1);
        getNextPicture();
    }

    private void nextDay() {
        Calendar today = Calendar.getInstance();
        if (!cal.getTime().equals(today.getTime())) {
            cal.add(Calendar.DATE, 1);
            getNextPicture();
        }
    }

    public void fullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
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
}
