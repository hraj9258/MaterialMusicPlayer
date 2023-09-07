package com.example.materialmusicplayer2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // hide the action bar
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        Intent intent = new Intent(SplashActivity.this, MainActivity.class);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        }, 400);
    }
}