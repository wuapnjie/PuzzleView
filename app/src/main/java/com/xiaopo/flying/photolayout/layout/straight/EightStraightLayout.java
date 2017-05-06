package com.xiaopo.flying.photolayout.layout.straight;

import com.xiaopo.flying.puzzle.Line;

/**
 * @author wupanjie
 */
public class EightStraightLayout extends NumberStraightLayout {
  public EightStraightLayout(int theme) {
    super(theme);
  }

  @Override public int getThemeCount() {
    return 11;
  }

  @Override public void layout() {
    switch (theme) {
      case 0:
        cutBorderEqualPart(0, 3, 1);
        break;
      case 1:
        cutBorderEqualPart(0, 1, 3);
        break;
      case 2:
        cutBorderEqualPart(0, 4, Line.Direction.VERTICAL);
        addLine(3, Line.Direction.HORIZONTAL, 4f / 5);
        addLine(2, Line.Direction.HORIZONTAL, 3f / 5);
        addLine(1, Line.Direction.HORIZONTAL, 2f / 5);
        addLine(0, Line.Direction.HORIZONTAL, 1f / 5);
        break;
      case 3:
        cutBorderEqualPart(0, 4, Line.Direction.HORIZONTAL);
        addLine(3, Line.Direction.VERTICAL, 4f / 5);
        addLine(2, Line.Direction.VERTICAL, 3f / 5);
        addLine(1, Line.Direction.VERTICAL, 2f / 5);
        addLine(0, Line.Direction.VERTICAL, 1f / 5);
        break;
      case 4:
        cutBorderEqualPart(0, 4, Line.Direction.VERTICAL);
        addLine(3, Line.Direction.HORIZONTAL, 1f / 5);
        addLine(2, Line.Direction.HORIZONTAL, 2f / 5);
        addLine(1, Line.Direction.HORIZONTAL, 3f / 5);
        addLine(0, Line.Direction.HORIZONTAL, 4f / 5);
        break;
      case 5:
        cutBorderEqualPart(0, 4, Line.Direction.HORIZONTAL);
        addLine(3, Line.Direction.VERTICAL, 1f / 5);
        addLine(2, Line.Direction.VERTICAL, 2f / 5);
        addLine(1, Line.Direction.VERTICAL, 3f / 5);
        addLine(0, Line.Direction.VERTICAL, 4f / 5);
        break;
      case 6:
        cutBorderEqualPart(0, 3, Line.Direction.HORIZONTAL);
        cutBorderEqualPart(2, 3, Line.Direction.VERTICAL);
        cutBorderEqualPart(1, 2, Line.Direction.VERTICAL);
        cutBorderEqualPart(0, 3, Line.Direction.VERTICAL);
        break;
      case 7:
        cutBorderEqualPart(0, 3, Line.Direction.VERTICAL);
        cutBorderEqualPart(2, 3, Line.Direction.HORIZONTAL);
        cutBorderEqualPart(1, 2, Line.Direction.HORIZONTAL);
        cutBorderEqualPart(0, 3, Line.Direction.HORIZONTAL);
        break;
      case 8:
        addLine(0, Line.Direction.HORIZONTAL, 4f / 5);
        cutBorderEqualPart(1, 5, Line.Direction.VERTICAL);
        addLine(0, Line.Direction.HORIZONTAL, 1f / 2);
        addLine(1, Line.Direction.VERTICAL, 1f / 2);
        break;
      case 9:
        cutBorderEqualPart(0, 3, Line.Direction.HORIZONTAL);
        cutBorderEqualPart(2, 2, Line.Direction.VERTICAL);
        cutBorderEqualPart(1, 3, Line.Direction.VERTICAL);
        addLine(0, Line.Direction.VERTICAL, 3f / 4);
        addLine(0, Line.Direction.VERTICAL, 1f / 3);
        break;
      case 10:
        cutBorderEqualPart(0, 2, 1);
        addLine(5, Line.Direction.VERTICAL, 1f / 2);
        addLine(4, Line.Direction.VERTICAL, 1f / 2);
        break;
      default:
        break;
    }
  }
}
