package com.xiaopo.flying.puzzle.layout;

import com.xiaopo.flying.puzzle.Line;

/**
 * Created by snowbean on 16-8-17.
 */
public class EightPieceLayout extends NumberPieceLayout {
    public EightPieceLayout(int theme) {
        super(theme);
    }

    @Override
    public int getThemeCount() {
        return 11;
    }

    @Override
    public void layout() {
        switch (mTheme) {
            case 0:
                cutBorderEqualPart(getOuterBorder(), 3, 1);
                break;
            case 1:
                cutBorderEqualPart(getOuterBorder(), 1, 3);
                break;
            case 2:
                cutBorderEqualPart(getOuterBorder(), 4, Line.Direction.VERTICAL);
                addLine(getBorder(3), Line.Direction.HORIZONTAL, 4f / 5);
                addLine(getBorder(2), Line.Direction.HORIZONTAL, 3f / 5);
                addLine(getBorder(1), Line.Direction.HORIZONTAL, 2f / 5);
                addLine(getBorder(0), Line.Direction.HORIZONTAL, 1f / 5);
                break;
            case 3:
                cutBorderEqualPart(getOuterBorder(), 4, Line.Direction.HORIZONTAL);
                addLine(getBorder(3), Line.Direction.VERTICAL, 4f / 5);
                addLine(getBorder(2), Line.Direction.VERTICAL, 3f / 5);
                addLine(getBorder(1), Line.Direction.VERTICAL, 2f / 5);
                addLine(getBorder(0), Line.Direction.VERTICAL, 1f / 5);
                break;
            case 4:
                cutBorderEqualPart(getOuterBorder(), 4, Line.Direction.VERTICAL);
                addLine(getBorder(3), Line.Direction.HORIZONTAL, 1f / 5);
                addLine(getBorder(2), Line.Direction.HORIZONTAL, 2f / 5);
                addLine(getBorder(1), Line.Direction.HORIZONTAL, 3f / 5);
                addLine(getBorder(0), Line.Direction.HORIZONTAL, 4f / 5);
                break;
            case 5:
                cutBorderEqualPart(getOuterBorder(), 4, Line.Direction.HORIZONTAL);
                addLine(getBorder(3), Line.Direction.VERTICAL, 1f / 5);
                addLine(getBorder(2), Line.Direction.VERTICAL, 2f / 5);
                addLine(getBorder(1), Line.Direction.VERTICAL, 3f / 5);
                addLine(getBorder(0), Line.Direction.VERTICAL, 4f / 5);
                break;
            case 6:
                cutBorderEqualPart(getOuterBorder(), 3, Line.Direction.HORIZONTAL);
                cutBorderEqualPart(getBorder(2), 3, Line.Direction.VERTICAL);
                cutBorderEqualPart(getBorder(1), 2, Line.Direction.VERTICAL);
                cutBorderEqualPart(getBorder(0), 3, Line.Direction.VERTICAL);
                break;
            case 7:
                cutBorderEqualPart(getOuterBorder(), 3, Line.Direction.VERTICAL);
                cutBorderEqualPart(getBorder(2), 3, Line.Direction.HORIZONTAL);
                cutBorderEqualPart(getBorder(1), 2, Line.Direction.HORIZONTAL);
                cutBorderEqualPart(getBorder(0), 3, Line.Direction.HORIZONTAL);
                break;
            case 8:
                addLine(getOuterBorder(), Line.Direction.HORIZONTAL, 4f / 5);
                cutBorderEqualPart(getBorder(1), 5, Line.Direction.VERTICAL);
                addLine(getBorder(0), Line.Direction.HORIZONTAL, 1f / 2);
                addLine(getBorder(1), Line.Direction.VERTICAL, 1f / 2);
                break;
            case 9:
                cutBorderEqualPart(getOuterBorder(), 3, Line.Direction.HORIZONTAL);
                cutBorderEqualPart(getBorder(2), 2, Line.Direction.VERTICAL);
                cutBorderEqualPart(getBorder(1), 3, Line.Direction.VERTICAL);
                addLine(getBorder(0), Line.Direction.VERTICAL, 3f / 5);
                addLine(getBorder(0), Line.Direction.VERTICAL, 1f / 3);
                break;
            case 10:
                cutBorderEqualPart(getOuterBorder(),2,1);
                addLine(getBorder(5), Line.Direction.VERTICAL,1f/2);
                addLine(getBorder(4), Line.Direction.VERTICAL,1f/2);
                break;
            default:
                break;
        }
    }

}
