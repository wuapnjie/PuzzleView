package com.xiaopo.flying.photolayout.layout;

import com.xiaopo.flying.puzzle.Line;

/**
 * Created by snowbean on 16-8-17.
 */
public class SevenPieceLayout extends NumberPieceLayout {
  public SevenPieceLayout(int theme) {
    super(theme);
  }

  @Override public int getThemeCount() {
    return 9;
  }

  @Override public void layout() {
    switch (mTheme) {
      case 0:
        addLine(0, Line.Direction.HORIZONTAL, 1f / 2);
        cutBorderEqualPart(1, 4, Line.Direction.VERTICAL);
        cutBorderEqualPart(0, 3, Line.Direction.VERTICAL);
        break;
      case 1:
        addLine(0, Line.Direction.VERTICAL, 1f / 2);
        cutBorderEqualPart(1, 4, Line.Direction.HORIZONTAL);
        cutBorderEqualPart(0, 3, Line.Direction.HORIZONTAL);
        break;
      case 2:
        addLine(0, Line.Direction.HORIZONTAL, 1f / 2);
        cutBorderEqualPart(1, 1, 2);
        break;
      case 3:
        addLine(0, Line.Direction.HORIZONTAL, 2f / 3);
        cutBorderEqualPart(1, 3, Line.Direction.VERTICAL);
        addCross(0, 1f / 2);
        break;
      case 4:
        cutBorderEqualPart(0, 3, Line.Direction.VERTICAL);
        cutBorderEqualPart(2, 3, Line.Direction.HORIZONTAL);
        cutBorderEqualPart(0, 3, Line.Direction.HORIZONTAL);
        break;
      case 5:
        addLine(0, Line.Direction.HORIZONTAL, 3f / 5);
        addLine(1, Line.Direction.VERTICAL, 3f / 4);
        addLine(0, Line.Direction.HORIZONTAL, 1f / 2);
        addLine(1, Line.Direction.VERTICAL, 2f / 5);
        cutBorderEqualPart(0, 3, Line.Direction.VERTICAL);
        break;
      case 6:
        addLine(0, Line.Direction.VERTICAL, 3f / 5);
        addLine(1, Line.Direction.HORIZONTAL, 3f / 4);
        addLine(0, Line.Direction.VERTICAL, 1f / 2);
        addLine(1, Line.Direction.HORIZONTAL, 2f / 5);
        cutBorderEqualPart(0, 3, Line.Direction.HORIZONTAL);
        break;
      case 7:
        addLine(0, Line.Direction.VERTICAL, 1f / 4);
        addLine(1, Line.Direction.VERTICAL, 2f / 3);
        addLine(2, Line.Direction.HORIZONTAL, 1f / 2);
        addLine(1, Line.Direction.HORIZONTAL, 3f / 4);
        addLine(1, Line.Direction.HORIZONTAL, 1f / 3);
        addLine(0, Line.Direction.HORIZONTAL, 1f / 2);
        break;
      case 8:
        addLine(0, Line.Direction.HORIZONTAL, 1f / 4);
        addLine(1, Line.Direction.HORIZONTAL, 2f / 3);
        cutBorderEqualPart(2, 3, Line.Direction.VERTICAL);
        cutBorderEqualPart(0, 3, Line.Direction.VERTICAL);
        break;
      default:
        break;
    }
  }
}
