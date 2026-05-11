package com.example.projeto8.UI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.example.projeto8.R;

public class LoadScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_load_screen);

        ImageView logo = findViewById(R.id.splashLogo);
        TextView appName = findViewById(R.id.splashName);
        TextView byLine = findViewById(R.id.splashByLine);
        ProgressBar loading = findViewById(R.id.loadingProgress);

        loading.setAlpha(0f);
        loading.animate()
                .alpha(1f)
                .setStartDelay(900)
                .setDuration(400)
                .start();

        logo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.splash_logo));

        appName.setAlpha(0f);
        appName.setTranslationY(16f);
        appName.animate()
                .alpha(1f)
                .translationY(0f)
                .setStartDelay(500)
                .setDuration(400)
                .start();

        byLine.setAlpha(0f);
        byLine.setTranslationY(16f);
        byLine.animate()
                .alpha(1f)
                .translationY(0f)
                .setStartDelay(700)
                .setDuration(400)
                .start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LoadScreenActivity.this, LoginActivity.class);
                startActivity(intent);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    overrideActivityTransition(OVERRIDE_TRANSITION_OPEN, R.anim.fade_in, R.anim.fade_out);
                } else {
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }

                finish();
            }
        }, 2000);
    }
}
