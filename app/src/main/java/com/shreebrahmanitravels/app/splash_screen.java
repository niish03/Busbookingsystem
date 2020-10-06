package com.shreebrahmanitravels.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.TextView;

public class splash_screen extends AppCompatActivity {
    TextView textView;
    private static int SPLASH_SCREEN=2000;
    //private static int SPLASH_SCREEN=3000;
    Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent= new Intent(splash_screen.this, login.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);

    }
}