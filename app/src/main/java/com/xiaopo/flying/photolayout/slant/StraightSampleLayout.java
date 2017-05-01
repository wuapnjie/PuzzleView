package com.xiaopo.flying.photolayout.slant;

import com.xiaopo.flying.puzzle.Line;
import com.xiaopo.flying.puzzle.square.StraightPuzzleLayout;

/**
 * @author wupanjie
 */

public class StraightSampleLayout extends StraightPuzzleLayout {
  @Override public void layout() {
    addLine(0, Line.Direction.HORIZONTAL, 0.5f);
    //addLine(0, Line.Direction.VERTICAL, 0.67f);
    //addLine(0, Line.Direction.VERTICAL, 0.5f);
  }
}
