package com.dariushm2.universify.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.dariushm2.universify.App;
import com.dariushm2.universify.R;
import com.dariushm2.universify.remote.InternetConnectionListener;
import com.dariushm2.universify.view.notification.PictureOfTheDayBroadcastReceiver;
import com.dariushm2.universify.view.pictureOfTheDay.ScrollingActivity;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements InternetConnectionListener {


    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.nav_picture_of_the_day:
                    App app = (App) getApplication();
                    if (app.isInternetAvailable()) {
                        Intent intent = new Intent(MainActivity.this, ScrollingActivity.class);
                        startActivity(intent);
                    } else
                        showNoInternetMessage();
                    drawer.closeDrawers();
                    break;
            }
            return true;
        });
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_picture_of_the_day)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);


        ((App) getApplication()).setInternetConnectionListener(this);


        PictureOfTheDayBroadcastReceiver.setAlarm(getApplicationContext());
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((App) getApplication()).removeInternetConnectionListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onInternetUnavailable() {
        showNoInternetMessage();
    }

    private void showNoInternetMessage() {
        Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
        Log.i(App.TAG, "No Internet!");
    }
}
