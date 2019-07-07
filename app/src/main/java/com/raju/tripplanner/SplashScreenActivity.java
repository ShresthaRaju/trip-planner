package com.raju.tripplanner;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.raju.tripplanner.authentication.SignInActivity;

public class SplashScreenActivity extends AppCompatActivity {

    private ImageView imgLogo;
    private TextView title, subtitle;
    private Animation splashScreen, fadeIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        splashScreen = AnimationUtils.loadAnimation(this, R.anim.splash_screen_animation);
        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_animation);

        imgLogo = findViewById(R.id.logo);
        title = findViewById(R.id.tv_title);
        subtitle = findViewById(R.id.tv_subTitle);

        imgLogo.startAnimation(splashScreen);
        title.startAnimation(fadeIn);
        subtitle.startAnimation(fadeIn);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, SignInActivity.class));
                finish();
            }
        }, 5000);
    }
}
