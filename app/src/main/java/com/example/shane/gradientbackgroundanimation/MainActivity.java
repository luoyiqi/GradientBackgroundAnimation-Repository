package com.example.shane.gradientbackgroundanimation;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView image = (ImageView) findViewById(R.id.background);

        Drawable backgrounds[] = new Drawable[2];
        backgrounds[0] = ContextCompat.getDrawable(this, R.drawable.gradient1);
        backgrounds[1] = ContextCompat.getDrawable(this, R.drawable.gradient2);

        Crossfade(image, backgrounds, 5000);
    }

    public void Crossfade(final ImageView image, final Drawable layers[], final int speedInMs) {
        class BackgroundGradientThread implements Runnable {
            Context mainContext;
            TransitionDrawable crossFader;
            boolean first = true;

            BackgroundGradientThread(Context c) {
                mainContext = c;

                crossFader = new TransitionDrawable(layers);
                crossFader.setCrossFadeEnabled(true);
                image.setImageDrawable(crossFader);
            }

            public void run() {
                while (true) {
                    try { Thread.sleep(speedInMs); } catch (Exception e) { }

                    Handler mHandler = new Handler(mainContext.getMainLooper());

                    Runnable transitionRunnable = new Runnable() {
                        @Override
                        public void run() {
                            if (first) {
                                crossFader.startTransition(speedInMs);
                                first = false;
                            } else {
                                crossFader.reverseTransition(speedInMs);
                                first = true;
                            }
                        }
                    };

                    mHandler.post(transitionRunnable);
                }
            }
        }

        Thread backgroundThread = new Thread(new BackgroundGradientThread(this));
        backgroundThread.start();
    }
}
