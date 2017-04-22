package com.xiaopo.flying.puzzle.slant;

import android.graphics.Path;
import android.graphics.PointF;

/**
 * @author wupanjie
 */

public interface Area {
  boolean contains(PointF point);

  boolean contains(float x, float y);

  Path getAreaPath();
}
