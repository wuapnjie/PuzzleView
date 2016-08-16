package com.xiaopo.flying.photolayout.photolayout;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * Created by snowbean on 16-8-12.
 */
public class LayoutBitmap extends LayoutPhoto {

    private Bitmap mBitmap;

    public LayoutBitmap(Bitmap bitmap, Border border, Matrix matrix) {
        super(matrix, border);
        mBitmap = bitmap;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(mBitmap, mMatrix, paint);
    }

    @Override
    public int getWidth() {
        return mBitmap.getWidth();
    }

    @Override
    public int getHeight() {
        return mBitmap.getHeight();
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    @Override
    public void release() {
        super.release();

        if (mBitmap != null) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }
}
