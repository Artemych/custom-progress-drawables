package com.example.artem.CustomProgressDrawables.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import com.example.artem.CustomProgressDrawables.activity.MainActivity;

public class CustomProgressDrawable extends Drawable {

    private Paint mPaint;
    private float mDrawTo;
    private RectF mBoundsF;
    private RectF mInnerBoundsF;

    private static final float CANVAS_1ST_ARC_ROTATE_DEGREES = 0f;
    private static final float CANVAS_2ND_ARC_ROTATE_DEGREES = -180f;
    private static final int HALF_CIRCLE_DEGREES = 180;
    private final float START_ANGLE = 0.f;

    public CustomProgressDrawable(int color) {
        super();
        mPaint = new Paint();
        mPaint.setColor(color);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.rotate(CANVAS_1ST_ARC_ROTATE_DEGREES, getBounds().centerX(), getBounds().centerY());
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawOval(mBoundsF, mPaint);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawArc(mInnerBoundsF, START_ANGLE, mDrawTo, true, mPaint);
        canvas.rotate(CANVAS_2ND_ARC_ROTATE_DEGREES, getBounds().centerX(), getBounds().centerY());
        canvas.drawArc(mInnerBoundsF, START_ANGLE, mDrawTo, true, mPaint);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        final int halfBorder = (int) (mPaint.getStrokeWidth() / 2 + 0.5f);
        mBoundsF = mInnerBoundsF = new RectF(bounds);
        mInnerBoundsF.inset(halfBorder, halfBorder);
    }

    @Override
    protected boolean onLevelChange(int level) {
        final  float drawTo = START_ANGLE + HALF_CIRCLE_DEGREES * level / (float)MainActivity.PROGRESS_MAX_VALUE;
        boolean update = drawTo != mDrawTo;
        mDrawTo = drawTo;
        invalidateSelf();

        return update;
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return mPaint.getAlpha();
    }
}