package com.xiaopo.flying.photolayout.layout;

import com.xiaopo.flying.puzzle.Line;

/**
 * Created by snowbean on 16-8-17.
 */
public class FourPieceLayout extends NumberPieceLayout {
  private static final String TAG = "FourPieceLayout";

  public FourPieceLayout(int theme) {
    super(theme);
  }

  @Override public int getThemeCount() {
    return 8;
  }

  @Override public void layout() {
    switch (mTheme) {
      case 0:
        cutBorderEqualPart(0, 4, Line.Direction.HORIZONTAL);
        break;
      case 1:
        cutBorderEqualPart(0, 4, Line.Direction.VERTICAL);
        break;
      case 2:
        addCross(0, 1f / 2);
        break;
      case 3:
        addLine(0, Line.Direction.HORIZONTAL, 1f / 3);
        cutBorderEqualPart(0, 3, Line.Direction.VERTICAL);
        break;
      case 4:
        addLine(0, Line.Direction.HORIZONTAL, 2f / 3);
        cutBorderEqualPart(1, 3, Line.Direction.VERTICAL);
        break;
      case 5:
        addLine(0, Line.Direction.VERTICAL, 1f / 3);
        cutBorderEqualPart(0, 3, Line.Direction.HORIZONTAL);
        break;
      case 6:
        addLine(0, Line.Direction.VERTICAL, 2f / 3);
        cutBorderEqualPart(1, 3, Line.Direction.HORIZONTAL);
        break;
      case 7:
        addLine(0, Line.Direction.VERTICAL, 1f / 2);
        addLine(1, Line.Direction.HORIZONTAL, 2f / 3);
        addLine(1, Line.Direction.HORIZONTAL, 1f / 3);
        break;
      default:
        cutBorderEqualPart(0, 4, Line.Direction.HORIZONTAL);
        break;
    }
  }
}
