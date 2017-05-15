package com.xiaopo.flying.puzzle.slant;

import android.graphics.PointF;

/**
 * @author wupanjie
 */

public class CrossoverPointF extends PointF {
  private final SlantLine horizontal;
  private final SlantLine vertical;

  public CrossoverPointF(SlantLine horizontal, SlantLine vertical) {
    this.horizontal = horizontal;
    this.vertical = vertical;
  }

  public void update() {
    SlantUtils.intersectionOfLines(this, horizontal, vertical);
  }
}
