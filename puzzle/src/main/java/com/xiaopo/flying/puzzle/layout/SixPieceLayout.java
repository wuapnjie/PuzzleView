package com.xiaopo.flying.puzzle.layout;

import com.xiaopo.flying.puzzle.Line;

/**
 * Created by snowbean on 16-8-17.
 */
public class SixPieceLayout extends NumberPieceLayout {

    public SixPieceLayout(int theme) {
        super(theme);
    }

    @Override
    public int getThemeCount() {
        return 12;
    }

    @Override
    public void layout() {
        switch (mTheme) {
            case 0:
                cutBorderEqualPart(getOuterBorder(), 2, 1);
                break;

            case 1:
                cutBorderEqualPart(getOuterBorder(), 1, 2);
                break;
            case 2:
                addCross(getOuterBorder(), 2f / 3, 1f / 2);
                addLine(getBorder(3), Line.Direction.VERTICAL, 1f / 2);
                addLine(getBorder(2), Line.Direction.VERTICAL, 1f / 2);
                break;
            case 3:
                addCross(getOuterBorder(), 1f / 2, 2f / 3);
                addLine(getBorder(3), Line.Direction.HORIZONTAL, 1f / 2);
                addLine(getBorder(1), Line.Direction.HORIZONTAL, 1f / 2);
                break;
            case 4:
                addCross(getOuterBorder(), 1f / 2, 1f / 3);
                addLine(getBorder(2), Line.Direction.HORIZONTAL, 1f / 2);
                addLine(getBorder(0), Line.Direction.HORIZONTAL, 1f / 2);
                break;
            case 5:
                addCross(getOuterBorder(), 1f / 3, 1f / 2);
                addLine(getBorder(1), Line.Direction.VERTICAL, 1f / 2);
                addLine(getBorder(0), Line.Direction.VERTICAL, 1f / 2);
                break;
            case 6:
                addLine(getOuterBorder(), Line.Direction.HORIZONTAL, 4f / 5);
                cutBorderEqualPart(getBorder(1), 5, Line.Direction.VERTICAL);
                break;
            case 7:
                addLine(getOuterBorder(), Line.Direction.HORIZONTAL, 1f / 4);
                addLine(getBorder(1), Line.Direction.HORIZONTAL, 2f / 3);
                addLine(getBorder(1), Line.Direction.VERTICAL, 1f / 4);
                addLine(getBorder(2), Line.Direction.VERTICAL, 2f / 3);
                addLine(getBorder(4), Line.Direction.VERTICAL, 1f / 2);
                break;
            case 8:
                addCross(getOuterBorder(), 1f / 3);
                addLine(getBorder(1), Line.Direction.VERTICAL, 1f / 2);
                addLine(getBorder(4), Line.Direction.HORIZONTAL, 1f / 2);
                break;
            case 9:
                addCross(getOuterBorder(), 2f / 3, 1f / 3);
                addLine(getBorder(3), Line.Direction.VERTICAL, 1f / 2);
                addLine(getBorder(0), Line.Direction.HORIZONTAL, 1f / 2);
                break;
            case 10:
                addCross(getOuterBorder(), 2f / 3);
                addLine(getBorder(2), Line.Direction.VERTICAL, 1f / 2);
                addLine(getBorder(1), Line.Direction.HORIZONTAL, 1f / 2);
                break;
            case 11:
                addCross(getOuterBorder(), 1f / 3, 2f / 3);
                addLine(getBorder(3), Line.Direction.HORIZONTAL, 1f / 2);
                addLine(getBorder(0), Line.Direction.VERTICAL, 1f / 2);
                break;
            case 12:
                addCross(getOuterBorder(), 1f / 3);
                addLine(getBorder(2), Line.Direction.HORIZONTAL, 1f / 2);
                addLine(getBorder(1), Line.Direction.VERTICAL, 1f / 2);
                break;
            default:
                addCross(getOuterBorder(), 2f / 3, 1f / 2);
                addLine(getBorder(3), Line.Direction.VERTICAL, 1f / 2);
                addLine(getBorder(2), Line.Direction.VERTICAL, 1f / 2);
                break;
        }
    }
}
