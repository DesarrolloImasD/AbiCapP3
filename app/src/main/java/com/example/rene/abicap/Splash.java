package com.example.rene.abicap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.camilodesarrollo.abicap.R;

/**
 * Created by Miguel on 23/03/2018.
 */

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView imageView = (ImageView) findViewById(R.id.imagenSplash);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
        imageView.startAnimation(animation);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);

         Thread timer = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(3000);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                    super.run();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        };
        timer.start();
    }
}
