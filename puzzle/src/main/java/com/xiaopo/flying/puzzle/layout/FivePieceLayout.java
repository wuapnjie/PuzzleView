package com.xiaopo.flying.puzzle.layout;

import com.xiaopo.flying.puzzle.Line;

/**
 * Created by snowbean on 16-8-17.
 */
public class FivePieceLayout extends NumberPieceLayout {

    public FivePieceLayout(int theme) {
        super(theme);
    }

    @Override
    public int getThemeCount() {
        return 17;
    }

    @Override
    public void layout() {
        switch (mTheme) {
            case 0:
                cutBorderEqualPart(getOuterBorder(), 5, Line.Direction.HORIZONTAL);
                break;
            case 1:
                cutBorderEqualPart(getOuterBorder(), 5, Line.Direction.VERTICAL);
                break;
            case 2:
                addLine(getOuterBorder(), Line.Direction.HORIZONTAL, 2f / 5);
                addLine(getBorder(0), Line.Direction.VERTICAL, 1f / 2);
                cutBorderEqualPart(getBorder(2), 3, Line.Direction.VERTICAL);
                break;
            case 3:
                addLine(getOuterBorder(), Line.Direction.HORIZONTAL, 3f / 5);
                cutBorderEqualPart(getBorder(0), 3, Line.Direction.VERTICAL);
                addLine(getBorder(3), Line.Direction.VERTICAL, 1f / 2);
                break;
            case 4:
                addLine(getOuterBorder(), Line.Direction.VERTICAL, 2f / 5);
                cutBorderEqualPart(getBorder(0), 3, Line.Direction.HORIZONTAL);
                addLine(getBorder(1), Line.Direction.HORIZONTAL, 1f / 2);
                break;
            case 5:
                addLine(getOuterBorder(), Line.Direction.VERTICAL, 2f / 5);
                cutBorderEqualPart(getBorder(1), 3, Line.Direction.HORIZONTAL);
                addLine(getBorder(0), Line.Direction.HORIZONTAL, 1f / 2);
                break;
            case 6:
                addLine(getOuterBorder(), Line.Direction.HORIZONTAL, 3f / 4);
                cutBorderEqualPart(getBorder(1), 4, Line.Direction.VERTICAL);
                break;
            case 7:
                addLine(getOuterBorder(), Line.Direction.HORIZONTAL, 1f / 4);
                cutBorderEqualPart(getBorder(0), 4, Line.Direction.VERTICAL);
                break;
            case 8:
                addLine(getOuterBorder(), Line.Direction.VERTICAL, 3f / 4);
                cutBorderEqualPart(getBorder(1), 4, Line.Direction.HORIZONTAL);
                break;
            case 9:
                addLine(getOuterBorder(), Line.Direction.VERTICAL, 1f / 4);
                cutBorderEqualPart(getBorder(0), 4, Line.Direction.HORIZONTAL);
                break;
            case 10:
                addLine(getOuterBorder(), Line.Direction.HORIZONTAL, 1f / 4);
                addLine(getBorder(1), Line.Direction.HORIZONTAL, 2f / 3);
                addLine(getBorder(0), Line.Direction.VERTICAL, 1f / 2);
                addLine(getBorder(3), Line.Direction.VERTICAL, 1f / 2);
                break;
            case 11:
                addLine(getOuterBorder(), Line.Direction.VERTICAL, 1f / 4);
                addLine(getBorder(1), Line.Direction.VERTICAL, 2f / 3);
                addLine(getBorder(0), Line.Direction.HORIZONTAL, 1f / 2);
                addLine(getBorder(2), Line.Direction.HORIZONTAL, 1f / 2);
                break;
            case 12:
                addCross(getOuterBorder(), 1f / 3);
                addLine(getBorder(2), Line.Direction.HORIZONTAL, 1f / 2);
                break;
            case 13:
                addCross(getOuterBorder(), 2f / 3);
                addLine(getBorder(1), Line.Direction.HORIZONTAL, 1f / 2);
                break;
            case 14:
                addCross(getOuterBorder(), 1f / 3, 2f / 3);
                addLine(getBorder(3), Line.Direction.HORIZONTAL, 1f / 2);
                break;
            case 15:
                addCross(getOuterBorder(), 2f / 3, 1f / 3);
                addLine(getBorder(0), Line.Direction.HORIZONTAL, 1f / 2);
                break;
            case 16:
                cutSpiral(getOuterBorder());
                break;
            default:
                cutBorderEqualPart(getOuterBorder(), 5, Line.Direction.HORIZONTAL);
                break;
        }
    }
}
