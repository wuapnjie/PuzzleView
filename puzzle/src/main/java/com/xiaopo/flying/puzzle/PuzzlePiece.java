package com.xiaopo.flying.puzzle;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

/**
 * the puzzle piece , the content can be a bitmap or drawable
 *
 * @see BitmapPiece
 * @see DrawablePiece
 * <p>
 * Created by snowbean on 16-8-16.
 */
public abstract class PuzzlePiece {
    Matrix mMatrix;
    private Border mBorder;
    private Matrix mDownMatrix;
    private float mTranslateX = 0f;
    private float mTranslateY = 0f;
    private float mScaleFactor = 0f;

    private boolean mNeedHorizontalFlip = false;
    private boolean mNeedVerticalFlip = false;

    private float mRotation = 0f;

    public PuzzlePiece(Matrix matrix, Border border) {
        mMatrix = matrix;
        mBorder = border;
        mDownMatrix = new Matrix();
    }

    public float getRotation() {
        return mRotation;
    }

    public void setRotation(float rotation) {
        mRotation = rotation;
    }

    public boolean isNeedHorizontalFlip() {
        return mNeedHorizontalFlip;
    }

    public void setNeedHorizontalFlip(boolean needHorizontalFlip) {
        mNeedHorizontalFlip = needHorizontalFlip;
    }

    public boolean isNeedVerticalFlip() {
        return mNeedVerticalFlip;
    }

    public void setNeedVerticalFlip(boolean needVerticalFlip) {
        mNeedVerticalFlip = needVerticalFlip;
    }

    public float getTranslateX() {
        return mTranslateX;
    }

    public void setTranslateX(float translateX) {
        mTranslateX = translateX;
    }

    public float getTranslateY() {
        return mTranslateY;
    }

    public void setTranslateY(float translateY) {
        mTranslateY = translateY;
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

    public float getScaleFactor() {
        return mScaleFactor;
    }

    public void setScaleFactor(float scaleFactor) {
        mScaleFactor = scaleFactor;
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

    public float getMappedWidth() {
        return getMappedBound().width();
    }

    public float getMappedHeight() {
        return getMappedBound().height();
    }

    public boolean contains(float x, float y) {
        return mBorder.getRect().contains(x, y);
    }

    public boolean isFilledBorder() {
        RectF rectF = getMappedBound();
        return !(rectF.left > mBorder.left()
                || rectF.top > mBorder.top()
                || rectF.right < mBorder.right()
                || rectF.bottom < mBorder.bottom());
    }

    public void release() {
        if (mMatrix != null) {
            mMatrix.reset();
            mMatrix = null;
        }

        if (mDownMatrix != null) {
            mDownMatrix.reset();
            mDownMatrix = null;
        }
    }
}
