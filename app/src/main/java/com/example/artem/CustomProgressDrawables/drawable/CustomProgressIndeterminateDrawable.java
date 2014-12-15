package com.example.artem.CustomProgressDrawables.drawable;

import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.view.animation.AnimationUtils;

public class CustomProgressIndeterminateDrawable extends Drawable implements Animatable, Runnable {

    public static final int LOOP_MILLS = 2000;
    public static final float FULL_CIRCLE_DEGREES = 360f;
    public static final int Z_CAMERA_COORDINATE = -100;

    private Paint mPaint;
    private long mStartTicks = 0;
    private boolean mIsRunning = false;
    private Camera mCamera = new Camera();
    private Matrix mMatrix = new Matrix();

    public CustomProgressIndeterminateDrawable(int color) {
        super();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(color);
    }

    @Override
    public void draw(Canvas canvas) {
        final Rect bounds = getBounds();
        final float loopPercent = calculateCurrentLoopPercent();
        final float centerX = bounds.centerX();
        final float centerY = bounds.centerY();

        canvas.save();

        mCamera.save();
        mCamera.rotateX(FULL_CIRCLE_DEGREES * loopPercent);
        mCamera.setLocation(0, 0, Z_CAMERA_COORDINATE);
        mCamera.getMatrix(mMatrix);

        mMatrix.preTranslate(-centerX, -centerY);
        mMatrix.postTranslate(centerX, centerY);

        mCamera.restore();

        canvas.concat(mMatrix);
        canvas.drawCircle(bounds.centerX(), bounds.centerY(), centerX, mPaint);
        canvas.restore();
    }

    @Override
    public void run() {
        invalidateSelf();
        scheduleSelf(this, AnimationUtils.currentAnimationTimeMillis());
    }

    @Override
    public boolean isRunning() {
        return mIsRunning;
    }

    @Override
    public void start() {
        if (!isRunning()) {
            mIsRunning = true;
            mStartTicks = AnimationUtils.currentAnimationTimeMillis();
            run();
        }
    }

    @Override
    public void stop() {
        if (isRunning()) {
            unscheduleSelf(this);
            mIsRunning = false;
        }
    }

    @Override
    public int getOpacity() {
        return mPaint.getAlpha();
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter filter) {
        mPaint.setColorFilter(filter);
    }

    private float calculateCurrentLoopPercent() {
        float loopPercent = 0f;
        if (isRunning()) {
            float loopMillis = LOOP_MILLS;
            loopPercent = (AnimationUtils.currentAnimationTimeMillis() - mStartTicks) / loopMillis;
            while (loopPercent > 1) {
                loopPercent -= 1;
                mStartTicks += loopMillis;
            }
        }
        return loopPercent;
    }
}