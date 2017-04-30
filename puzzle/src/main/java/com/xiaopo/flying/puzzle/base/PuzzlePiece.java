package com.xiaopo.flying.puzzle.base;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * @author wupanjie
 */

public class PuzzlePiece {
  private static final String TAG = "PuzzlePiece";
  private Drawable drawable;
  private Matrix matrix;
  private Matrix previousMatrix;
  private Area area;
  private Rect drawableBounds;

  private ValueAnimator animator;

  private float previousMoveX;
  private float previousMoveY;

  public PuzzlePiece(Drawable drawable, Area area, Matrix matrix) {
    this.drawable = drawable;
    this.area = area;
    this.matrix = matrix;
    this.previousMatrix = new Matrix();
    this.drawableBounds = new Rect(0, 0, getWidth(), getHeight());

    this.animator = ValueAnimator.ofFloat(0f, 1f);
    this.animator.setInterpolator(new DecelerateInterpolator());
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

  public Matrix getPreviousMatrix() {
    return previousMatrix;
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

  public void setPreviousMoveX(float previousMoveX) {
    this.previousMoveX = previousMoveX;
  }

  public void setPreviousMoveY(float previousMoveY) {
    this.previousMoveY = previousMoveY;
  }

  public float getPreviousMoveX() {
    return previousMoveX;
  }

  public float getPreviousMoveY() {
    return previousMoveY;
  }

  public boolean isFilledArea() {
    RectF bounds = getMappedBounds();
    return !(bounds.left > area.left()
        || bounds.top > area.top()
        || bounds.right < area.right()
        || bounds.bottom < area.bottom());
  }

  public boolean canFilledArea() {
    float scale = MatrixUtils.getMatrixScale(matrix);
    float minScale = AreaUtils.getMinMatrixScale(this);
    return scale >= minScale;
  }

  public void prepare() {
    previousMatrix.set(matrix);
  }

  public void translate(float offsetX, float offsetY) {
    matrix.set(previousMatrix);
    postTranslate(offsetX, offsetY);
  }

  public void zoom(float scaleX, float scaleY, PointF midPoint) {
    matrix.set(previousMatrix);
    postScale(scaleX, scaleY, midPoint);
  }

  public void zoomAndTranslate(float scaleX, float scaleY, PointF midPoint, float offsetX,
      float offsetY) {
    matrix.set(previousMatrix);
    postTranslate(offsetX, offsetY);
    postScale(scaleX, scaleY, midPoint);
  }

  public void set(Matrix matrix) {
    this.matrix.set(matrix);
  }

  public void postTranslate(float x, float y) {
    this.matrix.postTranslate(x, y);
  }

  public void postScale(float scaleX, float scaleY, PointF midPoint) {
    this.matrix.postScale(scaleX, scaleY, midPoint.x, midPoint.y);
  }
}
