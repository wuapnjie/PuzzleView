package com.xiaopo.flying.puzzle;

import android.graphics.RectF;
import java.util.List;

/**
 * @author wupanjie
 */
public interface PuzzleLayout {
  void setOuterBounds(RectF bounds);

  void layout();

  int getAreaCount();

  List<Line> getOuterLines();

  List<Line> getLines();

  Area getOuterArea();

  void update();

  void reset();

  Area getArea(int position);

  float width();

  float height();
}
