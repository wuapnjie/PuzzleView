package com.xiaopo.flying.puzzle.base;

import android.graphics.Matrix;

/**
 * @author wupanjie
 */

public class MatrixUtils {
  private MatrixUtils() {
    //no instance
  }

  private static final float[] sMatrixValues = new float[9];

  /**
   * This method calculates scale value for given Matrix object.
   */
  public static float getMatrixScale(Matrix matrix) {
    return (float) Math.sqrt(Math.pow(getMatrixValue(matrix, Matrix.MSCALE_X), 2)
        + Math.pow(getMatrixValue(matrix, Matrix.MSKEW_Y), 2));
  }

  /**
   * This method calculates rotation angle for given Matrix object.
   */
  private static float getMatrixAngle( Matrix matrix) {
    return (float) -(Math.atan2(getMatrixValue(matrix, Matrix.MSKEW_X),
        getMatrixValue(matrix, Matrix.MSCALE_X)) * (180 / Math.PI));
  }

  private static float getMatrixValue(Matrix matrix, int valueIndex) {
    matrix.getValues(sMatrixValues);
    return sMatrixValues[valueIndex];
  }
}
