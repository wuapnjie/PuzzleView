package com.xiaopo.flying.puzzle;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Created by snowbean on 16-8-12.
 */
public class DrawablePiece extends PuzzlePiece {

    private Drawable mDrawable;
    private Rect mRealBound;

    public DrawablePiece(Drawable drawable, Border border, Matrix matrix) {
        super(matrix, border);
        mDrawable = drawable;
        mRealBound = new Rect(0, 0, getWidth(), getHeight());
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.save();
        canvas.concat(mMatrix);
        mDrawable.setBounds(mRealBound);
        mDrawable.draw(canvas);
        canvas.restore();
    }

    @Override
    public int getWidth() {
        return mDrawable.getIntrinsicWidth();
    }

    @Override
    public int getHeight() {
        return mDrawable.getIntrinsicHeight();
    }


    public Drawable getDrawable() {
        return mDrawable;
    }

    public void setDrawable(Drawable drawable) {
        mDrawable = drawable;
    }

    @Override
    public void release() {
        super.release();

        if (mDrawable != null) {
            mDrawable = null;
        }
    }
}
