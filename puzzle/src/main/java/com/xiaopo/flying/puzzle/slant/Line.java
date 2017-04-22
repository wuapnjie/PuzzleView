package com.xiaopo.flying.puzzle.slant;

import android.graphics.PointF;

/**
 * @author wupanjie
 */

public interface Line {
  enum Direction {
    HORIZONTAL, VERTICAL
  }

  float length();

  PointF startPoint();

  PointF endPoint();

  Direction direction();

  float slope();

  boolean contains(float x, float y, float extra);

  void prepareMove();

  void move(float offset, float extra);

  void update();
}
