package com.xiaopo.flying.photolayout.slant;

import com.xiaopo.flying.puzzle.slant.Line;
import com.xiaopo.flying.puzzle.slant.SlantLayout;

/**
 * @author wupanjie
 */

public class SlantSampleLayout extends SlantLayout {
  @Override public void layout() {
    addLine(0, Line.Direction.VERTICAL, 0.4f, 0.6f);
    addLine(1, Line.Direction.HORIZONTAL, 0.5f, 0.4f);
    addLine(0, Line.Direction.HORIZONTAL , 0.5f ,0.4f);
    addLine(2, Line.Direction.HORIZONTAL, 0.4f, 0.6f);
  }
}
