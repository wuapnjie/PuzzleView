package com.xiaopo.flying.puzzle.layout;

import android.util.Log;

import com.xiaopo.flying.puzzle.Line;

/**
 * Created by snowbean on 16-8-17.
 */
public class TwoPieceLayout extends NumberPieceLayout {
    private float mRadio = 1f / 2;

    public TwoPieceLayout(int theme) {
        super(theme);
    }

    public TwoPieceLayout(float radio, int theme) {
        super(theme);
        if (mRadio > 1) {
            Log.e(TAG, "CrossLayout: the radio can not greater than 1f");
            mRadio = 1f;
        }
        mRadio = radio;
    }

    @Override
    public int getThemeCount() {
        return 7;
    }

    @Override
    public void layout() {
        switch (mTheme) {
            case 0:
                addLine(getOuterBorder(), Line.Direction.HORIZONTAL, mRadio);
                break;
            case 1:
                addLine(getOuterBorder(), Line.Direction.VERTICAL, mRadio);
                break;
            case 2:
                addLine(getOuterBorder(), Line.Direction.HORIZONTAL, 1f / 3);
                break;
            case 3:
                addLine(getOuterBorder(), Line.Direction.HORIZONTAL, 2f / 3);
                break;
            case 4:
                addLine(getOuterBorder(), Line.Direction.VERTICAL, 1f / 3);
                break;
            case 5:
                addLine(getOuterBorder(), Line.Direction.VERTICAL, 2f / 3);
                break;
            case 6:
                addCross(getOuterBorder(), 1f / 2);
                break;
            default:
                addLine(getOuterBorder(), Line.Direction.HORIZONTAL, mRadio);
                break;
        }

    }

}
