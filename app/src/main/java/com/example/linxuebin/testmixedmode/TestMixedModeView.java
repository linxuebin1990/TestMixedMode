package com.example.linxuebin.testmixedmode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by linxuebin on 18/3/10.
 */

public class TestMixedModeView extends View {

    private static final PorterDuff.Mode[] MIXED_MODE_ARR = new PorterDuff.Mode[]{
            /** [0, 0] */
            PorterDuff.Mode.CLEAR,
            /** [Sa, Sc] */
            PorterDuff.Mode.SRC,
            /** [Da, Dc] */
            PorterDuff.Mode.DST,
            /** [Sa + (1 - Sa)*Da, Rc = Sc + (1 - Sa)*Dc] */
            PorterDuff.Mode.SRC_OVER,
            /** [Sa + (1 - Sa)*Da, Rc = Dc + (1 - Da)*Sc] */
            PorterDuff.Mode.DST_OVER,
            /** [Sa * Da, Sc * Da] */
            PorterDuff.Mode.SRC_IN,
            /** [Sa * Da, Sa * Dc] */
            PorterDuff.Mode.DST_IN,
            /** [Sa * (1 - Da), Sc * (1 - Da)] */
            PorterDuff.Mode.SRC_OUT,
            /** [Da * (1 - Sa), Dc * (1 - Sa)] */
            PorterDuff.Mode.DST_OUT,
            /** [Da, Sc * Da + (1 - Sa) * Dc] */
            PorterDuff.Mode.SRC_ATOP,
            /** [Sa, Sa * Dc + Sc * (1 - Da)] */
            PorterDuff.Mode.DST_ATOP,
            /** [Sa + Da - 2 * Sa * Da, Sc * (1 - Da) + (1 - Sa) * Dc] */
            PorterDuff.Mode.XOR,
            /** [Sa + Da - Sa*Da,
            Sc*(1 - Da) + Dc*(1 - Sa) + min(Sc, Dc)] */
            PorterDuff.Mode.DARKEN,
            /** [Sa + Da - Sa*Da,
            Sc*(1 - Da) + Dc*(1 - Sa) + max(Sc, Dc)] */
            PorterDuff.Mode.LIGHTEN,
            /** [Sa * Da, Sc * Dc] */
            PorterDuff.Mode.MULTIPLY,
            /** [Sa + Da - Sa * Da, Sc + Dc - Sc * Dc] */
            PorterDuff.Mode.SCREEN,
    };

    private static final String[] MIXED_MODE_STR_ARR = new String[]{
            "CLEAR",
            "SRC",
            "DST",
            "SRC_OVER",
            "DST_OVER",
            "SRC_IN",
            "DST_IN",
            "SRC_OUT",
            "DST_OUT",
            "SRC_ATOP",
            "DST_ATOP",
            "XOR",
            "DARKEN",
            "LIGHTEN",
            "MULTIPLY",
            "SCREEN"
    };

    private static final int BACKGROUND_COLOR = Color.WHITE;
    private static final int TEXT_COLOR = Color.BLACK;
    private static final int ROW_ITEM_NUM = 4;

    private int mWidth;
    private int mHeight;
    private Paint mPaint;
    private Bitmap mSrcBitmap;
    private Bitmap mDstBitmap;
    private Canvas mSrcBitmapCanvas;
    private Canvas mDstBitmapCanvas;
    private boolean mIsBitmap;
    private int mAlphaInt;
    private int mCircleColor = 0xFFFF0000;
    private int mSquareColor = 0xFF0000FF;

    public TestMixedModeView(Context context) {
        this(context, null);
    }

    public TestMixedModeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestMixedModeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(BACKGROUND_COLOR);
        int xOffset = mWidth / 20;
        int yOffset = mHeight / 20;
        int len = MIXED_MODE_ARR.length;
        int xLen = (mWidth - xOffset * 2) / ROW_ITEM_NUM;
        int yLen = (mHeight - yOffset * 2) / ((len + ROW_ITEM_NUM - 1) / ROW_ITEM_NUM);
        RectF rectF = new RectF();
        for (int i = 0; i < len; i++) {
            int rows = i / 4;
            int cols = i % 4;
            rectF.set(xOffset + cols * xLen, yOffset + yLen * rows,
                    xOffset + cols * xLen + xLen, yOffset + yLen * rows + yLen);
            if (mIsBitmap) {
                drawMixedModeCaverAll(rectF, MIXED_MODE_ARR[i], canvas, mPaint, MIXED_MODE_STR_ARR[i]);
            } else {
                drawMixedMode(rectF, MIXED_MODE_ARR[i], canvas, mPaint, MIXED_MODE_STR_ARR[i]);
            }
        }
    }

    private void drawMixedMode(RectF rectF, PorterDuff.Mode mode,
                               Canvas canvas, Paint paint, String mixedModeStr) {
        int layerId = canvas.saveLayer(rectF, mPaint, Canvas.ALL_SAVE_FLAG);
        int width = (int) rectF.width();
        int height = (int) rectF.height();
        int len = Math.min(width, height);
        int radius = len / 4;
        int squareLen = radius * 2;

        int offsetX = (width - squareLen - radius) / 2;

        int textSize = radius * 2 / 3;
        paint.setTextSize(textSize);
        Rect textBounds = new Rect();
        paint.getTextBounds(mixedModeStr, 0, mixedModeStr.length(), textBounds);

        // dst
        paint.setColor(mCircleColor);
        canvas.drawCircle(rectF.left + offsetX + radius,
                rectF.top + textBounds.height() * 2 + radius, radius, paint);

        paint.setXfermode(new PorterDuffXfermode(mode));

        //src
        paint.setColor(mSquareColor);
        Rect rect = new Rect(0, 0, squareLen, squareLen);
        rect.offset((int) (rectF.left + offsetX + radius),
                (int) (rectF.top + textBounds.height() * 2 + radius));
        canvas.drawRect(rect, paint);

        paint.setXfermode(null);
        canvas.restoreToCount(layerId);

        // draw text
        paint.setColor(TEXT_COLOR);
        canvas.drawText(mixedModeStr, 0, mixedModeStr.length(),
                rectF.left + width / 2 - textBounds.width() / 2,
                rectF.top - textBounds.top + textBounds.height() / 2, paint);
    }

    private void drawMixedModeCaverAll(RectF rectF, PorterDuff.Mode mode,
                                       Canvas canvas, Paint paint, String mixedModeStr) {
        int layerId = canvas.saveLayer(rectF, mPaint, Canvas.ALL_SAVE_FLAG);
        int width = (int) rectF.width();
        int height = (int) rectF.height();
        int len = Math.min(width, height);
        int radius = len / 4;
        int squareLen = radius * 2;

        int offsetX = (width - squareLen - radius) / 2;

        int textSize = radius * 2 / 3;
        paint.setTextSize(textSize);
        Rect textBounds = new Rect();
        paint.getTextBounds(mixedModeStr, 0, mixedModeStr.length(), textBounds);

        Rect drawArea = new Rect(0, 0,
                radius + squareLen, radius + squareLen);

        // src bitmap
        if (mSrcBitmap == null) {
            mSrcBitmap = Bitmap.createBitmap(drawArea.width(), drawArea.height(),
                    Bitmap.Config.ARGB_8888);
            mSrcBitmapCanvas = new Canvas(mSrcBitmap);
            paint.setColor(mSquareColor);
            Rect rect = new Rect(0, 0, squareLen, squareLen);
            rect.offset(radius, radius);
            mSrcBitmapCanvas.drawRect(rect, paint);
        }

        // dst bitmap
        if (mDstBitmap == null) {
            mDstBitmap = Bitmap.createBitmap(drawArea.width(), drawArea.height(),
                    Bitmap.Config.ARGB_8888);
            mDstBitmapCanvas = new Canvas(mDstBitmap);
            paint.setColor(mCircleColor);
            mDstBitmapCanvas.drawCircle(radius, radius, radius, paint);
        }

        drawArea.offset((int) rectF.left + offsetX,
                (int) rectF.top + textBounds.height() * 2);

        paint.setColor(mCircleColor);
        // dst
        canvas.drawBitmap(mDstBitmap, drawArea.left, drawArea.top, paint);
        paint.setXfermode(new PorterDuffXfermode(mode));

        //src
        canvas.drawBitmap(mSrcBitmap, drawArea.left, drawArea.top, paint);

        paint.setXfermode(null);
        canvas.restoreToCount(layerId);

        // draw text
        paint.setColor(TEXT_COLOR);
        canvas.drawText(mixedModeStr, 0, mixedModeStr.length(),
                rectF.left + width / 2 - textBounds.width() / 2,
                rectF.top - textBounds.top + textBounds.height() / 2, paint);
    }

    public void setDrawByBitmap(boolean isBitmap) {
        mIsBitmap = isBitmap;
        postInvalidate();
    }

    public void setAlpha(float alpha) {
        mAlphaInt = (int) (alpha * 255);
        mCircleColor = ((mAlphaInt & 0xff) << 24) | (mCircleColor & 0x00ffffff);
        mSquareColor = ((mAlphaInt & 0xff) << 24) | (mSquareColor & 0x00ffffff);
        postInvalidate();
    }
}
