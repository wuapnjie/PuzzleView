package com.xiaopo.flying.puzzle.layout;

import android.os.Parcel;

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

    @Override
    public int getThemeCount() {
        return 3;
    }

    @Override
    public void layout() {
        switch (mTheme) {
            case 0:
                addLine(getOuterBorder(), Line.Direction.HORIZONTAL, 1f/2);
                break;
            case 1:
                addLine(getOuterBorder(), Line.Direction.VERTICAL, 1f/2);
                break;
            case 2:
                addCross(getOuterBorder(), 1f / 2);
                break;
            default:
                addLine(getOuterBorder(), Line.Direction.HORIZONTAL, 1f/2);
                break;
        }

    }

}
