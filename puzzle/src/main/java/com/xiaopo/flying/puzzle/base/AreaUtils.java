package com.xiaopo.flying.puzzle.base;

import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

/**
 * @author wupanjie
 */

public class AreaUtils {
  private AreaUtils() {
    //no instance
  }

  public static float getMinMatrixScale(PuzzlePiece piece) {
    return MatrixUtils.getMatrixScale(generateMatrix(piece, 0));
  }

  public static Matrix generateMatrix(PuzzlePiece piece, float extra) {
    return generateMatrix(piece.getArea(), piece.getDrawable(), extra);
  }

  public static Matrix generateMatrix(Area area, Drawable drawable, float extraSize) {
    return generateCenterCropMatrix(area, drawable.getIntrinsicWidth(),
        drawable.getIntrinsicHeight(), extraSize);
  }

  private static Matrix generateCenterCropMatrix(Area area, int width, int height,
      float extraSize) {
    final RectF rectF = area.getAreaRect();

    Matrix matrix = new Matrix();

    float offsetX = rectF.centerX() - width / 2;
    float offsetY = rectF.centerY() - height / 2;

    matrix.postTranslate(offsetX, offsetY);

    float scale;

    if (width * rectF.height() > rectF.width() * height) {
      scale = (rectF.height() + extraSize) / height;
    } else {
      scale = (rectF.width() + extraSize) / width;
    }

    matrix.postScale(scale, scale, rectF.centerX(), rectF.centerY());

    return matrix;
  }

  public static float getMinFillScale(Area area, Drawable drawable) {
    final RectF rectF = area.getAreaRect();
    final float width = drawable.getIntrinsicWidth();
    final float height = drawable.getIntrinsicHeight();
    float scale;

    if (width * rectF.height() > rectF.width() * height) {
      scale = rectF.height() / height;
    } else {
      scale = rectF.width() / width;
    }

    return scale;
  }
}
