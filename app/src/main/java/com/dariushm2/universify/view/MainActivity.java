package com.dariushm2.universify.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.dariushm2.universify.R;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        AppCompatImageView imageView = findViewById(R.id.img);


        imageView.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ScrollingActivity.class);
            startActivity(intent);

        });


    }


}
