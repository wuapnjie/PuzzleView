package com.xiaopo.flying.puzzle;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

/**
 * Created by snowbean on 16-8-9.
 */
public abstract class PuzzlePiece {
    protected Matrix mMatrix;
    protected Border mBorder;
    protected Matrix mDownMatrix;
    protected Matrix mMoveMatrix;


    public PuzzlePiece(Matrix matrix, Border border) {
        mMatrix = matrix;
        mBorder = border;

        mDownMatrix = new Matrix();
        mMoveMatrix = new Matrix();
    }

    public Matrix getMatrix() {
        return mMatrix;
    }

    public void setMatrix(Matrix matrix) {
        mMatrix = matrix;
    }

    public Matrix getDownMatrix() {
        return mDownMatrix;
    }

    public void setDownMatrix(Matrix downMatrix) {
        mDownMatrix = downMatrix;
    }

    public Matrix getMoveMatrix() {
        return mMoveMatrix;
    }

    public void setMoveMatrix(Matrix moveMatrix) {
        mMoveMatrix = moveMatrix;
    }

    public Border getBorder() {
        return mBorder;
    }

    public void setBorder(Border border) {
        mBorder = border;
    }

    public abstract void draw(Canvas canvas, Paint paint);

    public abstract int getWidth();

    public abstract int getHeight();

    public float[] getBoundPoints() {
        return new float[]{
                0f, 0f,
                getWidth(), 0f,
                0f, getHeight(),
                getWidth(), getHeight()
        };
    }

    public float[] getMappedBoundPoints() {
        float[] dst = new float[8];
        mMatrix.mapPoints(dst, getBoundPoints());
        return dst;
    }

    public float[] getMappedPoints(float[] src) {
        float[] dst = new float[src.length];
        mMatrix.mapPoints(dst, src);
        return dst;
    }


    public RectF getBound() {
        return new RectF(0, 0, getWidth(), getHeight());
    }

    public RectF getMappedBound() {
        RectF dst = new RectF();
        mMatrix.mapRect(dst, getBound());
        return dst;
    }

    public PointF getCenterPoint() {
        return new PointF(getWidth() / 2, getHeight() / 2);
    }

    public PointF getMappedCenterPoint() {
        PointF pointF = getCenterPoint();
        float[] dst = getMappedPoints(new float[]{
                pointF.x,
                pointF.y
        });
        return new PointF(dst[0], dst[1]);
    }

    public boolean contains(float x, float y) {
        return mBorder.getRect().contains(x, y);
    }

    public void release() {
        if (mMatrix != null) {
            mMatrix.reset();
            mMatrix = null;
        }
    }
}
