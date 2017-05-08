package com.xiaopo.flying.puzzle;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import static com.xiaopo.flying.puzzle.MatrixUtils.calculateImageIndents;
import static com.xiaopo.flying.puzzle.MatrixUtils.getMinMatrixScale;
import static com.xiaopo.flying.puzzle.MatrixUtils.judgeIsImageContainsBorder;

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
  private float[] drawablePoints;
  private float[] mappedDrawablePoints;

  private float previousMoveX;
  private float previousMoveY;

  private final RectF mappedBounds;
  private final PointF centerPoint;
  private final PointF mappedCenterPoint;

  private ValueAnimator animator;
  private int duration = 300;
  private Matrix tempMatrix;

  public PuzzlePiece(Drawable drawable, Area area, Matrix matrix) {
    this.drawable = drawable;
    this.area = area;
    this.matrix = matrix;
    this.previousMatrix = new Matrix();
    this.drawableBounds = new Rect(0, 0, getWidth(), getHeight());
    this.drawablePoints = new float[] {
        0f, 0f, getWidth(), 0f, getWidth(), getHeight(), 0f, getHeight()
    };
    this.mappedDrawablePoints = new float[8];

    this.mappedBounds = new RectF();
    this.centerPoint = new PointF(area.centerX(), area.centerY());
    this.mappedCenterPoint = new PointF();

    this.animator = ValueAnimator.ofFloat(0f, 1f);
    this.animator.setInterpolator(new DecelerateInterpolator());

    this.tempMatrix = new Matrix();
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
    this.drawablePoints = new float[] {
        0f, 0f, getWidth(), 0f, getWidth(), getHeight(), 0f, getHeight()
    };
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

  public Rect getDrawableBounds() {
    return drawableBounds;
  }

  public RectF getCurrentDrawableBounds() {
    matrix.mapRect(mappedBounds, new RectF(drawableBounds));
    return mappedBounds;
  }

  public PointF getCurrentDrawableCenterPoint() {
    getCurrentDrawableBounds();
    mappedCenterPoint.x = mappedBounds.centerX();
    mappedCenterPoint.y = mappedBounds.centerY();
    return mappedCenterPoint;
  }

  public PointF getAreaCenterPoint() {
    centerPoint.x = area.centerX();
    centerPoint.y = area.centerY();
    return centerPoint;
  }

  public float getMatrixScale() {
    return MatrixUtils.getMatrixScale(matrix);
  }

  public float getMatrixAngle() {
    return MatrixUtils.getMatrixAngle(matrix);
  }

  public float[] getCurrentDrawablePoints() {
    matrix.mapPoints(mappedDrawablePoints, drawablePoints);
    return mappedDrawablePoints;
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
    RectF bounds = getCurrentDrawableBounds();
    return !(bounds.left > area.left()
        || bounds.top > area.top()
        || bounds.right < area.right()
        || bounds.bottom < area.bottom());
  }

  public boolean canFilledArea() {
    float scale = MatrixUtils.getMatrixScale(matrix);
    float minScale = MatrixUtils.getMinMatrixScale(this);
    return scale >= minScale;
  }

  public void record() {
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
    moveToFillArea(null);
  }

  public void postTranslate(float x, float y) {
    this.matrix.postTranslate(x, y);
  }

  public void postScale(float scaleX, float scaleY, PointF midPoint) {
    this.matrix.postScale(scaleX, scaleY, midPoint.x, midPoint.y);
  }

  public void postFlipVertically() {
    this.matrix.postScale(1, -1, area.centerX(), area.centerY());
  }

  public void postFlipHorizontally() {
    this.matrix.postScale(-1, 1, area.centerX(), area.centerY());
  }

  public void postRotate(float degree) {
    this.matrix.postRotate(degree, area.centerX(), area.centerY());

    float minScale = getMinMatrixScale(this);
    if (getMatrixScale() < minScale) {
      final PointF midPoint = new PointF();
      midPoint.set(getCurrentDrawableCenterPoint());

      postScale(minScale / getMatrixScale(), minScale / getMatrixScale(), midPoint);
    }

    if (!judgeIsImageContainsBorder(this, getMatrixAngle())) {
      final float[] imageIndents = calculateImageIndents(this);
      float deltaX = -(imageIndents[0] + imageIndents[2]);
      float deltaY = -(imageIndents[1] + imageIndents[3]);

      postTranslate(deltaX, deltaY);
    }
  }

  public void animateTranslate(final View view, final float translateX, final float translateY) {
    animator.end();
    animator.removeAllUpdateListeners();
    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator animation) {
        float x = translateX * (float) animation.getAnimatedValue();
        float y = translateY * (float) animation.getAnimatedValue();

        translate(x, y);
        view.invalidate();
      }
    });
    animator.setDuration(duration);
    animator.start();
  }

  public void moveToFillArea(final View view) {
    if (isFilledArea()) return;
    record();

    RectF rectF = getCurrentDrawableBounds();
    float offsetX = 0f;
    float offsetY = 0f;

    if (rectF.left > area.left()) {
      offsetX = area.left() - rectF.left;
    }

    if (rectF.top > area.top()) {
      offsetY = area.top() - rectF.top;
    }

    if (rectF.right < area.right()) {
      offsetX = area.right() - rectF.right;
    }

    if (rectF.bottom < area.bottom()) {
      offsetY = area.bottom() - rectF.bottom;
    }

    if (view == null) {
      postTranslate(offsetX, offsetY);
    } else {
      animateTranslate(view, offsetX, offsetY);
    }
  }

  public void fillArea(final View view, boolean quick) {
    if (isFilledArea()) return;
    if (quick) {
      set(MatrixUtils.generateMatrix(this, 0f));
    } else {
      record();

      final float startScale = getMatrixScale();
      final float endScale = MatrixUtils.getMinMatrixScale(this);

      final PointF midPoint = new PointF();
      midPoint.set(getCurrentDrawableCenterPoint());

      tempMatrix.set(matrix);
      tempMatrix.postScale(endScale / startScale, endScale / startScale, midPoint.x, midPoint.y);

      RectF rectF = new RectF(drawableBounds);
      tempMatrix.mapRect(rectF);

      float offsetX = 0f;
      float offsetY = 0f;

      if (rectF.left > area.left()) {
        offsetX = area.left() - rectF.left;
      }

      if (rectF.top > area.top()) {
        offsetY = area.top() - rectF.top;
      }

      if (rectF.right < area.right()) {
        offsetX = area.right() - rectF.right;
      }

      if (rectF.bottom < area.bottom()) {
        offsetY = area.bottom() - rectF.bottom;
      }

      final float translateX = offsetX;
      final float translateY = offsetY;

      animator.end();
      animator.removeAllUpdateListeners();
      animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
        @Override public void onAnimationUpdate(ValueAnimator animation) {
          float value = (float) animation.getAnimatedValue();
          float scale = (startScale + (endScale - startScale) * value) / startScale;
          float x = translateX * value;
          float y = translateY * value;

          zoom(scale, scale, midPoint);
          postTranslate(x, y);
          view.invalidate();
        }
      });

      animator.setDuration(duration);
      animator.start();
    }
  }

  public void setArea(Area area) {
    this.area = area;
  }

  public boolean isAnimateRunning() {
    return animator.isRunning();
  }

  public void setAnimateDuration(int duration) {
    this.duration = duration;
  }
}
