package com.xiaopo.flying.photolayout.layout;

import com.xiaopo.flying.puzzle.Line;

/**
 * Created by snowbean on 16-8-17.
 */
public class FivePieceLayout extends NumberPieceLayout {

  public FivePieceLayout(int theme) {
    super(theme);
  }

  @Override public int getThemeCount() {
    return 17;
  }

  @Override public void layout() {
    switch (mTheme) {
      case 0:
        cutBorderEqualPart(0, 5, Line.Direction.HORIZONTAL);
        break;
      case 1:
        cutBorderEqualPart(0, 5, Line.Direction.VERTICAL);
        break;
      case 2:
        addLine(0, Line.Direction.HORIZONTAL, 2f / 5);
        addLine(0, Line.Direction.VERTICAL, 1f / 2);
        cutBorderEqualPart(2, 3, Line.Direction.VERTICAL);
        break;
      case 3:
        addLine(0, Line.Direction.HORIZONTAL, 3f / 5);
        cutBorderEqualPart(0, 3, Line.Direction.VERTICAL);
        addLine(3, Line.Direction.VERTICAL, 1f / 2);
        break;
      case 4:
        addLine(0, Line.Direction.VERTICAL, 2f / 5);
        cutBorderEqualPart(0, 3, Line.Direction.HORIZONTAL);
        addLine(1, Line.Direction.HORIZONTAL, 1f / 2);
        break;
      case 5:
        addLine(0, Line.Direction.VERTICAL, 2f / 5);
        cutBorderEqualPart(1, 3, Line.Direction.HORIZONTAL);
        addLine(0, Line.Direction.HORIZONTAL, 1f / 2);
        break;
      case 6:
        addLine(0, Line.Direction.HORIZONTAL, 3f / 4);
        cutBorderEqualPart(1, 4, Line.Direction.VERTICAL);
        break;
      case 7:
        addLine(0, Line.Direction.HORIZONTAL, 1f / 4);
        cutBorderEqualPart(0, 4, Line.Direction.VERTICAL);
        break;
      case 8:
        addLine(0, Line.Direction.VERTICAL, 3f / 4);
        cutBorderEqualPart(1, 4, Line.Direction.HORIZONTAL);
        break;
      case 9:
        addLine(0, Line.Direction.VERTICAL, 1f / 4);
        cutBorderEqualPart(0, 4, Line.Direction.HORIZONTAL);
        break;
      case 10:
        addLine(0, Line.Direction.HORIZONTAL, 1f / 4);
        addLine(1, Line.Direction.HORIZONTAL, 2f / 3);
        addLine(0, Line.Direction.VERTICAL, 1f / 2);
        addLine(3, Line.Direction.VERTICAL, 1f / 2);
        break;
      case 11:
        addLine(0, Line.Direction.VERTICAL, 1f / 4);
        addLine(1, Line.Direction.VERTICAL, 2f / 3);
        addLine(0, Line.Direction.HORIZONTAL, 1f / 2);
        addLine(2, Line.Direction.HORIZONTAL, 1f / 2);
        break;
      case 12:
        addCross(0, 1f / 3);
        addLine(2, Line.Direction.HORIZONTAL, 1f / 2);
        break;
      case 13:
        addCross(0, 2f / 3);
        addLine(1, Line.Direction.HORIZONTAL, 1f / 2);
        break;
      case 14:
        addCross(0, 1f / 3, 2f / 3);
        addLine(3, Line.Direction.HORIZONTAL, 1f / 2);
        break;
      case 15:
        addCross(0, 2f / 3, 1f / 3);
        addLine(0, Line.Direction.HORIZONTAL, 1f / 2);
        break;
      case 16:
        cutSpiral(0);
        break;
      default:
        cutBorderEqualPart(0, 5, Line.Direction.HORIZONTAL);
        break;
    }
  }
}
