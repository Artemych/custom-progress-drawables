package com.example.artem.CustomProgressDrawables.activity;

import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.example.artem.CustomProgressDrawables.R;
import com.example.artem.CustomProgressDrawables.drawable.CustomProgressIndeterminateDrawable;
import com.example.artem.CustomProgressDrawables.drawable.CustomProgressDrawable;

public class MainActivity extends ActionBarActivity {

    public static final int PROGRESS_MAX_VALUE = 10000;
    public static final int COUNT_DOWN_INTERVAL = 100;
    public static final int INDETERMINATE_DURATION = 1000;
    private ProgressBar mCustomProgressBar;
    private CountDownTimer mIndeterminateTimer;
    private CountDownTimer mDeterminateTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        final int color = getResources().getColor(R.color.green);

        // initialize drawables
        CustomProgressDrawable progressDrawable = new CustomProgressDrawable(color);
        CustomProgressIndeterminateDrawable barIndeterminateDrawable = new CustomProgressIndeterminateDrawable(color);

        // set drawables and max value
        mCustomProgressBar = (ProgressBar) findViewById(R.id.custom_progress_bar);
        mCustomProgressBar.setIndeterminateDrawable(barIndeterminateDrawable);
        mCustomProgressBar.setProgressDrawable(progressDrawable);
        mCustomProgressBar.setMax(PROGRESS_MAX_VALUE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startProgressAnimation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopProgressAnimation();
    }

    private void startProgressAnimation() {
        mCustomProgressBar.setIndeterminate(true);
        if (mIndeterminateTimer == null) {
            initIndeterminateTimer();
        } else {
            mIndeterminateTimer.start();
        }
    }

    private void initIndeterminateTimer() {
        mIndeterminateTimer = new CountDownTimer(INDETERMINATE_DURATION, INDETERMINATE_DURATION) {
            @Override
            public void onTick(long millisUntilFinished) {}

            @Override
            public void onFinish() {
                mCustomProgressBar.setIndeterminate(false);
                if (mDeterminateTimer == null) {
                    initDeterminateTimer();
                } else {
                    mDeterminateTimer.start();
                }
            }
        }.start();
    }

    private void initDeterminateTimer() {
        mDeterminateTimer = new CountDownTimer(PROGRESS_MAX_VALUE, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                final int progress = (int)(PROGRESS_MAX_VALUE - millisUntilFinished);

                mCustomProgressBar.setProgress(progress);
            }

            @Override
            public void onFinish() {
                mCustomProgressBar.setIndeterminate(true);
                mIndeterminateTimer.start();
            }
        }.start();
    }

    private void stopProgressAnimation() {
        mDeterminateTimer.cancel();
        mIndeterminateTimer.cancel();
    }
}
