package com.xiaopo.flying.puzzle.slant;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * @author wupanjie
 */

public class SlantPuzzlePiece {
  private static final String TAG = "SlantPuzzlePiece";
  private Drawable drawable;
  private Matrix matrix;
  private Matrix previousMatrix;
  private Area area;
  private Rect drawableBounds;

  public SlantPuzzlePiece(Drawable drawable, Area area, Matrix matrix) {
    this.drawable = drawable;
    this.area = area;
    this.matrix = matrix;
    this.previousMatrix = new Matrix();
    this.drawableBounds = new Rect(0, 0, getWidth(), getHeight());
  }

  public void draw(Canvas canvas) {
    draw(canvas, 255, true);
  }

  public void draw(Canvas canvas, int alpha) {
    draw(canvas, alpha, false);
  }

  public void draw(Canvas canvas, int alpha, boolean needClip) {
    canvas.save();

    if (needClip) {
      canvas.clipPath(area.getAreaPath());
    }
    canvas.concat(matrix);
    drawable.setBounds(drawableBounds);
    drawable.setAlpha(alpha);
    drawable.draw(canvas);

    canvas.restore();
  }

  public Area getArea() {
    return area;
  }

  public void setDrawable(Drawable drawable) {
    this.drawable = drawable;
    this.drawableBounds = new Rect(0, 0, getWidth(), getHeight());
  }

  public Matrix getMatrix() {
    return matrix;
  }

  public Drawable getDrawable() {
    return drawable;
  }

  public int getWidth() {
    return drawable.getIntrinsicWidth();
  }

  public int getHeight() {
    return drawable.getIntrinsicHeight();
  }

  public boolean contains(float x, float y) {
    return area.contains(x, y);
  }

  public boolean contains(Line line) {
    return area.contains(line);
  }

  public RectF getMappedBounds() {
    RectF dst = new RectF();
    matrix.mapRect(dst, new RectF(drawableBounds));
    return dst;
  }

  public PointF getMappedCenterPoint() {
    float[] dst = new float[2];
    float[] src = { area.centerX(), area.centerY() };
    matrix.mapPoints(dst, src);

    return new PointF(dst[0], dst[1]);
  }

  public boolean isFilledArea() {
    RectF bounds = getMappedBounds();
    return !(bounds.left > area.left()
        || bounds.top > area.top()
        || bounds.right < area.right()
        || bounds.bottom < area.bottom());
  }

  public void prepare() {
    previousMatrix.set(matrix);
  }

  public void translate(float offsetX, float offsetY) {
    matrix.set(previousMatrix);
    matrix.postTranslate(offsetX, offsetY);
  }

  public void zoom(float scaleX, float scaleY, PointF midPoint) {
    matrix.set(previousMatrix);
    matrix.postScale(scaleX, scaleY, midPoint.x, midPoint.y);
  }

  public void set(Matrix matrix) {
    this.matrix.set(matrix);
  }

  public void translate(final View view, final float offsetX, final float offsetY, int duration) {
    ValueAnimator animator = ValueAnimator.ofFloat(0, 1f);
    animator.setDuration(duration);
    animator.setInterpolator(new DecelerateInterpolator());
    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator animation) {
        float x = offsetX * (float) animation.getAnimatedValue();
        float y = offsetY * (float) animation.getAnimatedValue();

        translate(x, y);
        view.invalidate();
      }
    });

    animator.start();
  }

  public void zoom(final View view, final float scaleX, final float scaleY, final PointF midPoint,
      int duration) {
    ValueAnimator animator = ValueAnimator.ofFloat(0, 1f);
    animator.setDuration(duration);
    animator.setInterpolator(new DecelerateInterpolator());
    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator animation) {
        float x = scaleX * (float) animation.getAnimatedValue();
        float y = scaleY * (float) animation.getAnimatedValue();

        Log.d(TAG, "onAnimationUpdate: midPoint --> " + midPoint.toString());
        zoom(x, y, midPoint);
        view.invalidate();
      }
    });

    animator.start();
  }
}
