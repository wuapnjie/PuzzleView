package com.xiaopo.flying.photolayout.layout;

import com.xiaopo.flying.puzzle.Line;

/**
 * Created by snowbean on 16-8-17.
 */
public class OnePieceLayout extends NumberPieceLayout {

  public OnePieceLayout(int theme) {
    super(theme);
  }

  public OnePieceLayout(float radio, int theme) {
    super(theme);
  }

  @Override public int getThemeCount() {
    return 6;
  }

  @Override public void layout() {
    switch (mTheme) {
      case 0:
        addLine(0, Line.Direction.HORIZONTAL, 1f / 2);
        break;
      case 1:
        addLine(0, Line.Direction.VERTICAL, 1f / 2);
        break;
      case 2:
        addCross(0, 1f / 2);
        break;
      case 3:
        cutBorderEqualPart(0, 2, 1);
        break;
      case 4:
        cutBorderEqualPart(0, 1, 2);
        break;
      case 5:
        cutBorderEqualPart(0, 2, 2);
        break;
      default:
        addLine(0, Line.Direction.HORIZONTAL, 1f / 2);
        break;
    }
  }
}
