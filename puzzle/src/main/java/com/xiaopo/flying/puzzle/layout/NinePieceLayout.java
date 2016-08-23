package com.xiaopo.flying.puzzle.layout;

import com.xiaopo.flying.puzzle.Line;

/**
 * Created by snowbean on 16-8-17.
 */
public class NinePieceLayout extends NumberPieceLayout {
    public NinePieceLayout(int theme) {
        super(theme);
    }

    @Override
    public int getThemeCount() {
        return 8;
    }

    @Override
    public void layout() {
        switch (mTheme) {
            case 0:
                cutBorderEqualPart(getOuterBorder(), 2, 2);
                break;
            case 1:
                addLine(getOuterBorder(), Line.Direction.VERTICAL, 3f / 4);
                addLine(getBorder(0), Line.Direction.VERTICAL, 1f / 4);
                cutBorderEqualPart(getBorder(2), 4, Line.Direction.HORIZONTAL);
                cutBorderEqualPart(getBorder(0), 4, Line.Direction.HORIZONTAL);
                break;
            case 2:
                addLine(getOuterBorder(), Line.Direction.HORIZONTAL, 3f / 4);
                addLine(getBorder(0), Line.Direction.HORIZONTAL, 1f / 4);
                cutBorderEqualPart(getBorder(2), 4, Line.Direction.VERTICAL);
                cutBorderEqualPart(getBorder(0), 4, Line.Direction.VERTICAL);
                break;
            case 3:
                addLine(getOuterBorder(), Line.Direction.HORIZONTAL, 3f / 4);
                addLine(getBorder(0), Line.Direction.HORIZONTAL, 1f / 4);
                cutBorderEqualPart(getBorder(2), 3, Line.Direction.VERTICAL);
                addLine(getBorder(1), Line.Direction.VERTICAL, 3f / 4);
                addLine(getBorder(1), Line.Direction.VERTICAL, 1f / 4);
                cutBorderEqualPart(getBorder(0), 3, Line.Direction.VERTICAL);
                break;
            case 4:
                addLine(getOuterBorder(), Line.Direction.VERTICAL, 3f / 4);
                addLine(getBorder(0), Line.Direction.VERTICAL, 1f / 4);
                cutBorderEqualPart(getBorder(2), 3, Line.Direction.HORIZONTAL);
                addLine(getBorder(1), Line.Direction.HORIZONTAL, 3f / 4);
                addLine(getBorder(1), Line.Direction.HORIZONTAL, 1f / 4);
                cutBorderEqualPart(getBorder(0), 3, Line.Direction.HORIZONTAL);
                break;
            case 5:
                cutBorderEqualPart(getOuterBorder(), 3, Line.Direction.VERTICAL);
                addLine(getBorder(2), Line.Direction.HORIZONTAL, 3f / 4);
                addLine(getBorder(2), Line.Direction.HORIZONTAL, 1f / 4);
                cutBorderEqualPart(getBorder(1), 3, Line.Direction.HORIZONTAL);
                addLine(getBorder(0), Line.Direction.HORIZONTAL, 3f / 4);
                addLine(getBorder(0), Line.Direction.HORIZONTAL, 1f / 4);
                break;
            case 6:
                cutBorderEqualPart(getOuterBorder(), 3, Line.Direction.HORIZONTAL);
                addLine(getBorder(2), Line.Direction.VERTICAL, 3f / 4);
                addLine(getBorder(2), Line.Direction.VERTICAL, 1f / 4);
                cutBorderEqualPart(getBorder(1), 3, Line.Direction.VERTICAL);
                addLine(getBorder(0), Line.Direction.VERTICAL, 3f / 4);
                addLine(getBorder(0), Line.Direction.VERTICAL, 1f / 4);
                break;

            case 7:
                addLine(getOuterBorder(), Line.Direction.HORIZONTAL, 1f / 2);
                cutBorderEqualPart(getBorder(1), 1, 3);
                break;
            default:
                break;
        }
    }

}
